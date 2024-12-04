package com.software.API.controlador;

import com.software.API.modelo.ObraSocial;
import com.software.API.servicio.ServicioAPISalud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/servicio-salud/obras-sociales")
public class ObrasSocialesControlador {

    private final ServicioAPISalud servicioAPISalud;

    @Autowired
    public ObrasSocialesControlador(ServicioAPISalud servicioAPISalud) {
        this.servicioAPISalud = servicioAPISalud;
    }

    @GetMapping
    public ResponseEntity<List<ObraSocial>> obtenerTodasLasObrasSociales() {
        try {
            List<ObraSocial> obrasSociales = servicioAPISalud.obtenerTodasLasObrasSociales();
            return ResponseEntity.ok(obrasSociales);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ObraSocial> obtenerObraSocialPorCodigo(@PathVariable String codigo) {
        try {
            ObraSocial obraSocial = servicioAPISalud.obtenerObraSocialPorCodigo(codigo);
            if (obraSocial == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(obraSocial);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
