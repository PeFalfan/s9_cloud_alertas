package com.example.alertas.service;

import com.example.alertas.dto.AlertaDTO;
import com.example.alertas.model.Alerta;
import com.example.alertas.model.Paciente;
import com.example.alertas.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    /**
     * Guarda una nueva alerta en la base de datos.
     */
    public Alerta guardarAlerta(AlertaDTO alertaDTO, Paciente paciente) {
        System.out.println("📌 Ejecutando guardarAlerta...");
        System.out.println("📩 Datos recibidos - Mensaje: " + alertaDTO.getMensaje() + ", Tipo: " + alertaDTO.getTipoAnomalia());

        Alerta alerta = new Alerta();
        alerta.setMensaje(alertaDTO.getMensaje());
        alerta.setTipoAnomalia(alertaDTO.getTipoAnomalia());
        alerta.setPaciente(paciente);
        alerta.setFecha(LocalDateTime.now());

        Alerta alertaGuardada = alertaRepository.save(alerta);
        System.out.println("✅ Alerta guardada correctamente en la BD: " + alertaGuardada);

        return alertaGuardada;
    }

    /**
     * Obtiene todas las alertas de la base de datos.
     */
    public List<Alerta> obtenerTodasLasAlertas() {
        System.out.println("📌 Ejecutando obtenerTodasLasAlertas...");
        List<Alerta> alertas = alertaRepository.findAll();
        System.out.println("📋 Número total de alertas obtenidas: " + alertas.size());
        return alertas;
    }

    /**
     * Obtiene una alerta específica por ID.
     */
    public Alerta obtenerAlertaPorId(Long id) {
        System.out.println("📌 Ejecutando obtenerAlertaPorId - ID: " + id);
        return alertaRepository.findById(id)
                .orElseThrow(() -> {
                    System.err.println("⚠️ Alerta no encontrada con ID: " + id);
                    return new RuntimeException("Alerta no encontrada");
                });
    }

    /**
     * Elimina una alerta por su ID.
     */
    public void eliminarAlerta(Long id) {
        System.out.println("📌 Ejecutando eliminarAlerta - ID: " + id);
        if (!alertaRepository.existsById(id)) {
            System.err.println("⚠️ No se puede eliminar, alerta no encontrada con ID: " + id);
            throw new RuntimeException("Alerta no encontrada");
        }
        alertaRepository.deleteById(id);
        System.out.println("🗑️ Alerta eliminada correctamente con ID: " + id);
    }
}
