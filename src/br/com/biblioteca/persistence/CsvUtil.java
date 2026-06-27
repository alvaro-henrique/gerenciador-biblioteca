package br.com.biblioteca.persistence;

import java.util.ArrayList;
import java.util.List;

public final class CsvUtil {
    private CsvUtil() {
    }

    public static String juntar(String... campos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) {
                sb.append('|');
            }
            sb.append(escapar(campos[i] == null ? "" : campos[i]));
        }
        return sb.toString();
    }

    public static List<String> separar(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean escapando = false;
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (escapando) {
                atual.append(c);
                escapando = false;
            } else if (c == '\\') {
                escapando = true;
            } else if (c == '|') {
                campos.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        campos.add(atual.toString());
        return campos;
    }

    private static String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("|", "\\|");
    }
}
