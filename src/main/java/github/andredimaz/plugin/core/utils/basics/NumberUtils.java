package github.andredimaz.plugin.core.utils.basics;

import java.util.LinkedHashMap;
import java.util.Map;

public class NumberUtils {

    // Mapeamento de numerais romanos para valores inteiros
    private static final LinkedHashMap<String, Integer> romanNumerals = new LinkedHashMap<>();

    static {
        // Inicialização do mapa de numerais romanos
        romanNumerals.put("M", 1000);
        romanNumerals.put("CM", 900);
        romanNumerals.put("D", 500);
        romanNumerals.put("CD", 400);
        romanNumerals.put("C", 100);
        romanNumerals.put("XC", 90);
        romanNumerals.put("L", 50);
        romanNumerals.put("XL", 40);
        romanNumerals.put("X", 10);
        romanNumerals.put("IX", 9);
        romanNumerals.put("V", 5);
        romanNumerals.put("IV", 4);
        romanNumerals.put("I", 1);
    }

    /**
     * Converte um número inteiro para seu equivalente em numeral romano.
     *
     * @param numb O número a ser convertido.
     * @return O numeral romano correspondente.
     */
    public static String convertToNumeralRoman(int numb) {
        StringBuilder res = new StringBuilder();

        // Itera sobre o mapa de numerais romanos para construir a string de resultado
        for (Map.Entry<String, Integer> entry : romanNumerals.entrySet()) {
            int matches = numb / entry.getValue();
            res.append(repeat(entry.getKey(), matches));  // Adiciona os caracteres repetidos
            numb %= entry.getValue();  // Atualiza o número restante
        }

        return res.toString();
    }

    /**
     * Repete uma string um número específico de vezes.
     *
     * @param s A string a ser repetida.
     * @param n O número de repetições.
     * @return A string repetida.
     */
    private static String repeat(String s, int n) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append(s);
        }

        return sb.toString();
    }

    /**
     * Formata um tempo dado em segundos para uma string no formato "HHh MMm SSs".
     *
     * @param timeInSeconds O tempo em segundos.
     * @return O tempo formatado.
     */
    public static String formatSeconds(int timeInSeconds) {
        int secondsLeft = timeInSeconds % 3600 % 60;
        int minutes = (int) Math.floor((double) (timeInSeconds % 3600) / 60);
        int hours = (int) Math.floor((double) (timeInSeconds / 3600));

        String HH = hours < 10 ? "0" + hours : "" + hours;
        String MM = minutes < 10 ? "0" + minutes : "" + minutes;
        String SS = secondsLeft < 10 ? "0" + secondsLeft : "" + secondsLeft;

        if (hours > 0) {
            return HH + "h " + MM + "m " + SS + "s";
        } else if (minutes > 0) {
            return MM + "m " + SS + "s";
        } else {
            return SS + "s";
        }
    }
}
