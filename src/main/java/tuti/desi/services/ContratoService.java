package tuti.desi.services;

import java.time.LocalDate;
import java.util.List;

import tuti.desi.entities.Contrato;
import tuti.desi.entities.enums.EstadoContrato;
import tuti.desi.exceptions.Excepcion;

public interface ContratoService {
    Contrato save(Contrato contrato) throws Excepcion;
    Contrato getById(Long id) throws Excepcion;
    List<Contrato> getAll();
    List<Contrato> getAllActivos();
    void deleteById(Long id) throws Excepcion;
    List<Contrato> filter(Long propiedadId, Long inquilinoId, EstadoContrato estado, LocalDate fechaInicio);
    boolean existsContratoActivoByPropiedad(Long propiedadId, Long id);
    void cambiarEstado(Contrato contrato, EstadoContrato nuevoEstado) throws Excepcion;
}