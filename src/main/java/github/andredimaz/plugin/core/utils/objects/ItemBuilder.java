package github.andredimaz.plugin.core.utils.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import github.andredimaz.plugin.core.utils.basics.SkullUtils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
    }

    public ItemBuilder(String textureCode) {
        this.item = SkullUtils.getSkull("http://textures.minecraft.net/texture/" + textureCode);
    }

    public ItemBuilder(Material material, int data) {
        this.item = new ItemStack(material, 1, (short)data);
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material, 1, (short)0);
    }

    public ItemBuilder(int material, int data) {
        this.item = new ItemStack(material, 1, (short)data);
    }

    public ItemBuilder(int material) {
        this.item = new ItemStack(material, 1, (short)0);
    }

    public ItemBuilder setType(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        this.item.setDurability((short)durability);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        this.item.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List lore) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(lore);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeLoreLine(String linha) {
        ItemMeta im = this.item.getItemMeta();
        List lore = new ArrayList(im.getLore());
        if (!lore.contains(linha)) {
            return this;
        } else {
            lore.remove(linha);
            im.setLore(lore);
            this.item.setItemMeta(im);
            return this;
        }
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta im = this.item.getItemMeta();
        List lore = new ArrayList(im.getLore());
        if (index >= 0 && index <= lore.size()) {
            lore.remove(index);
            im.setLore(lore);
            this.item.setItemMeta(im);
            return this;
        } else {
            return this;
        }
    }

    public ItemBuilder addLoreLine(String linha) {
        ItemMeta im = this.item.getItemMeta();
        List lore = new ArrayList();
        if (im.hasLore()) {
            lore = new ArrayList(im.getLore());
        }

        lore.add(linha);
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String linha, int pos) {
        ItemMeta im = this.item.getItemMeta();
        List lore = new ArrayList(im.getLore());
        lore.set(pos, linha);
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLeatherColor(Color color) {
        if (!(this.item.getItemMeta() instanceof LeatherArmorMeta)) {
            return this;
        } else {
            LeatherArmorMeta meta = (LeatherArmorMeta)this.item.getItemMeta();
            meta.setColor(color);
            this.item.setItemMeta(meta);
            return this;
        }
    }

    public ItemBuilder setOwner(String owner) {
        if (!(this.item.getItemMeta() instanceof SkullMeta)) {
            return this;
        } else {
            SkullMeta meta = (SkullMeta)this.item.getItemMeta();
            meta.setOwner(owner);
            this.item.setItemMeta(meta);
            return this;
        }
    }

    public ItemBuilder setInfinityDurability() {
        this.item.setDurability((short)32767);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag) {
        ItemMeta im = this.item.getItemMeta();
        im.addItemFlags(new ItemFlag[]{flag});
        this.item.setItemMeta(im);
        return this;
    }

    public List getLore() {
        return this.item.getItemMeta().getLore();
    }

    public ItemBuilder addTexture(String url) {
        if (url != null && url.length() != 0) {
            this.addNBT("itembuilder.skull_url", url);
            this.item = SkullUtils.getSkull(this.item, url);
            return this;
        } else {
            return this;
        }
    }

    public String getTexture() {
        return this.getNBT("itembuilder.skull_url");
    }

    public ItemBuilder repair() {
        return this.addTexture(this.getTexture());
    }

    public ItemBuilder addNBT(String tag, String value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsIS = CraftItemStack.asNMSCopy(this.item);
        NBTTagCompound com = nmsIS.getTag();
        if (com == null) {
            com = new NBTTagCompound();
        }

        com.setString(tag, value);
        nmsIS.setTag(com);
        this.item = CraftItemStack.asBukkitCopy(nmsIS);
        String info = this.getNBT("itembuilder.skull_url");
        if (info != null) {
            this.item = SkullUtils.getSkull(this.item, info);
        }

        return this;
    }

    public ItemBuilder addNBT(String tag, int value) {
        return this.addNBT(tag, "" + value);
    }

    public String getNBT(String tag) {
        try {
            NBTTagCompound com = CraftItemStack.asNMSCopy(this.item).getTag();
            return com != null && com.hasKey(tag) ? com.getString(tag) : null;
        } catch (Exception var3) {
            return null;
        }
    }

    public ItemStack build() {
        return this.item;
    }
}

