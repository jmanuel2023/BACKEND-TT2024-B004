package com.example.tt2024b004.skincanbe.DTO;

import java.time.LocalDate;

import com.example.tt2024b004.skincanbe.enums.EstadoVinculacion;

public class VinculacionDTO {
    private Long pacienteId;
    private Long especialistaId;
    private LocalDate fechaVinculacion;
    private EstadoVinculacion status;
    // Getters y setters
    public Long getPacienteId() {
        return pacienteId;
    }
    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }
    public Long getEspecialistaId() {
        return especialistaId;
    }
    public void setEspecialistaId(Long especialistaId) {
        this.especialistaId = especialistaId;
    }
    public LocalDate getFechaVinculacion() {
        return fechaVinculacion;
    }
    public void setFechaVinculacion(LocalDate fechaVinculacion) {
        this.fechaVinculacion = fechaVinculacion;
    }
    public EstadoVinculacion getStatus() {
        return status;
    }
    public void setStatus(EstadoVinculacion status) {
        this.status = status;
    }

    
    
}
