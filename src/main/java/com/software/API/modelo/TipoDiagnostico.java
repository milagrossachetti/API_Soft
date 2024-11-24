package com.software.API.modelo;

import lombok.Getter;

@Getter
public enum TipoDiagnostico {
    FIEBRE_DEL_DENGUE("Fiebre del dengue [Dengue clásico]"),
    FIEBRE_DEL_DENGUE_HEMORRAGICO("Fiebre del dengue hemorrágico"),
    GASTROENTEROPATIA_AGUDA_DEBIDA_AL_AGENTE_DE_NORWALK("Gastroenteropatía aguda debida al agente de Norwalk"),
    DIARREA_Y_GASTROENTERITIS_DE_PRESUNTO_ORIGEN_INFECCIOSO("Diarrea y gastroenteritis de presunto origen infeccioso"),
    MUCORMICOSIS_GASTROINTESTINAL("Mucormicosis gastrointestinal"),
    SINUSITIS_ESFENOIDAL_AGUDA("Sinusitis esfenoidal aguda"),
    SINUSITIS_ETMOIDAL_AGUDA("Sinusitis etmoidal aguda"),
    SINUSITIS_FRONTAL_AGUDA("Sinusitis frontal aguda"),
    SINUSITIS_MAXILAR_AGUDA("Sinusitis maxilar aguda"),
    FIEBRE_PARATIFOIDEA_NO_ESPECIFICADA("Fiebre paratifoidea, no especificada"),
    FIEBRE_PARATIFOIDEA_C("Fiebre paratifoidea C"),
    FIEBRE_PARATIFOIDEA_B("Fiebre paratifoidea B"),
    FIEBRE_PARATIFOIDEA_A("Fiebre paratifoidea A"),
    FIEBRE_TIFOIDEA("Fiebre tifoidea"),
    FATIGA_POR_CALOR_TRANSITORIA("Fatiga por calor, transitoria"),
    MALESTAR_Y_FATIGA("Malestar y fatiga"),
    SINDROME_DE_FATIGA_POSTVIRAL("Síndrome de fatiga postviral");

    private final String nombre;

    TipoDiagnostico(String nombre) {
        this.nombre = nombre;
    }

    public static TipoDiagnostico fromNombre(String nombre) {
        for (TipoDiagnostico diagnostico : TipoDiagnostico.values()) {
            if (diagnostico.getNombre().equalsIgnoreCase(nombre.trim())) {
                return diagnostico;
            }
        }
        throw new IllegalArgumentException("Diagnóstico no reconocido: " + nombre);
    }
}
