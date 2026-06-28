package com.tuti.desi.PozzerOteroSosaNaranjo.entity;

import java.time.LocalDateTime;

import com.tuti.desi.PozzerOteroSosaNaranjo.enums.EstadoContrato;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class HistorialEstadoContrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private EstadoContrato estado;

	private LocalDateTime fechaHora;

	@ManyToOne
	@JoinColumn(name = "contrato_id")
	private Contrato contrato;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EstadoContrato getEstado() {
		return estado;
	}

	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
}
