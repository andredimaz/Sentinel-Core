package github.andredimaz.plugin.core.utils.basics;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static ArrayList getNearbyPlayers(Location location, int radius) {
        return (ArrayList)location.getWorld().getNearbyEntities(location, (double)radius, (double)radius, (double)radius).stream().filter((e) -> e instanceof Player).map((e) -> (Player)e).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList getNearbyPlayers(Location location, int radius, Player without) {
        ArrayList players = getNearbyPlayers(location, radius);
        players.remove(without);
        return players;
    }

    public static void playSound(Location location, ArrayList<Player> players, Sound sound, float value1, float value2) {
        players.forEach(p -> p.playSound(location, sound, value1, value2));
    }

    public static UUID getFixedUUID(OfflinePlayer p) {
        return p.getUniqueId();
    }
}

