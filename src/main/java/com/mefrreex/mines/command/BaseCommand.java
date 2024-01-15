package com.mefrreex.mines.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseCommand extends Command {

    @Getter
    private final Map<String, BaseSubCommand> subcommands = new HashMap<>();

    public BaseCommand(String name, String description) {
        super(name, description);
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            String name = args[0].toLowerCase();
            if (subcommands.containsKey(name)) {
                BaseSubCommand command = subcommands.get(name);
                return command.execute(sender, label, name, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        this.executeDefault(sender, label);
        return false;
    }

    public abstract boolean executeDefault(CommandSender sender, String label);  
    
    public final void sendUsage(CommandSender sender, String usage) {
        sender.sendMessage(new TranslationContainer("commands.generic.usage", usage));
    }

    public void addSubCommand(BaseSubCommand subcommand) {
		subcommands.put(subcommand.getName(), subcommand);
        for (String alias : subcommand.getAliases()) {
            subcommands.put(alias, subcommand);
        }
	}

    public void registerSubCommand(BaseSubCommand command) {
        this.addSubCommand(command);

        int length = command.getCommandParameters().length + 1;
        CommandParameter[] parameters = new CommandParameter[length];
        parameters[0] = CommandParameter.newEnum(command.getName(), false, new String[]{command.getName()});

        int i = 1;
        for (CommandParameter param : command.getCommandParameters()) {
            parameters[i] = param;
            i++;
        }

        this.commandParameters.put(command.getName(), parameters);
    }

    public void registerSubCommands(BaseSubCommand[] subcommands) {
        for (BaseSubCommand command : subcommands) {
            this.registerSubCommand(command);
        }
    }
}
