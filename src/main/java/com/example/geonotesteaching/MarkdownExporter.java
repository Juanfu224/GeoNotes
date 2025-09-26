// - [ ID 1] Título — (lat, lon) — YYYY-MM-DD
package com.example.geonotesteaching;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

final class MarkdownExporter implements Exporter {

    public MarkdownExporter() {
    }

    private final Map<Long, Note> notes = new LinkedHashMap<>();

    public void addNote(Note note) {
        notes.put(note.id(), note);
    }

    public Note getNote(long id) {
        return notes.get(id);
    }

    public Map<Long, Note> getNotes() {
        return notes;
    }

    @Override
    public String export() {
        // - [ ID 1] Título — (lat, lon) — YYYY-MM-DD
        var notesList = notes.values().stream()
                // Un 'text block' es una cadena de texto multilinea que no necesita
                // concatenación ni caracteres de escape para las comillas.
                .map(note -> """
                        - [ID %d], "Titulo" - %s - (%.4f,  .4%f) - "%s"
                            """
                        .formatted(
                                note.id(), note.title(),
                                note.location().lat(), note.location().lon(),
                                note.createdAt()))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.joining(",\n"));

        return """
                { "notes": [ %s ] }
                """.formatted(notesList);
    }
}
