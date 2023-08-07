package com.mefrreex.mines.command.subcommand;

import cn.nukkit.command.CommandSender;

import com.mefrreex.mines.command.BaseCommand;
import com.mefrreex.mines.command.BaseSubCommand;
import com.mefrreex.mines.utils.Language;

public class HelpSubCommand extends BaseSubCommand {

    public HelpSubCommand() {
        super("help", "Help");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String label, String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append(Language.get("subcommand-help-available"));
        for (BaseSubCommand sub : BaseCommand.getSubcommands().values()) {
            if (sub.testPermission(sender)) {
                sb.append("\n- /" + commandLabel + " " + sub.getName() + " - " + sub.getDescription());
            }
        }
        sender.sendMessage(sb.toString());
        return true;
    }
}
