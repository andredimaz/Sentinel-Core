package github.andredimaz.plugin.core.utils.objects;

import github.andredimaz.plugin.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerFreezer implements Listener {
    private Player p;
    private Location location;
    private boolean freezed;

    public PlayerFreezer(Player p) {
        this.p = p;
    }

    public void freeze() {
        if (!this.freezed) {
            Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
            this.freezed = true;
            this.location = this.p.getLocation();
            this.p.setWalkSpeed(0.0F);
        }
    }

    public void unfreeze() {
        if (this.freezed) {
            this.p.setWalkSpeed(0.2F);
            this.disable();
        }
    }

    private void disable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p == this.p) {
            if (this.freezed) {
                if (e.getTo().getBlockY() != e.getFrom().getBlockY()) {
                    p.teleport(this.location);
                }
            }
        }
    }
}
