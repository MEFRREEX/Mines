package com.mefrreex.mines.command;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.ConfigSection;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.command.subcommand.CreateSubCommand;
import com.mefrreex.mines.command.subcommand.DeleteSubCommand;
import com.mefrreex.mines.command.subcommand.EditSubCommand;
import com.mefrreex.mines.command.subcommand.FirstPosSubCommand;
import com.mefrreex.mines.command.subcommand.HelpSubCommand;
import com.mefrreex.mines.command.subcommand.ListSubCommand;
import com.mefrreex.mines.command.subcommand.SecondPosSubCommand;

public class MineCommand extends BaseCommand {

    public MineCommand(String name, Mines main) {
        this(name, main.getConfig().getSection("commands." + name));
    }

    public MineCommand(String name, ConfigSection command) {
        super(command.getString("name"), command.getString("description"));
        this.setAliases(command.getStringList("aliases").toArray(new String[]{}));

        this.registerSubCommand(new CreateSubCommand(this));
        this.registerSubCommand(new DeleteSubCommand(this));
        this.registerSubCommand(new EditSubCommand(this));
        this.registerSubCommand(new FirstPosSubCommand(this));
        this.registerSubCommand(new SecondPosSubCommand(this));
        this.registerSubCommand(new ListSubCommand(this));
        this.registerSubCommand(new HelpSubCommand(this));
    }

    @Override
    public boolean executeDefault(CommandSender sender, String label) {
        this.sendUsage(sender, "/" + label + " help");
        return true;
    }
    
    public static void register() {
        MineCommand command = new MineCommand("mine", Mines.getInstance());
        Server.getInstance().getCommandMap().register("Mines", command);
    }
}
