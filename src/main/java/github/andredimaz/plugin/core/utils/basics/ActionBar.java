package github.andredimaz.plugin.core.utils.basics;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ActionBar {

    public static void send(Player p, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoc);
    }

    public static void send(ArrayList<Player> players, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);

        for(Player p : players) {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoc);
        }

    }
}
