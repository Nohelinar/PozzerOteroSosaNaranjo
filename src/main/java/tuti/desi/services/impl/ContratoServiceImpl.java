package tuti.desi.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.entities.Contrato;
import tuti.desi.entities.Propiedad;
import tuti.desi.entities.enums.EstadoContrato;
import tuti.desi.entities.enums.EstadoDisponibilidad;
import tuti.desi.exceptions.EntidadNoEncontradaException;
import tuti.desi.exceptions.Excepcion;
import tuti.desi.repository.ContratoRepository;
import tuti.desi.services.ContratoService;
import tuti.desi.services.PersonaService;
import tuti.desi.services.PropiedadService;

@Service
@Transactional
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository repo;
    private final PropiedadService propiedadService;
    private final PersonaService personaService;

    public ContratoServiceImpl(ContratoRepository repo, PropiedadService propiedadService, PersonaService personaService) {
        this.repo = repo;
        this.propiedadService = propiedadService;
        this.personaService = personaService;
    }

    @Override
    @Transactional
    public Contrato save(Contrato contrato) throws Excepcion {
        // Validar propiedad
        if (contrato.getPropiedad() == null || contrato.getPropiedad().getId() == null) {
            throw new Excepcion("La propiedad es obligatoria", "propiedad");
        }
        Propiedad propiedad = propiedadService.getById(contrato.getPropiedad().getId());

        // Validar propietario
        if (contrato.getPropietario() == null || contrato.getPropietario().getId() == null) {
            throw new Excepcion("El propietario es obligatorio", "propietario");
        }
        personaService.getById(contrato.getPropietario().getId());

        // Validar inquilino
        if (contrato.getInquilino() == null || contrato.getInquilino().getId() == null) {
            throw new Excepcion("El inquilino es obligatorio", "inquilino");
        }
        personaService.getById(contrato.getInquilino().getId());

        // Validar que propietario e inquilino no sean la misma persona
        if (contrato.getPropietario().getId().equals(contrato.getInquilino().getId())) {
            throw new Excepcion("El propietario y el inquilino no pueden ser la misma persona", "inquilino");
        }

        // Validar día de vencimiento
        if (contrato.getDiaVencimientoMensual() < 1 || contrato.getDiaVencimientoMensual() > 31) {
            throw new Excepcion("El día de vencimiento debe ser entre 1 y 31", "diaVencimientoMensual");
        }

        // Si el contrato se activa, validar que la propiedad esté disponible
        if (contrato.getEstadoContrato() == EstadoContrato.Activo) {
            // Verificar que no haya otro contrato activo
            if (repo.existsContratoActivoByPropiedad(propiedad.getId(), contrato.getId())) {
                throw new Excepcion("La propiedad ya tiene un contrato activo", "propiedad");
            }

            // Verificar que la propiedad esté disponible
            if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.Disponible) {
                throw new Excepcion("La propiedad no está disponible para alquilar", "propiedad");
            }
        }

        // Guardar contrato
        Contrato saved = repo.save(contrato);

        // Si es nuevo y está activo, actualizar estado de la propiedad
        if (contrato.getId() == null && contrato.getEstadoContrato() == EstadoContrato.Activo) {
            propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.Alquilada);
            propiedadService.cambiarEstado(propiedad, EstadoDisponibilidad.Alquilada);
        }

        return saved;
    }

    @Override
    public Contrato getById(Long id) throws Excepcion {
        return repo.findById(id)
            .orElseThrow(() -> new EntidadNoEncontradaException("Contrato", id));
    }

    @Override
    public List<Contrato> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Contrato> getAllActivos() {
        return repo.findAllActivos();
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws Excepcion {
        Contrato contrato = getById(id);

        // Solo se pueden eliminar contratos en estado Borrador
        if (contrato.getEstadoContrato() != EstadoContrato.Borrador) {
            throw new Excepcion("Solo se pueden eliminar contratos en estado Borrador");
        }

        contrato.setEliminado(true);
        repo.save(contrato);
    }

    @Override
    public List<Contrato> filter(Long propiedadId, Long inquilinoId, EstadoContrato estado, LocalDate fechaInicio) {
        return repo.filter(propiedadId, inquilinoId, estado, fechaInicio);
    }

    @Override
    public boolean existsContratoActivoByPropiedad(Long propiedadId, Long id) {
        return repo.existsContratoActivoByPropiedad(propiedadId, id);
    }

    @Override
    @Transactional
    public void cambiarEstado(Contrato contrato, EstadoContrato nuevoEstado) throws Excepcion {
        EstadoContrato estadoAnterior = contrato.getEstadoContrato();

        // Validar transiciones permitidas
        if (estadoAnterior == EstadoContrato.Borrador && nuevoEstado == EstadoContrato.Activo) {
            // OK
        } else if (estadoAnterior == EstadoContrato.Activo && (nuevoEstado == EstadoContrato.Finalizado || nuevoEstado == EstadoContrato.Rescindido)) {
            // OK
        } else {
            throw new Excepcion("No se permite cambiar de " + estadoAnterior + " a " + nuevoEstado);
        }

        // Si pasa a Activo, validar propiedad
        if (nuevoEstado == EstadoContrato.Activo) {
            Propiedad propiedad = contrato.getPropiedad();
            if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.Disponible) {
                throw new Excepcion("La propiedad no está disponible", "propiedad");
            }
            if (repo.existsContratoActivoByPropiedad(propiedad.getId(), contrato.getId())) {
                throw new Excepcion("La propiedad ya tiene un contrato activo", "propiedad");
            }
        }

        // Actualizar estado
        contrato.setEstadoContrato(nuevoEstado);
        contrato.agregarCambioEstado(estadoAnterior, nuevoEstado);
        repo.save(contrato);

        // Actualizar estado de la propiedad
        Propiedad propiedad = contrato.getPropiedad();
        if (nuevoEstado == EstadoContrato.Activo) {
            propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.Alquilada);
            propiedadService.cambiarEstado(propiedad, EstadoDisponibilidad.Alquilada);
        } else if (nuevoEstado == EstadoContrato.Finalizado || nuevoEstado == EstadoContrato.Rescindido) {
            propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.Disponible);
            propiedadService.cambiarEstado(propiedad, EstadoDisponibilidad.Disponible);
        }
    }
}