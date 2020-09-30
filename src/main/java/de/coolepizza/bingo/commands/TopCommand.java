package de.coolepizza.bingo.commands;

import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.manager.BingoManager;
import de.coolepizza.bingo.team.Team;
import de.coolepizza.bingo.utils.ItemBuilder;
import de.coolepizza.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class TopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            return false;
        }
        if (Bingo.getBingoManager().bingoState == BingoManager.BingoState.INGAME) {
            Player player = (Player) commandSender;
            if (player.getWorld().getName().equals("world")) {
                int x = (int) player.getLocation().getX();
                int z = (int) player.getLocation().getZ();
                int y = Bukkit.getWorld("world").getHighestBlockYAt(x , z);

                player.teleport(new Location(Bukkit.getWorld("world") , x ,y+2 ,z));
                player.playSound(player.getLocation() , Sound.ENTITY_ENDERMAN_TELEPORT , 1, 1);
            }else {
            int x = (int) Bukkit.getWorld("world").getSpawnLocation().getX();
            int z = (int) Bukkit.getWorld("world").getSpawnLocation().getZ();
            int y = Bukkit.getWorld("world").getHighestBlockYAt(x, z);
            player.teleport(new Location(Bukkit.getWorld("world"), x, y + 1, z));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        }
            } else {
                commandSender.sendMessage("§cDieser Command ist nur während des Spiels freigeschaltet!");
            }
        return false;
    }
}
