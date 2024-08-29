package github.andredimaz.plugin.core.utils.basics;


import org.bukkit.ChatColor;
import sun.plugin2.util.ColorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ColorUtils {

    /**
     * Converte códigos de cor no formato "&" para "§", permitindo o uso de cores no Minecraft.
     *
     * @param text A string que contém códigos de cor no formato "&".
     * @return A string com códigos de cor convertidos para o formato "§".
     */
    public static String colorize(String text) {
        if (text == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Converte uma lista de strings com códigos de cor no formato "&".
     *
     * @param texts A lista de strings a ser colorida.
     * @return A lista de strings coloridas.
     */
    public static List<String> colorize(List<String> texts) {
        if (texts == null) {
            return new ArrayList<>();
        }
        return texts.stream().map(ColorUtils::colorize).collect(Collectors.toList());
    }

    /**
     * Converte um array de strings com códigos de cor no formato "&".
     *
     * @param texts O array de strings a ser colorido.
     * @return Um array de strings coloridas.
     */
    public static String[] colorize(String[] texts) {
        if (texts == null) {
            return new String[]{};
        }
        return Arrays.stream(texts).map(ColorUtils::colorize).toArray(String[]::new);
    }
}

