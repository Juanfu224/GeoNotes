package com.example.geonotesteaching;

import java.util.Comparator;

public class NoteOrderDate implements Comparator<Note> {

    @Override
    public int compare(Note nota1, Note nota2) {
        return nota2.createdAt().compareTo(nota1.createdAt());
    }
    

}
