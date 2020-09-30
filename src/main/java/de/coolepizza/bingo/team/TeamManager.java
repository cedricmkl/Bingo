package de.coolepizza.bingo.team;

import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.utils.ItemBuilder;
import de.coolepizza.bingo.utils.ScoreboardUtils;
import de.coolepizza.bingo.utils.TablistManager;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import javax.swing.text.Style;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamManager {
    private HashMap<UUID , Team> teams = new HashMap<>();
    private  Inventory inv  = Bukkit.createInventory(null , 9 , "§9Teamauswahl");
    public TeamManager(){
        System.out.println("Creating Teams");
    }
    public Team getTeamFromPlayer(Player player){
        return teams.get(player.getUniqueId());
    }
    public void initPlayer(Player player){
        if (!teams.containsKey(player.getUniqueId())){
            setTeam(player , Team.SPECTATOR);
        }
    }
    public void setTeam(Player player , Team team){
        teams.put(player.getUniqueId() , team);
        if (team == Team.SPECTATOR) {
            ScoreboardUtils.setScore(3 , "§9Dein Team: §7SPECTATOR "  , player.getScoreboard());
        }else {
            ScoreboardUtils.setScore(3 , "§9Dein Team: §7Team " + team.getTeamid()  , player.getScoreboard());
        }
        TablistManager.setTeam(player , team);
        updateInventory();
    }

    public ArrayList<UUID> getPlayersInTeam(Team team){
        ArrayList<UUID> players = new ArrayList<>();
        for (Map.Entry<UUID, Team> uuidTeamEntry : teams.entrySet()) {
            if (uuidTeamEntry.getValue().getTeamid() == team.getTeamid()) {
                players.add(uuidTeamEntry.getKey());
            }
        }
        return players;
    }
    public void openTeamGUI(Player player){
        updateInventory();
        player.openInventory(inv);
    }
    public void updateInventory(){
        inv.clear();
        Team[] teams = Team.values();
        for (Team team : teams) {
            if (!(team == Team.SPECTATOR)){
                ArrayList<UUID> players = getPlayersInTeam(team);
                int maxplayers = Bingo.getBingoManager().getBingosettings().getMaxplayersinteam();

                ItemBuilder item = new ItemBuilder(team.getMat()).setDisplayname("§9Team " + team.getTeamid()).setLocalizedName(team.name());
                for (int i = 0; i < maxplayers ; i++) {
                    if (i < players.size()){
                        item.addLore("§8- §a" + Bukkit.getOfflinePlayer(players.get(i)).getName());
                    }else {
                        item.addLore("§8- §7-");
                    }
                }
                inv.addItem(item.build());
            }
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.updateInventory();
        }
    }

    public HashMap<UUID, Team> getTeams() {
        return teams;
    }
}
