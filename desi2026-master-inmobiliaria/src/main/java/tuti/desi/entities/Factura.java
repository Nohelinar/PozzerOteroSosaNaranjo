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

import tuti.desi.entities.enums.EstadoFactura;
import tuti.desi.entities.enums.MedioPago;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El contrato es obligatorio")
    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @NotNull(message = "El concepto es obligatorio")
    @Column(nullable = false)
    private String concepto;

    @NotNull(message = "La fecha de emisión es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser un número positivo")
    @Column(nullable = false)
    private Double importe;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estadoFactura;

    // Datos de pago (opcionales)
    private LocalDate fechaPago;
    
    @Enumerated(EnumType.STRING)
    private MedioPago medioPago;
    
    private Double importePagado;
    private Double interes;

    @Column(nullable = false)
    private Boolean eliminado = false;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoFactura> historialEstados = new ArrayList<>();

    // Constructor
    public Factura() {
        this.estadoFactura = EstadoFactura.Pendiente;
        this.eliminado = false;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public Double getImporte() { return importe; }
    public void setImporte(Double importe) { this.importe = importe; }

    public EstadoFactura getEstadoFactura() { return estadoFactura; }
    public void setEstadoFactura(EstadoFactura estadoFactura) { this.estadoFactura = estadoFactura; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public MedioPago getMedioPago() { return medioPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }

    public Double getImportePagado() { return importePagado; }
    public void setImportePagado(Double importePagado) { this.importePagado = importePagado; }

    public Double getInteres() { return interes; }
    public void setInteres(Double interes) { this.interes = interes; }

    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }

    public List<HistorialEstadoFactura> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoFactura> historialEstados) { this.historialEstados = historialEstados; }

    // Método para agregar cambio de estado al historial
    public void agregarCambioEstado(EstadoFactura estadoAnterior, EstadoFactura estadoNuevo) {
        HistorialEstadoFactura historial = new HistorialEstadoFactura();
        historial.setFactura(this);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        this.historialEstados.add(historial);
    }

    @Override
    public String toString() {
        return contrato.getPropiedad().getDireccion() + " - " + concepto;
    }
}