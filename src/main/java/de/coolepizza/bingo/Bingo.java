package de.coolepizza.bingo;

import de.coolepizza.bingo.events.Listeners;
import de.coolepizza.bingo.utils.ScoreboardUtils;
import de.coolepizza.bingo.utils.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class Bingo extends JavaPlugin {
    private static Bingo instance;
    private static Timer timer;

    @Override
    public void onEnable() {
        instance = this;
        timer = new Timer();
        saveConfig();

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        ScoreboardUtils.insert(15 , "§c");
        ScoreboardUtils.insert(14 , "§9Deine Plazierung: §7N/A");
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
        ScoreboardUtils.insert(1 , "§aBingo by CoolePizza");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Bingo getInstance() {
        return instance;
    }

    public static Timer getTimer() {
        return timer;
    }
}
