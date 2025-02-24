package com.example.alertas.controller;

import com.example.alertas.dto.AlertaDTO;
import com.example.alertas.model.Alerta;
import com.example.alertas.model.Paciente;
import com.example.alertas.model.SenalVital;
import com.example.alertas.repository.PacienteRepository;
import com.example.alertas.service.AlertaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    public ResponseEntity<Alerta> recibirAlerta(@RequestBody AlertaDTO alertaDTO) {
        System.out.println("📩 Recibiendo alerta vía HTTP: " + alertaDTO);

        Paciente paciente = pacienteRepository.findById(alertaDTO.getPacienteId())
                .orElseThrow(() -> new RuntimeException("⚠️ Paciente no encontrado con ID: " + alertaDTO.getPacienteId()));

        Alerta alertaGuardada = alertaService.guardarAlerta(alertaDTO, paciente);

        System.out.println("✅ Alerta guardada desde HTTP: " + alertaGuardada);
        return ResponseEntity.ok(alertaGuardada);
    }

    @KafkaListener(topics = "topic_alertas", groupId = "group_alertas")
    public void recibirAlertaDesdeKafka(SenalVital senalVital) {
        System.out.println("📥 Alerta recibida desde Kafka: " + senalVital);

        Paciente paciente = pacienteRepository.findById(1L)
                .orElseThrow(() -> {
                    System.err.println("⚠️ Paciente no encontrado con ID: ");
                    return new RuntimeException("Paciente no encontrado");
                });

        if (esAnomalia(senalVital)) {
          AlertaDTO nuevAlerta = new AlertaDTO();
          nuevAlerta.setMensaje("Alerta critica");
          nuevAlerta.setTipoAnomalia("Señales Anomalas");
          nuevAlerta.setPacienteId(paciente.getId());
          Alerta alertaGuardada = alertaService.guardarAlerta(nuevAlerta, paciente);
          System.out.println("✅ Alerta guardada en la BD desde Kafka: " + alertaGuardada);
        }

    }

  private boolean esAnomalia(SenalVital senal) {
      return senal.getRitmoCardiaco() < 60 || senal.getRitmoCardiaco() > 100 ||
             senal.getTemperatura() < 36.0 || senal.getTemperatura() > 37.5 ||
             senal.getPresionSistolica() < 90 || senal.getPresionSistolica() > 140 ||
             senal.getPresionDiastolica() < 60 || senal.getPresionDiastolica() > 90;
  }
}
