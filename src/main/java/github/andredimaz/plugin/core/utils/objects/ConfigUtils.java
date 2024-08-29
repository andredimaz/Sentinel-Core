package github.andredimaz.plugin.core.utils.objects;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigUtils {
    private static HashMap files = new HashMap();
    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;

    public ConfigUtils(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        if (files.containsKey(plugin.getName() + ":" + name)) {
            this.file = ((ConfigUtils)files.get(plugin.getName() + ":" + name)).file;
        } else {
            this.file = new File(plugin.getDataFolder(), name);
            files.put(plugin.getName() + ":" + name, this);
        }

        this.reloadConfig();
    }

    public ConfigUtils(JavaPlugin plugin, String name, String folder) {
        this.plugin = plugin;
        if (files.containsKey(plugin.getName() + ":" + folder + "/" + name)) {
            this.file = ((ConfigUtils)files.get(plugin.getName() + ":" + folder + "/" + name)).file;
        } else {
            File parent = new File(plugin.getDataFolder().toString() + "/" + folder);
            if (!parent.exists()) {
                parent.mkdirs();
            }

            this.file = new File(parent, name);
            files.put(plugin.getName() + ":" + folder + "/" + name, this);
        }

        this.reloadConfig();
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public File getFile() {
        return this.file;
    }

    public boolean exists() {
        return this.getFile().exists();
    }

    public void delete() {
        this.getFile().delete();
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        InputStream imputStream = this.plugin.getResource(this.file.getName());
        if (imputStream != null) {
            this.saveDefaultConfig();
        }

    }

    public void saveConfig() {
        try {
            this.getConfig().save(this.file);
        } catch (IOException var2) {
        }

    }

    public void saveDefault() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    public void saveDefaultConfig() {
        if (!this.file.exists()) {
            this.plugin.saveResource(this.file.getName(), true);
        }

    }

    public ConfigurationSection getConfigurationSection(String arg0) {
        return this.config.getConfigurationSection(arg0);
    }

    public void set(String arg0, Object arg1) {
        this.config.set(arg0, arg1);
    }

    public Object get(String arg0) {
        return this.config.get(arg0);
    }

    public boolean isNull(String arg0) {
        return this.get(arg0) == null;
    }

    public String getString(String arg0) {
        return this.config.getString(arg0);
    }

    public Boolean getBoolean(String arg0) {
        return this.config.getBoolean(arg0);
    }

    public int getInt(String arg0) {
        return this.config.getInt(arg0);
    }

    public double getDouble(String arg0) {
        return this.config.getDouble(arg0);
    }

    public List getStringList(String arg0) {
        return this.config.getStringList(arg0);
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }
}
