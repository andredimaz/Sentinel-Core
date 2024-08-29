package github.andredimaz.plugin.core.utils.basics;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtils {
    public static ItemStack getSkull(String url) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if (url.isEmpty()) {
            return item;
        } else {
            SkullMeta itemMeta = (SkullMeta)item.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
            byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;

            try {
                profileField = itemMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(itemMeta, profile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException var7) {
                var7.printStackTrace();
            }

            item.setItemMeta(itemMeta);
            return item;
        }
    }

    public static ItemStack getSkull(ItemStack skull, String url) {
        if (url != null && !url.isEmpty()) {
            SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
            byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;

            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
            } catch (SecurityException | NoSuchFieldException var10) {
                Exception ex = null;
                ex.printStackTrace();
            }

            profileField.setAccessible(true);

            try {
                profileField.set(skullMeta, profile);
            } catch (IllegalAccessException | IllegalArgumentException var9) {
                Exception ex2 = null;
                ex2.printStackTrace();
            }

            skull.setItemMeta(skullMeta);
            return skull;
        } else {
            return skull;
        }
    }
}
