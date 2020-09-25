package de.coolepizza.bingo.manager;

import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.team.Team;
import de.coolepizza.bingo.team.TeamManager;
import de.coolepizza.bingo.utils.Cuboid;
import de.coolepizza.bingo.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class BingoManager {
    public BingoState bingoState;
    private BingoSettings bingosettings;
    private Cuboid bedrock;
    private TeamManager teamManager;

    public BingoManager(){
        teamManager = new TeamManager();

        bingoState = BingoState.SETTINGS;
        bingosettings = new BingoSettings(1 , 9 , BingoSettings.BingoDifficulty.NORMAl);
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                if (Bingo.getInstance().wasReset()){
                    World w =  Bukkit.getWorld("world");
                    int spawnx = (int) w.getSpawnLocation().getX();
                    int spawnz = (int) w.getSpawnLocation().getZ();

                    int y = w.getHighestBlockYAt(spawnx , spawnz);
                    Cuboid bedrock1 = new Cuboid(new Location(w, spawnx , y , spawnz) , new Location(w, spawnx+10 , y+4 , spawnz+10));
                    Cuboid bedrock2 = new Cuboid(new Location(w, spawnx+1 , y+1 , spawnz+1) , new Location(w, spawnx+9 , y+4 , spawnz+9));

                    bedrock = bedrock1;
                    for (Block block : bedrock1.getBlocks()) {
                        block.setType(Material.BEDROCK);
                    }
                    for (Block block : bedrock2.getBlocks()) {
                        block.setType(Material.AIR);
                    }
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
        });
    }

    public BingoSettings getBingosettings() {
        return bingosettings;
    }

    public Cuboid getBedrock() {
        return bedrock;
    }
}
