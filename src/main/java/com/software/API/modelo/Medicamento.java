package com.software.API.modelo;

public enum Medicamento {
    ASPIRINA("Aspirina"),
    OMEPRAZOL("Omeprazol"),
    LEXOTIROXINA_SODICA("Lexotiroxina sódica"),
    RAMIPRIL("Ramipril"),
    AMLODIPINA("Amlodipina"),
    PARACETAMOL("Paracetamol"),
    ATORVASTATINA("Atorvastatina"),
    SALBUTAMOL("Salbutamol"),
    LANSOPRAZOL("Lansoprazol"),
    AMOXICILINA("Amoxicilina"),
    IBUPROFENO("Ibuprofeno"),
    SERTAL_COMPUESTO("Sertal Compuesto"),
    SERTAL_PERLA("Sertal Perla"),
    BUSCAPINA("Buscapina");

    private final String nombre;

    Medicamento(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static Medicamento fromNombre(String nombre) {
        for (Medicamento medicamento : Medicamento.values()) {
            if (medicamento.getNombre().equalsIgnoreCase(nombre)) {
                return medicamento;
            }
        }
        throw new IllegalArgumentException("Medicamento no reconocido: " + nombre);
    }
}
