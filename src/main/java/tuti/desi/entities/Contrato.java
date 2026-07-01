package tuti.desi.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import tuti.desi.entities.enums.EstadoContrato;

@Entity
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La propiedad es obligatoria")
    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @NotNull(message = "El propietario es obligatorio")
    @ManyToOne
    @JoinColumn(name = "propietario_id", nullable = false)
    private Persona propietario;

    @NotNull(message = "El inquilino es obligatorio")
    @ManyToOne
    @JoinColumn(name = "inquilino_id", nullable = false)
    private Persona inquilino;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser al menos 1 mes")
    @Column(nullable = false)
    private Integer duracionMeses;

    @NotNull(message = "El importe mensual es obligatorio")
    @Positive(message = "El importe mensual debe ser un número positivo")
    @Column(nullable = false)
    private Double importeMensual;

    @NotNull(message = "El día de vencimiento es obligatorio")
    @Min(value = 1, message = "El día de vencimiento debe ser entre 1 y 31")
    @Column(nullable = false)
    private Integer diaVencimientoMensual;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El estado del contrato es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoContrato estadoContrato;

    @Column(nullable = false)
    private Boolean eliminado = false;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoContrato> historialEstados = new ArrayList<>();

    // Constructores
    public Contrato() {
        this.estadoContrato = EstadoContrato.Borrador;
        this.eliminado = false;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }

    public Persona getPropietario() { return propietario; }
    public void setPropietario(Persona propietario) { this.propietario = propietario; }

    public Persona getInquilino() { return inquilino; }
    public void setInquilino(Persona inquilino) { this.inquilino = inquilino; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public Integer getDuracionMeses() { return duracionMeses; }
    public void setDuracionMeses(Integer duracionMeses) { this.duracionMeses = duracionMeses; }

    public Double getImporteMensual() { return importeMensual; }
    public void setImporteMensual(Double importeMensual) { this.importeMensual = importeMensual; }

    public Integer getDiaVencimientoMensual() { return diaVencimientoMensual; }
    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) { this.diaVencimientoMensual = diaVencimientoMensual; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoContrato getEstadoContrato() { return estadoContrato; }
    public void setEstadoContrato(EstadoContrato estadoContrato) { this.estadoContrato = estadoContrato; }

    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }

    public List<HistorialEstadoContrato> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoContrato> historialEstados) { this.historialEstados = historialEstados; }

    // Método para agregar cambio de estado al historial
    public void agregarCambioEstado(EstadoContrato estadoAnterior, EstadoContrato estadoNuevo) {
        HistorialEstadoContrato historial = new HistorialEstadoContrato();
        historial.setContrato(this);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        this.historialEstados.add(historial);
    }

    // Método para obtener fecha de fin
    public LocalDate getFechaFin() {
        return fechaInicio.plusMonths(duracionMeses);
    }

    @Override
    public String toString() {
        return propiedad.getDireccion() + " - " + inquilino.getNombreCompleto();
    }
}