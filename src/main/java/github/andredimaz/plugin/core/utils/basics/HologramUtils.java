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

    public static int[] createMultiLineHologram(Player player, Location loc, String[] lines) {
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        int[] entityIds = new int[lines.length];  // Array para armazenar os IDs das entidades

        for (int i = 0; i < lines.length; i++) {
            EntityArmorStand armorStand = new EntityArmorStand(world);
            // Ajuste a altura para cada linha
            armorStand.setLocation(loc.getX(), loc.getY() - (i * 0.3), loc.getZ(), 0.0F, 0.0F);
            armorStand.setCustomName(lines[i]);  // Define o nome para cada linha
            armorStand.setCustomNameVisible(true);
            armorStand.setInvisible(true);
            armorStand.setGravity(false);

            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

            DataWatcher watcher = armorStand.getDataWatcher();
            watcher.watch(10, (byte) 32);  // Nome sempre visível
            PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(armorStand.getId(), watcher, true);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(metaPacket);

            entityIds[i] = armorStand.getId();  // Armazena o ID da entidade
        }

        return entityIds;  // Retorna os IDs das entidades
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

    public static void removeMultiLineHologram(Player player, int[] entityIds) {
        for (int entityId : entityIds) {
            removeHologram(player, entityId);
        }
    }

    public static void updateHologramLine(Player player, int entityId, String newText) {
        // Cria o DataWatcher com o novo texto
        DataWatcher dataWatcher = new DataWatcher(null);
        dataWatcher.a(2, newText); // 2 é o índice do nome da entidade (pode variar conforme a versão do Minecraft)
        dataWatcher.a(3, (byte) 1); // Define a visibilidade do nome da entidade (1 = visível)

        // Cria o pacote para atualizar os dados da entidade
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(entityId, dataWatcher, true);

        // Envia o pacote para o jogador
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(metadataPacket);
    }

}

