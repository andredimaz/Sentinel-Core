package github.andredimaz.plugin.core.utils.abstracts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SubCommandUtils {
    private String subCommand;

    public SubCommandUtils(CommandUtils command, String subCommand) {
        this.subCommand = subCommand;

        if (command.getPlugin().getCommand(subCommand) != null) {
            command.getPlugin().getCommand(subCommand).setExecutor(command);
        } else {
            command.getPlugin().getLogger().severe("Subcomando " + subCommand + " não encontrado no plugin.yml");
        }

        command.getSubCommands().add(this);
    }

    public abstract void onExecute(CommandSender sender, Command cmd, String label, String[] args);

    public String getSubCommand() {
        return this.subCommand;
    }
}
