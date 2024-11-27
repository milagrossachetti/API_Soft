package com.software.API.repositorio;


import com.software.API.modelo.Medicamento;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RepositorioAPISalud {

        private final List<Medicamento> medicamentos = new ArrayList<>();

        public RepositorioAPISalud() {
            inicializarMedicamentos();
        }

        private void inicializarMedicamentos() {
            medicamentos.add(new Medicamento("Aspirina"));
            medicamentos.add(new Medicamento("Omeprazol"));
            medicamentos.add(new Medicamento("Lexotiroxina sódica"));
            medicamentos.add(new Medicamento("Ramipril"));
            medicamentos.add(new Medicamento("Amlodipina"));
            medicamentos.add(new Medicamento("Paracetamol"));
            medicamentos.add(new Medicamento("Atorvastatina"));
            medicamentos.add(new Medicamento("Salbutamol"));
            medicamentos.add(new Medicamento("Lansoprazol"));
            medicamentos.add(new Medicamento("Amoxicilina"));
            medicamentos.add(new Medicamento("Ibuprofeno"));
            medicamentos.add(new Medicamento("Sertal Compuesto"));
            medicamentos.add(new Medicamento("Sertal Perla"));
            medicamentos.add(new Medicamento("Buscapina"));
        }

        // Método para obtener todos los medicamentos
        public List<Medicamento> obtenerMedicamentos() {
            return new ArrayList<>(medicamentos); // Devolver una copia para evitar modificaciones externas
        }
    }

