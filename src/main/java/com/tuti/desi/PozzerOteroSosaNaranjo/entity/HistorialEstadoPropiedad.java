
package com.tuti.desi.PozzerOteroSosaNaranjo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.tuti.desi.PozzerOteroSosaNaranjo.enums.EstadoPropiedad;

@Entity
public class HistorialEstadoPropiedad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "propiedad_id")
	private Propiedad propiedad;

	@Enumerated(EnumType.STRING)
	private EstadoPropiedad estadoPropiedad;

	private LocalDateTime fechaCambio;

	public Long getId() {
		return id;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public EstadoPropiedad getEstadoPropiedad() {
		return estadoPropiedad;
	}

	public void setEstadoPropiedad(EstadoPropiedad estadoPropiedad) {
		this.estadoPropiedad = estadoPropiedad;
	}

	public LocalDateTime getFechaCambio() {
		return fechaCambio;
	}

	public void setFechaCambio(LocalDateTime fechaCambio) {
		this.fechaCambio = fechaCambio;
	}
}