package de.coolepizza.bingo.commands;

import de.coolepizza.bingo.Bingo;
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
import java.util.Map;
import java.util.Random;

public class ItemsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (Map.Entry<Integer, ArrayList<Material>> integerArrayListEntry : Bingo.getBingoManager().getItemManager().getItems().entrySet()) {
            StringBuilder stringBuilder = new StringBuilder(integerArrayListEntry.getKey()  + ": ");
            for (Material material : integerArrayListEntry.getValue()) {
                stringBuilder.append(material.name()  +" ");
            }
            commandSender.sendMessage(stringBuilder.toString());
        }
        return true;
    }
}
