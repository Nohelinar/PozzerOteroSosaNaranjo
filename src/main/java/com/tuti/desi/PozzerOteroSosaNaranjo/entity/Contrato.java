package com.tuti.desi.PozzerOteroSosaNaranjo.entity;

import java.time.LocalDate;
import java.util.List;

import com.tuti.desi.PozzerOteroSosaNaranjo.enums.EstadoContrato;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Contrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "propiedad_id")
	private Propiedad propiedad;

	@ManyToOne
	@JoinColumn(name = "inquilino_id")
	private Persona inquilino;
	
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaInicio;

	private Integer duracionMeses;

	private BigDecimal importeMensual;

	private Integer diaVencimientoMensual;

	private String descripcion;

	@Enumerated(EnumType.STRING)
	private EstadoContrato estado;

	private boolean eliminado;

	@OneToMany(mappedBy = "contrato")
	private List<HistorialEstadoContrato> historialEstados;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public Persona getInquilino() {
		return inquilino;
	}

	public void setInquilino(Persona inquilino) {
		this.inquilino = inquilino;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Integer getDuracionMeses() {
		return duracionMeses;
	}

	public void setDuracionMeses(Integer duracionMeses) {
		this.duracionMeses = duracionMeses;
	}

	public BigDecimal getImporteMensual() {
		return importeMensual;
	}

	public void setImporteMensual(BigDecimal importeMensual) {
		this.importeMensual = importeMensual;
	}

	public Integer getDiaVencimientoMensual() {
		return diaVencimientoMensual;
	}

	public void setDiaVencimientoMensual(Integer diaVencimientoMensual) {
		this.diaVencimientoMensual = diaVencimientoMensual;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoContrato getEstado() {
		return estado;
	}

	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}
}
