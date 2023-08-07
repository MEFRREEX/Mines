package com.mefrreex.mines.command;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.command.subcommand.*;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class BaseCommand extends Command {

    @Getter
    private static final Map<String, BaseSubCommand> subcommands = new HashMap<>();

    public BaseCommand() {
        super("mine", "Mines");
        this.setPermission(Mines.PERMISSION_ADMIN);
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
        sender.sendMessage(new TranslationContainer("commands.generic.usage", "/" + label + " help"));
        return false;
    }
    
    public void registerSubCommand(BaseSubCommand subcommand) {
		subcommands.put(subcommand.getName(), subcommand);
        for (String alias : subcommand.getAliases()) {
            subcommands.put(alias, subcommand);
        }
	}

    public void register() {
        BaseSubCommand[] subcommands = {
            new CreateSubCommand(),
            new DeleteSubCommand(),
            new EditSubCommand(),
            new ListSubCommand(),
            new FirstPosSubCommand(),
            new SecondPosSubCommand(),
            new HelpSubCommand()
        };
        this.registerSubCommands(subcommands);
        Server.getInstance().getCommandMap().register("Mines", this);
    }

    public void registerSubCommands(BaseSubCommand[] subcommands) {
        for (BaseSubCommand command : subcommands) {
            this.registerSubCommand(command);

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
    }
}
