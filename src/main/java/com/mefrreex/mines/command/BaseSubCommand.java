package com.mefrreex.mines.command;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import io.netty.util.internal.EmptyArrays;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedList;

@Getter @Setter
public abstract class BaseSubCommand {
    
    private final String name;
    private final String description;

    private String[] aliases = EmptyArrays.EMPTY_STRINGS;
    protected LinkedList<CommandParameter> parameters = new LinkedList<>();

    private String permission = "";

    protected BaseSubCommand(String name) {
        this(name, name);
    }

    protected BaseSubCommand(String name, String description) {
        this.name = name.toLowerCase();
        this.description = description;
    }

    public final CommandParameter[] getCommandParameters() {
		return parameters.toArray(new CommandParameter[parameters.size()]);
	}

    public final void sendUsage(CommandSender sender, String usage) {
        sender.sendMessage(new TranslationContainer("commands.generic.usage", usage));
    }

    public final boolean testPermission(CommandSender sender) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }
        return sender.hasPermission(permission);
    }

    public abstract boolean execute(CommandSender sender, String commandLabel, String sublabel, String[] args);
}
