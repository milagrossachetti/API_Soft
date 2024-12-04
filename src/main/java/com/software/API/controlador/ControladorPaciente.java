package com.software.API.controlador;


import com.software.API.modelo.Paciente;
import com.software.API.servicio.ServicioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paciente")
public class ControladorPaciente {

    @Autowired
    ServicioPaciente servicioPaciente;

    @GetMapping("/buscar/{cuil}")
    public ResponseEntity<Paciente> verificarPaciente(@PathVariable Long cuil) {
        Paciente existePaciente = servicioPaciente.obtenerPacientePorCuil(cuil);
        if (existePaciente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(existePaciente);
    }

    @GetMapping()
    public ResponseEntity<List<Paciente>> buscarPaciente(@RequestParam Long cuil) {
        List<Paciente> pacientes = servicioPaciente.buscarPacientesPorCuilParcial(cuil);
        if (pacientes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } return ResponseEntity.ok().body(pacientes);
    }
}

