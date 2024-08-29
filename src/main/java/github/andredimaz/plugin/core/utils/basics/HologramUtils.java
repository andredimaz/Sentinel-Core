package github.andredimaz.plugin.core.utils.basics;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HologramUtils {

    /**
     * Creates a hologram at the specified location with the given text.
     *
     * @param player The player who will see the hologram.
     * @param loc The location where the hologram will appear.
     * @param text The text to display in the hologram.
     * @return The entity ID of the armor stand (hologram), used for removal.
     */
    public static int createHologram(Player player, Location loc, String text) {
        // Convert Bukkit world to NMS world
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();

        // Create the armor stand
        EntityArmorStand armorStand = new EntityArmorStand(world);
        armorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        armorStand.setCustomName(String.valueOf(new ChatComponentText(text)));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);  // Prevents the armor stand from falling

        // Send spawn packet to the player
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

        // Set armor stand metadata (invisible, custom name visible)
        DataWatcher watcher = armorStand.getDataWatcher();
        watcher.watch(10, (byte) 0x20); // Invisible flag

        PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(armorStand.getId(), watcher, true);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(metaPacket);

        // Return the entity ID for future reference (e.g., removal)
        return armorStand.getId();
    }

    /**
     * Removes a hologram by destroying the entity with the given ID.
     *
     * @param player The player who will no longer see the hologram.
     * @param entityId The entity ID of the hologram to remove.
     */
    public static void removeHologram(Player player, int entityId) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}

