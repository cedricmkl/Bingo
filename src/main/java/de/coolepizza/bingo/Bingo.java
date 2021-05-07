package de.coolepizza.bingo;

import de.coolepizza.bingo.commands.BingoCommand;
import de.coolepizza.bingo.commands.ResetCommand;
import de.coolepizza.bingo.commands.TeamChatCommand;
import de.coolepizza.bingo.commands.TopCommand;
import de.coolepizza.bingo.events.Listeners;
import de.coolepizza.bingo.manager.BingoManager;
import de.coolepizza.bingo.utils.MetricsLite;
import de.coolepizza.bingo.utils.ScoreboardUtils;
import de.coolepizza.bingo.utils.Timer;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.Metrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public final class Bingo extends JavaPlugin {
    public final int BSTATS_PLUGIN_ID = 8931;
    public static final int SPIGOT_PLUGIN_ID = 84348;

    private static Bingo instance;
    private static Timer timer;
    private static BingoManager bingoManager;
    public static final String prefix = "§7[§aBINGO§7] §a";
    private boolean wasreset;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        if (getConfig().getBoolean("reset")) {
            File propertiesFile = new File(Bukkit.getWorldContainer(), "server.properties");
            wasreset = true;
            try (FileInputStream stream = new FileInputStream(propertiesFile)) {
                Properties properties = new Properties();
                properties.load(stream);

                // Getting and deleting the main world
                File world = new File(Bukkit.getWorldContainer(), properties.getProperty("level-name"));
                FileUtils.deleteDirectory(world);

                File nether = new File(Bukkit.getWorldContainer(), "world_nether");
                FileUtils.deleteDirectory(nether);
                // Creating needed directories
                world.mkdirs();
                new File(world, "data").mkdirs();
                new File(world, "datapacks").mkdirs();
                new File(world, "playerdata").mkdirs();
                new File(world, "poi").mkdirs();
                new File(world, "region").mkdirs();

                new File(nether, "data").mkdirs();
                new File(nether, "datapacks").mkdirs();
                new File(nether, "playerdata").mkdirs();
                new File(nether, "poi").mkdirs();
                new File(nether, "region").mkdirs();
            } catch (IOException ignored) {
            }
            getConfig().set("reset" , false);
            saveConfig();
        }
    }
    @Override
    public void onEnable() {
        MetricsLite metricsLite = new MetricsLite(this, BSTATS_PLUGIN_ID);
        instance = this;
        timer = new Timer();
        bingoManager = new BingoManager();

        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        getCommand("reset").setExecutor(new ResetCommand());
        getCommand("bingo").setExecutor(new BingoCommand());
        getCommand("top").setExecutor(new TopCommand());
        getCommand("teamchat").setExecutor(new TeamChatCommand());

        ScoreboardUtils.insert(15 , "§c");
        ScoreboardUtils.insert(14 , "§9Deine Platzierung: §7N/A");
        ScoreboardUtils.insert(13 , "§9Noch §7N/A §9Items");
        ScoreboardUtils.insert(12 , "§9");
        ScoreboardUtils.insert(11 , "§9Items:");
        ScoreboardUtils.insert(10 , "§a");
        ScoreboardUtils.insert(9 , "§a");
        ScoreboardUtils.insert(8 , "§a");
        ScoreboardUtils.insert(7 , "§a");
        ScoreboardUtils.insert(6 , "§a");
        ScoreboardUtils.insert(5 , "§a");
        ScoreboardUtils.insert(4 , "§b");
        ScoreboardUtils.insert(3 , "§9Dein Team: §7Kein Team");
        ScoreboardUtils.insert(2 , "§a");
        ScoreboardUtils.insert(1 , "§9Bingo by CoolePizza");

        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.SPAWN_RADIUS , 0);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS , false);
        });
    }

    @Override
    public void onDisable() {
    }

    public static Bingo getInstance() {
        return instance;
    }

    public static Timer getTimer() {
        return timer;
    }


    public static BingoManager getBingoManager() {
        return bingoManager;
    }

    public boolean wasReset() {
        return wasreset;
    }
}
