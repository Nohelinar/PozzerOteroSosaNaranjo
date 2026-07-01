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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

import tuti.desi.entities.enums.EstadoPublicacion;

@Entity
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La propiedad es obligatoria")
    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @NotNull(message = "El precio mensual es obligatorio")
    @Positive(message = "El precio mensual debe ser un número positivo")
    @Column(nullable = false)
    private Double precioMensual;

    @NotBlank(message = "Las condiciones son obligatorias")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String condiciones;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @NotNull(message = "La fecha de publicación es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaPublicacion;

    @NotNull(message = "El estado de publicación es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPublicacion estadoPublicacion;

    @Column(nullable = false)
    private Boolean eliminado = false;

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoPublicacion> historialEstados = new ArrayList<>();

    // Constructores
    public Publicacion() {
        this.estadoPublicacion = EstadoPublicacion.Activa;
        this.eliminado = false;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }

    public Double getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(Double precioMensual) { this.precioMensual = precioMensual; }

    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public EstadoPublicacion getEstadoPublicacion() { return estadoPublicacion; }
    public void setEstadoPublicacion(EstadoPublicacion estadoPublicacion) { this.estadoPublicacion = estadoPublicacion; }

    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }

    public List<HistorialEstadoPublicacion> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoPublicacion> historialEstados) { this.historialEstados = historialEstados; }

    // Método para agregar cambio de estado al historial
    public void agregarCambioEstado(EstadoPublicacion estadoAnterior, EstadoPublicacion estadoNuevo) {
        HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();
        historial.setPublicacion(this);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        this.historialEstados.add(historial);
    }

    @Override
    public String toString() {
        return propiedad.getDireccion() + " - $" + precioMensual;
    }
}