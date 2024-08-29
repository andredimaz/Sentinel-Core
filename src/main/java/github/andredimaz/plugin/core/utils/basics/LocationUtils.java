package github.andredimaz.plugin.core.utils.basics;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {
    public static String convertLocationToString(Location location) {
        return location.getWorld().getName() + "|" + Double.toString(location.getX()).replace(".", ",") + "|" + Double.toString(location.getY()).replace(".", ",") + "|" + Double.toString(location.getZ()).replace(".", ",") + "|" + Double.toString((double)location.getYaw()).replace(".", ",") + "|" + Double.toString((double)location.getPitch()).replace(".", ",");
    }

    public static Location convertStringToLocation(String location) {
        String[] loc = location.split("\\|");
        return new Location(Bukkit.getWorld(loc[0]), (double)Float.parseFloat(loc[1].replace(",", ".")), (double)Float.parseFloat(loc[2].replace(",", ".")), (double)Float.parseFloat(loc[3].replace(",", ".")), Float.parseFloat(loc[4].replace(",", ".")), Float.parseFloat(loc[5].replace(",", ".")));
    }
}
