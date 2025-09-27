package com.example.geonotesteaching;

// Esta clase usa 'switch expressions' y 'pattern matching' para describir un 'Attachment'.
// Los 'switch expressions' permiten que el 'switch' sea una expresión que devuelve un valor.
// El 'pattern matching' en el 'case' permite desestructurar el objeto y
// aplicar una condición ('when') de forma concisa.

/**
 * Clase final que proporciona métodos para describir attachments
 * Utiliza switch expressions modernos con pattern matching
 */
final class Describe {
    public static String describeAttachment(Attachment a) {
        // Switch expression que maneja todos los tipos de Attachment
        return switch (a) {
            // Caso para fotos en alta definición (ancho > 1920px)
            case Photo p when p.width() > 1920 ->
                "📷 Foto en alta definición (%d x %d)".formatted(p.width(), p.height());

            // Caso para fotos normales
            case Photo p -> "📷 Foto";

            // MODIFICADO: Audio largo con bloque y yield
            // Caso para audios largos (más de 300 segundos/5 minutos)
            case Audio audio when audio.duration() > 300 -> {
                // Calcula la duración en minutos (división entera)
                var mins = audio.duration() / 60;
                // yield explícito para retornar desde dentro del bloque
                yield "🎵 Audio (" + mins + " minutos)";
            }

            // Caso para audios normales (300 segundos o menos)
            case Audio audio -> "🎵 Audio";

            // Caso para enlaces: muestra la etiqueta si existe, sino la URL
            case Link l -> "🔗 %s".formatted(
                    (l.label() == null || l.label().isEmpty()) ? l.url() : l.label());

            // Caso para videos largos (más de 120 segundos/2 minutos)
            case Video v when v.seconds() > 120 -> "🎬 Vídeo largo";

            // Caso para videos normales (120 segundos o menos)
            case Video v -> "🎬 Vídeo";
        };
    }

    static int mediaPixels(Attachment a) {
        int resultado = 0;

        if (a instanceof Photo) {
            Photo p = (Photo) a;
            resultado = p.width() * p.height();

        } else if (a instanceof Video) {
            Video v = (Video) a;
            resultado = v.width() * v.height();

        } else
            resultado = 0;
        return resultado;
    }
}