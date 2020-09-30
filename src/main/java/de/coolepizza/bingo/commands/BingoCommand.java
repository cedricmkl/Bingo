package de.coolepizza.bingo.commands;

import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.manager.BingoManager;
import de.coolepizza.bingo.team.Team;
import de.coolepizza.bingo.utils.ItemBuilder;
import de.coolepizza.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class BingoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            return false;
        }
        if (Bingo.getBingoManager().bingoState == BingoManager.BingoState.INGAME) {
            Player player = (Player) commandSender;
            Team team = Bingo.getBingoManager().getTeamManager().getTeamFromPlayer(player);
            Inventory inventory = Bukkit.createInventory(null, 9, "§9Bingo Items");

            if (Bingo.getBingoManager().getBingosettings().getItems() > 9) {
                if (!(Bingo.getBingoManager().getBingosettings().getItems() >= 18)) {
                    inventory = Bukkit.createInventory(null, 18, "§9Bingo Items");
                } else {
                    inventory = Bukkit.createInventory(null, 27, "§9Bingo Items");
                }
            }
                ArrayList<Material> teamitems = Bingo.getBingoManager().getItemManager().getItemsFromTeam(team.getTeamid());
                for (Material material : Bingo.getBingoManager().getItemManager().getNeeded()) {
                    if (teamitems != null){
                        if (!teamitems.contains(material)){
                            inventory.addItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayname("§9"+  Utils.getItemName(material)).setLore("§aDein Team hat das Item Bereits gefunden!").build());
                        }else {
                            inventory.addItem(new ItemBuilder(material).setDisplayname("§9"+  Utils.getItemName(material)).setLore("§aFinde dieses Item!").build());
                        }
                    }else {
                        inventory.addItem(new ItemBuilder(material).setDisplayname("§9"+  Utils.getItemName(material)).setLore("§aFinde dieses Item!").build());
                    }
                }

            player.openInventory(inventory);
            } else {
                commandSender.sendMessage("§cDieser Command ist nur während des Spiels freigeschaltet!");
            }
        return false;
    }
}
