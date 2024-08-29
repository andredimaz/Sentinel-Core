package github.andredimaz.plugin.core;

import github.andredimaz.plugin.core.utils.basics.NumberUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main plugin;

    public void onEnable() {
        plugin = this;
        new NumberUtils();
    }

    public static Main getPlugin() {
        return plugin;
    }
}
