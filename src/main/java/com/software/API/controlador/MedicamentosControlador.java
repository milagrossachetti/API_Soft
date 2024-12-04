package com.software.API.controlador;

import com.software.API.modelo.Medicamento;
import com.software.API.servicio.ServicioAPISalud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicio-salud/medicamentos")
public class MedicamentosControlador {

    private final ServicioAPISalud servicioAPISalud;

    @Autowired
    public MedicamentosControlador(ServicioAPISalud servicioAPISalud) {
        this.servicioAPISalud = servicioAPISalud;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Medicamento>> obtenerTodosLosMedicamentos(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "10") int limite) {
        if (pagina < 1 || limite < 1 || limite > 1000) {
            return ResponseEntity.badRequest().body(null); // O podrías devolver un mensaje específico
        }
        try {
            List<Medicamento> medicamentos = servicioAPISalud.obtenerTodosLosMedicamentos(pagina, limite);
            return ResponseEntity.ok(medicamentos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // O también un mensaje en el cuerpo
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Medicamento> obtenerMedicamento(@PathVariable int codigo) {
        try {
            Medicamento medicamento = servicioAPISalud.obtenerMedicamento(codigo);
            if (medicamento == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(medicamento);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Error en la obtención del medicamento
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Medicamento>> obtenerMedicamentosPorDescripcion(@RequestParam String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(List.of()); // Parámetro inválido
        }

        try {
            List<Medicamento> medicamentos = servicioAPISalud.obtenerMedicamentosPorDescripcion(descripcion);

            // Si no se encuentran medicamentos, devolvemos un 404
            if (medicamentos.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Devolvemos la lista de medicamentos encontrados
            return ResponseEntity.ok(medicamentos);
        } catch (Exception e) {
            // En caso de error, devolvemos un 500 con el mensaje del error
            return ResponseEntity.status(500).body(List.of());
        }
    }

}
