package de.coolepizza.bingo.commands;

import de.coolepizza.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("§9Server Reset");
        }
        Bingo.getInstance().getConfig().set("reset" , true);
        Bingo.getInstance().saveConfig();
        Bukkit.spigot().restart();
        return false;
    }

}
