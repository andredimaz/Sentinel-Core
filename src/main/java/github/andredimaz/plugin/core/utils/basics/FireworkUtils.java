package github.andredimaz.plugin.core.utils.basics;

import java.util.Random;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkUtils {
    public static void spawnFirework(Location loc) {
        Firework fw = (Firework)loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        Random r = new Random();
        int rt = r.nextInt(3) + 1;
        Type type = Type.BALL;
        if (rt == 1) {
            type = Type.BALL;
        } else if (rt == 2) {
            type = Type.BALL_LARGE;
        } else if (rt == 3) {
            type = Type.BURST;
        } else if (rt == 4) {
            type = Type.CREEPER;
        } else if (rt == 5) {
            type = Type.STAR;
        }

        Color c1 = getColor(r.nextInt(17) + 1);
        Color c2 = getColor(r.nextInt(17) + 1);
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
        fwm.addEffect(effect);
        int rp = r.nextInt(1);
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);
    }

    private static Color getColor(int i) {
        if (i == 1) {
            return Color.AQUA;
        } else if (i == 2) {
            return Color.BLACK;
        } else if (i == 3) {
            return Color.BLUE;
        } else if (i == 4) {
            return Color.FUCHSIA;
        } else if (i == 5) {
            return Color.GRAY;
        } else if (i == 6) {
            return Color.GREEN;
        } else if (i == 7) {
            return Color.LIME;
        } else if (i == 8) {
            return Color.MAROON;
        } else if (i == 9) {
            return Color.NAVY;
        } else if (i == 10) {
            return Color.OLIVE;
        } else if (i == 11) {
            return Color.ORANGE;
        } else if (i == 12) {
            return Color.PURPLE;
        } else if (i == 13) {
            return Color.RED;
        } else if (i == 14) {
            return Color.SILVER;
        } else if (i == 15) {
            return Color.TEAL;
        } else if (i == 16) {
            return Color.WHITE;
        } else {
            return i == 17 ? Color.YELLOW : null;
        }
    }
}
