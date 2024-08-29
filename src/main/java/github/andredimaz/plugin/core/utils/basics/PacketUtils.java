package github.andredimaz.plugin.core.utils.basics;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PacketUtils {
    public static void removeTo(Player p, Entity e) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{((CraftEntity)e).getEntityId()});
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public static void removeTo(List<Player> players, Entity e) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{((CraftEntity)e).getEntityId()});

        for(Player p : players) {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }

    }

    public static void removeTo(List players, Entity e, Player without) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{((CraftEntity)e).getEntityId()});

        for(Object p : (List)players.stream().filter((pp) -> !pp.equals(without)).collect(Collectors.toList())) {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }

    }

    public static void sendBlockChange(Player player, Location loc, int material, byte data) {
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld)loc.getWorld()).getHandle(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        packet.block = CraftMagicNumbers.getBlock(material).fromLegacyData(data);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
