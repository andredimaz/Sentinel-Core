package github.andredimaz.plugin.core.utils.abstracts;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommandUtils implements CommandExecutor, Listener {
    private JavaPlugin plugin;
    private ArrayList<SubCommandUtils> mageSubCommands = new ArrayList<>();

    public CommandUtils(JavaPlugin plugin, String... commands) {
        this.plugin = plugin;
        for (String command : commands) {
            if (plugin.getCommand(command) != null) {
                plugin.getCommand(command).setExecutor(this);
            } else {
                plugin.getLogger().severe("Comando " + command + " não encontrado no plugin.yml");
            }
        }
    }

    public CommandUtils(JavaPlugin plugin, String command, boolean listener) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getCommand(command).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean execute = false;

        for(SubCommandUtils sub : this.mageSubCommands) {
            if (label.equalsIgnoreCase(sub.getSubCommand()) || cmd.getName().equalsIgnoreCase(sub.getSubCommand())) {
                sub.onExecute(sender, cmd, label, args);
                execute = true;
            }
        }

        if (!execute) {
            this.onExecute(sender, cmd, label, args);
        }

        return false;
    }

    public abstract void onExecute(CommandSender var1, Command var2, String var3, String[] var4);

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public ArrayList getSubCommands() {
        return this.mageSubCommands;
    }
}
