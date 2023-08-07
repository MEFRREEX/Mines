package com.mefrreex.mines.command.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.command.BaseSubCommand;
import com.mefrreex.mines.utils.Language;
import com.mefrreex.mines.utils.Point;

public class SecondPosSubCommand extends BaseSubCommand {

    public SecondPosSubCommand() {
        super("pos2", "Set the points to create the mine");
        this.setPermission(Mines.PERMISSION_ADMIN);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String label, String[] args) {
        if (!testPermission(sender)) {
            sender.sendMessage(Mines.PREFIX_RED + Language.get("command-no-permission"));
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Mines.PREFIX_RED + Language.get("command-in-game"));
            return false;
        }
        Player player = (Player) sender;
        Mines.getSecondPoints().put(player, new Point(player));
        player.sendMessage(Mines.PREFIX_GREEN + Language.get("subcommand-setpos-pos2"));
        return true;
    } 
}
