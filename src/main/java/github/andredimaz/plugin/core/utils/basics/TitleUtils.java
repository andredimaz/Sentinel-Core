package github.andredimaz.plugin.core.utils.basics;

import java.util.List;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleUtils {
    public static void sendTitle(Player p, String title, String sub, int in, int time, int out) {
        IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
        PacketPlayOutTitle titled = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(EnumTitleAction.TIMES, chatTitle, 20, 40, 20);
        IChatBaseComponent subt = ChatSerializer.a("{\"text\": \"" + sub + "\"}");
        PacketPlayOutTitle subp = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subt);
        PacketPlayOutTitle subl = new PacketPlayOutTitle(EnumTitleAction.TIMES, subt, in, time, out);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(titled);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(length);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(subp);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(subl);
    }

    public static void sendTitle(List players, String title, String sub, int in, int time, int out) {
        IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
        PacketPlayOutTitle titled = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(EnumTitleAction.TIMES, chatTitle, 20, 40, 20);
        IChatBaseComponent subt = ChatSerializer.a("{\"text\": \"" + sub + "\"}");
        PacketPlayOutTitle subp = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subt);
        PacketPlayOutTitle subl = new PacketPlayOutTitle(EnumTitleAction.TIMES, subt, in, time, out);

        for(Object p : players) {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(titled);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(length);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(subp);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(subl);
        }

    }
}
