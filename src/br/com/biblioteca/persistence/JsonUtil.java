package br.com.biblioteca.persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.biblioteca.exception.PersistenciaException;

public final class JsonUtil {
    private JsonUtil() {
    }

    public static void escreverObjetos(Path arquivo, List<Map<String, Object>> objetos) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < objetos.size(); i++) {
            Map<String, Object> objeto = objetos.get(i);
            sb.append("  {");
            int indiceCampo = 0;
            for (Map.Entry<String, Object> entry : objeto.entrySet()) {
                if (indiceCampo++ > 0) {
                    sb.append(", ");
                }
                sb.append(quote(entry.getKey())).append(": ").append(toJsonValue(entry.getValue()));
            }
            sb.append("}");
            if (i < objetos.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]\n");
        Files.writeString(arquivo, sb.toString(), StandardCharsets.UTF_8);
    }

    public static List<Map<String, String>> lerObjetos(Path arquivo) throws IOException, PersistenciaException {
        if (!Files.exists(arquivo)) {
            return new ArrayList<>();
        }
        String conteudo = Files.readString(arquivo, StandardCharsets.UTF_8);
        if (!conteudo.isEmpty() && conteudo.charAt(0) == '\uFEFF') {
            conteudo = conteudo.substring(1);
        }
        conteudo = conteudo.trim();
        if (conteudo.isEmpty()) {
            return new ArrayList<>();
        }
        Parser parser = new Parser(conteudo);
        return parser.parseArray();
    }

    private static String toJsonValue(Object valor) {
        if (valor == null) {
            return "null";
        }
        if (valor instanceof Number || valor instanceof Boolean) {
            return valor.toString();
        }
        return quote(String.valueOf(valor));
    }

    private static String quote(String texto) {
        String escaped = texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
        return "\"" + escaped + "\"";
    }

    private static final class Parser {
        private final String texto;
        private int indice;

        private Parser(String texto) {
            this.texto = texto;
        }

        private List<Map<String, String>> parseArray() throws PersistenciaException {
            List<Map<String, String>> objetos = new ArrayList<>();
            skipWhitespace();
            expect('[');
            skipWhitespace();
            if (peek(']')) {
                expect(']');
                return objetos;
            }
            while (true) {
                objetos.add(parseObject());
                skipWhitespace();
                if (peek(',')) {
                    expect(',');
                    skipWhitespace();
                    continue;
                }
                expect(']');
                return objetos;
            }
        }

        private Map<String, String> parseObject() throws PersistenciaException {
            Map<String, String> objeto = new LinkedHashMap<>();
            expect('{');
            skipWhitespace();
            if (peek('}')) {
                expect('}');
                return objeto;
            }
            while (true) {
                String chave = parseString();
                skipWhitespace();
                expect(':');
                skipWhitespace();
                String valor = parseValue();
                objeto.put(chave, valor);
                skipWhitespace();
                if (peek(',')) {
                    expect(',');
                    skipWhitespace();
                    continue;
                }
                expect('}');
                return objeto;
            }
        }

        private String parseValue() throws PersistenciaException {
            if (peek('"')) {
                return parseString();
            }
            if (peek('n')) {
                expectLiteral("null");
                return null;
            }
            if (peek('t')) {
                expectLiteral("true");
                return "true";
            }
            if (peek('f')) {
                expectLiteral("false");
                return "false";
            }
            return parseNumber();
        }

        private String parseNumber() {
            int inicio = indice;
            if (texto.charAt(indice) == '-') {
                indice++;
            }
            while (indice < texto.length() && Character.isDigit(texto.charAt(indice))) {
                indice++;
            }
            if (indice < texto.length() && texto.charAt(indice) == '.') {
                indice++;
                while (indice < texto.length() && Character.isDigit(texto.charAt(indice))) {
                    indice++;
                }
            }
            return texto.substring(inicio, indice);
        }

        private String parseString() throws PersistenciaException {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (indice < texto.length()) {
                char atual = texto.charAt(indice++);
                if (atual == '"') {
                    return sb.toString();
                }
                if (atual == '\\') {
                    if (indice >= texto.length()) {
                        throw erro("Sequencia de escape invalida.");
                    }
                    char escaped = texto.charAt(indice++);
                    switch (escaped) {
                        case '\\':
                        case '"':
                        case '/':
                            sb.append(escaped);
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        default:
                            throw erro("Escape JSON nao suportado: \\" + escaped);
                    }
                } else {
                    sb.append(atual);
                }
            }
            throw erro("String JSON nao finalizada.");
        }

        private void expectLiteral(String literal) throws PersistenciaException {
            for (int i = 0; i < literal.length(); i++) {
                if (indice + i >= texto.length() || texto.charAt(indice + i) != literal.charAt(i)) {
                    throw erro("Literal JSON invalido.");
                }
            }
            indice += literal.length();
        }

        private void expect(char esperado) throws PersistenciaException {
            skipWhitespace();
            if (indice >= texto.length() || texto.charAt(indice) != esperado) {
                throw erro("Esperado '" + esperado + "'.");
            }
            indice++;
        }

        private boolean peek(char esperado) {
            skipWhitespace();
            return indice < texto.length() && texto.charAt(indice) == esperado;
        }

        private void skipWhitespace() {
            while (indice < texto.length() && Character.isWhitespace(texto.charAt(indice))) {
                indice++;
            }
        }

        private PersistenciaException erro(String mensagem) {
            return new PersistenciaException(mensagem + " Perto da posicao " + indice + ".", null);
        }
    }
}