package com.software.API.modelo;

import lombok.Getter;

import java.util.List;

@Getter
public enum TipoEstudioLaboratorio {
    HEMOGRAMA_COMPLETO("Hemograma completo", List.of( "Globulos Rojos (Eritrocitos)", "Globulos Blancos (Leucocitos)", "Plaquetas (Trombocitos)", "Hemoglobina (Hb)", "Hematrocito(Hto)", "Indices eritrocitarios")),
    LIPIDOGRAMA("Lipidograma", List.of( "Colesterol total", "Colesterol HDL (colesterol bueno)", "Colesterol LDL (colesterol malo)", "Triglicéridos")),
    ELECTROLITOS_EN_SANGRE("Electrolitos en sangre", List.of( "Sodio (Na)", "Potasio (K)", "Cloruro (Cl)", "Calcio (Ca)")),
    FUNCION_RENAL("Función renal", List.of( "Creatinina", "Nitrógeno ureico en sangre (BUM)")),
    FUNCION_HEPATICA("Función hepática", List.of( "Bilirrubina" , "Transaminasas (AST, ALT)", "Fosfatasa alcalina (ALP)", "Albúmina")),
    ORINA_COMPLETO("Orina completo", List.of( "Proteinuria", "Gluccosuria", "Cetonuria", "Hemoglobinuria", "Bilirrubinuria", "Urobilinuria", "Hematuria", "Cilndruria", "Leucocituria", "Piuria", "Cristaluria", "Celulas mucus", "Ionograma urinario", "pH", "Densidad", "Espuma", "Olor", "Coloración", "Consistencia", "Aspecto")),
    FUNCION_TIROIDEA("Función tiroidea", List.of( "TSH", "T4 libre", "T3")),
    GLUCEMIA("Glucemia", List.of( "Glucosa en ayunas", "Hemoglobina glicosilada (HbA1c)")),
    COPROCULTIVO("Coprocultivo", List.of( "examen físico","Bacteriologico/parasitario")),
    ;

    private final String nombreEstudio;
    private final List<String> items;

    TipoEstudioLaboratorio(String nombreEstudio, List<String> items) {
        this.nombreEstudio = nombreEstudio;
        this.items = items;
    }

    public static List<String> getItemsPorEstudio(String nombreEstudio) {
        for (TipoEstudioLaboratorio estudio : TipoEstudioLaboratorio.values()) {
            if (estudio.getNombreEstudio().equalsIgnoreCase(nombreEstudio)) {
                return estudio.getItems();
            }
        }
        throw new IllegalArgumentException("Tipo de estudio no reconocido: " + nombreEstudio);
    }
}
