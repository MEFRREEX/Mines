package com.mefrreex.mines.command.subcommand;

import cn.nukkit.command.CommandSender;
import com.mefrreex.mines.command.BaseCommand;
import com.mefrreex.mines.command.BaseSubCommand;
import com.mefrreex.mines.utils.Language;

public class HelpSubCommand extends BaseSubCommand {

    public HelpSubCommand(BaseCommand command) {
        super("help", "Help", command);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String label, String[] args) {
        StringBuilder builder = new StringBuilder(Language.get("subcommand-help-available"));

        for (BaseSubCommand sub : this.getCommand().getSubcommands().values()) {
            if (sub.testPermission(sender)) {
                String description = Language.get("subcommand-" + sub.getName() + "-description");
                builder.append("\n- /").append(commandLabel).append(" ").append(sub.getName()).append(" - ").append(description);
            }
        }

        sender.sendMessage(builder.toString());
        return true;
    }
}
