package github.andredimaz.plugin.core.utils.basics;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PacketAS {

    private JavaPlugin plugin;
    private final Map<Integer, Location> armorStands = new HashMap<>();

    public PacketAS(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void spawn(Player player, Location location, org.bukkit.inventory.ItemStack head, boolean rotateAnimation, double speed, boolean floatAnimation) {
        EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle());
        armorStand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setSmall(false);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

        addHelmet(player, armorStand, head);

        // Track this packet-based ArmorStand
        armorStands.put(armorStand.getId(), location);

        if (rotateAnimation) {
            RotateArmorStand(armorStand, speed);
        }
        if (floatAnimation) {
            FloatArmorStand(armorStand);
        }
    }

    public void spawnAll(Location location, org.bukkit.inventory.ItemStack head, boolean rotateAnimation, double speed, boolean floatAnimation) {
        EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle());
        armorStand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setSmall(false);

        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(armorStand);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnPacket);
        }

        addHelmet(armorStand, head);

        // Track this packet-based ArmorStand
        armorStands.put(armorStand.getId(), location);

        if (rotateAnimation) {
            RotateArmorStand(armorStand, speed);
        }
        if (floatAnimation) {
            FloatArmorStand(armorStand);
        }
    }

    public boolean isPacketArmorStand(int entityId) {
        return armorStands.containsKey(entityId);
    }

    public void removePacketArmorStand(int entityId) {
        armorStands.remove(entityId);
    }

    private void addHelmet(Player player, EntityArmorStand armorStand, org.bukkit.inventory.ItemStack head) {
        ItemStack nmsHelmet = CraftItemStack.asNMSCopy(head);
        PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(
                armorStand.getId(), 4, nmsHelmet);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(equipmentPacket);
    }

    private void addHelmet(EntityArmorStand armorStand, org.bukkit.inventory.ItemStack helmet) {
        ItemStack nmsHelmet = CraftItemStack.asNMSCopy(helmet);
        PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(
                armorStand.getId(), 4, nmsHelmet);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(equipmentPacket);
        }
    }

    private void RotateArmorStand(EntityArmorStand armorStand, double speed) {
        new BukkitRunnable() {
            private float yaw = 0.0f;

            @Override
            public void run() {
                if (!armorStand.isAlive()) {
                    this.cancel();
                    return;
                }

                yaw += speed;
                if (yaw >= 360.0f) {
                    yaw -= 360.0f;
                }

                armorStand.yaw = yaw;

                PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(
                        armorStand.getId(),
                        MathHelper.floor(armorStand.locX * 32.0D),
                        MathHelper.floor(armorStand.locY * 32.0D),
                        MathHelper.floor(armorStand.locZ * 32.0D),
                        (byte) ((yaw * 256.0F) / 360.0F),
                        (byte) ((armorStand.pitch * 256.0F) / 360.0F),
                        false
                );

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(teleportPacket);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void FloatArmorStand(EntityArmorStand armorStand) {
        new BukkitRunnable() {
            private double t = 0;  // Time counter
            private final double initialY = armorStand.locY;  // Initial Y position
            private final double amplitude = 0.3;  // Amplitude for the floating motion

            @Override
            public void run() {
                if (!armorStand.isAlive()) {
                    this.cancel();
                    return;
                }

                double yOffset = Math.sin(t) * amplitude;

                t += 0.15;

                armorStand.setPosition(armorStand.locX, initialY + yOffset, armorStand.locZ);

                PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(
                        armorStand.getId(), armorStand.getDataWatcher(), true);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(metadataPacket);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);  // Run every tick (1L = 1 tick)
    }
}
