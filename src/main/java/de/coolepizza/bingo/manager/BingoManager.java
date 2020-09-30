package de.coolepizza.bingo.manager;

import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.team.Team;
import de.coolepizza.bingo.team.TeamManager;
import de.coolepizza.bingo.utils.Cuboid;
import de.coolepizza.bingo.utils.ItemBuilder;
import de.coolepizza.bingo.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BingoManager {
    public BingoState bingoState;
    private BingoSettings bingosettings;
    private Cuboid bedrock;
    private TeamManager teamManager;
    private ItemManager itemManager;

    public BingoManager(){
        bingoState = BingoState.SETTINGS;
        bingosettings = new BingoSettings(1 , 9 , BingoSettings.BingoDifficulty.NORMAl);

        teamManager = new TeamManager();
        itemManager = new ItemManager();
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                if (Bingo.getInstance().wasReset()){
                    World w =  Bukkit.getWorld("world");
                    int spawnx = (int) w.getSpawnLocation().getX();
                    int spawnz = (int) w.getSpawnLocation().getZ();

                    int y = w.getHighestBlockYAt(spawnx , spawnz)+4;
                    Cuboid bedrock1 = new Cuboid(new Location(w, spawnx , y , spawnz) , new Location(w, spawnx+10 , y+4 , spawnz+10));
                    Cuboid bedrock2 = new Cuboid(new Location(w, spawnx+1 , y+1 , spawnz+1) , new Location(w, spawnx+9 , y+4 , spawnz+9));

                    bedrock = bedrock1;
                    for (Block block : bedrock1.getBlocks()) {
                        block.setType(Material.BEDROCK);
                    }
                    for (Block block : bedrock2.getBlocks()) {
                        block.setType(Material.AIR );
                    }
                    w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE , false);
                    w.setGameRule(GameRule.DO_WEATHER_CYCLE , false);
                }
            }
        };
        r.runTaskLater(Bingo.getInstance() , 20);
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public enum BingoState{
        SETTINGS,
        TEAM_JOIN,
        INGAME;
    }
    public void startTeamState(){
        bingoState = BingoState.TEAM_JOIN;
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.closeInventory();
            player.sendMessage("§aWähle dein Team aus!");
            Bingo.getTimer().information = "§9Wähle dein Team aus!";
            player.sendTitle("§aWähle dein Team aus!" , "§aKlicke auf das Bett!");
            player.playSound(player.getLocation() , Sound.ENTITY_PLAYER_LEVELUP , 1 ,1);
            player.getInventory().clear();
            player.getInventory().addItem(new ItemBuilder(Material.WHITE_BED).setDisplayname("§9Teamauswahl").build());
            if (player.hasPermission("orav.admin")){
                player.getInventory().setItem(8 , new ItemBuilder(Material.LIME_DYE).setDisplayname("§aRunde starten").build());
            }
        });
    }

    public void startIngameState(){
        bingoState = BingoState.INGAME;
        itemManager.start();
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.closeInventory();
            player.playSound(player.getLocation() , Sound.ENTITY_PLAYER_LEVELUP , 1 ,1);
            player.getInventory().clear();
            player.sendMessage(Bingo.prefix + "Die Runde startet jetzt, versuche alle Items zu bekommen! !");
        });
        getBedrock().getBlocks().forEach(block -> {
            block.setType(Material.AIR);
        });
        for (UUID uuid : Bingo.getBingoManager().getTeamManager().getPlayersInTeam(Team.SPECTATOR)) {
            Bukkit.getPlayer(uuid).setGameMode(GameMode.SPECTATOR);
        }
        Bingo.getTimer().paused = false;
    }
    public void win(Team team){
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle("§aTeam " + team.getTeamid() , "hat Bingo gewonnen");
            player.playSound(player.getLocation() , Sound.UI_TOAST_CHALLENGE_COMPLETE ,1 ,1);
            player.sendMessage("§7-------------------------------------");
            player.sendMessage("§lTeam " + team.getTeamid()  + " hat Bingo gewonnen!");
            player.sendMessage("§lZeit benötigt §8- §7" + Utils.shortInteger(Bingo.getTimer().time));
            player.sendMessage("§7-------------------------------------");
        });
        Bingo.getTimer().paused = true;
        Bingo.getTimer().information = "§9Team " + team.getTeamid() + " hat Bingo gewonnen!";
    }

    public BingoSettings getBingosettings() {
        return bingosettings;
    }

    public Cuboid getBedrock() {
        return bedrock;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }
}
