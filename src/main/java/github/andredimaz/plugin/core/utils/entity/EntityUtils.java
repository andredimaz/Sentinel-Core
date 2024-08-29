package github.andredimaz.plugin.core.utils.entity;

import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PathEntity;
import net.minecraft.server.v1_8_R3.PathfinderGoalBreakDoor;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class EntityUtils {
    public static void setAI(Entity e, boolean ai) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity)e).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);
        tag.setBoolean("NoAI", ai);
        EntityLiving el = (EntityLiving)nmsEntity;
        el.a(tag);
    }

    public static void removeAI(LivingEntity e) {
        EntityCreature c = (EntityCreature)((EntityInsentient)((CraftEntity)e).getHandle());

        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(c.goalSelector, new UnsafeList());
            bField.set(c.targetSelector, new UnsafeList());
            cField.set(c.goalSelector, new UnsafeList());
            cField.set(c.targetSelector, new UnsafeList());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static void lookAt(LivingEntity e) {
        EntityCreature c = (EntityCreature)((EntityInsentient)((CraftEntity)e).getHandle());
        c.goalSelector.a(8, new PathfinderGoalLookAtPlayer(c, EntityHuman.class, 8.0F));
        c.goalSelector.a(0, new PathfinderGoalFloat(c));
        c.goalSelector.a(1, new PathfinderGoalBreakDoor(c));
        c.goalSelector.a(7, new PathfinderGoalRandomLookaround(c));
    }

    public static void moveTo(LivingEntity e, Location moveTo, float velocity) {
        PathEntity path = ((EntityInsentient)((CraftLivingEntity)e).getHandle()).getNavigation().a(moveTo.getX(), moveTo.getY(), moveTo.getZ());
        ((EntityInsentient)((CraftLivingEntity)e).getHandle()).getNavigation().a(path, (double)velocity);
    }
}

