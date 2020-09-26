package de.coolepizza.bingo.commands;

import de.coolepizza.bingo.manager.BingoSettings;
import de.coolepizza.bingo.utils.ItemSelector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class ItemsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;

            Inventory inv = Bukkit.createInventory(null, 9 * 5);

            for (Material item : ItemSelector.getItems(BingoSettings.BingoDifficulty.HARD, 9)) {
                inv.addItem(new ItemStack(item));
            }
            p.openInventory(inv);
        }

        return true;
    }
}
