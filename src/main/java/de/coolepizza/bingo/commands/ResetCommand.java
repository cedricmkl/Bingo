package de.coolepizza.bingo.commands;

import de.coolepizza.bingo.Bingo;
import net.minecraft.server.v1_16_R2.World;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

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
