package com.example.geonotesteaching;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.stream.Collectors;

// La clase 'Timeline' usa un 'SequencedMap' para mantener las notas en orden de inserción.
// A diferencia de un HashMap, un 'SequencedMap' garantiza el orden y permite acceder
// al primer y último elemento de forma eficiente.
final class Timeline {
    private final SequencedMap<Long, Note> notes = new LinkedHashMap<>();

    public void addNote(Note note) {
        notes.put(note.id(), note);
    }

    public Note getNote(long id) {
        return notes.get(id);
    }

    public Map<Long, Note> getNotes() {
        return notes;
    }

    public java.util.Collection<Note> reversed() {
        return notes.reversed().values();
    }

    public List<Note> latest(int n) {
        List<Note> notas = new ArrayList<>();
        List<Note> notasRecientes = new ArrayList<>();
        notas.addAll(notes.values());
        Collections.sort(notas, new NoteOrderDate());

        for (int i = 0; i < n; i++)
            notasRecientes.add(notas.get(i));

        return notasRecientes;

    }

    public void mostrarNotas(List<Note> notas) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Note nota : notas) {
            long id = nota.id();
            String titulo = nota.title();
            GeoPoint localizacion = nota.location();
            Instant creacion = nota.createdAt();

            double lat = localizacion.lat();
            double lon = localizacion.lon();

            // Convertir el Instant a LocalDate y formatear
            String fechaFormateada = creacion
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(formatter);

            System.out.println("- [ ID " + id + "] " + titulo + " — (" + lat + ", " + lon + ") — " + fechaFormateada);
        }
    }

    // Esta clase final genera la salida JSON usando 'text blocks'.
    public final class Render extends AbstractExporter implements Exporter {
        @Override
        public String export() {
            var notesList = notes.values().stream()
                    // Un 'text block' es una cadena de texto multilinea que no necesita
                    // concatenación ni caracteres de escape para las comillas.
                    .map(note -> """
                            {
                              "id": %d,
                              "title": "%s",
                              "content": "%s",
                              "location": {
                                "lat": %f,
                                "lon": %f },
                              "createdAt": "%s"
                            }
                            """.formatted(
                            note.id(), note.title(), note.content(),
                            note.location().lat(), note.location().lon(),
                            note.createdAt()))
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.joining(",\n"));
            return """
                    { "notes": [ %s ] }
                    """.formatted(notesList);
        }
    }
}