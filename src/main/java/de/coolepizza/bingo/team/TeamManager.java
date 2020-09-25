package de.coolepizza.bingo.team;

import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.utils.ItemBuilder;
import de.coolepizza.bingo.utils.TablistManager;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamManager {
    private HashMap<UUID , Team> teams = new HashMap<>();
    private  Inventory inv  = Bukkit.createInventory(null , 9 , "§9Teamauswahl");
    public Team getTeamFromPlayer(Player player){
        return teams.get(player.getUniqueId());
    }
    public void initPlayer(Player player){
        if (!teams.containsKey(player.getUniqueId())){
            teams.put(player.getUniqueId() , Team.SPECTATOR);
            TablistManager.setTeam(player , Team.SPECTATOR);
        }
    }
    public void setTeam(Player player , Team team){
        teams.put(player.getUniqueId() , team);
    }

    public ArrayList<UUID> getPlayersInTeam(Team team){
        ArrayList<UUID> players = new ArrayList<>();
        for (Map.Entry<UUID, Team> uuidTeamEntry : teams.entrySet()) {
            if (uuidTeamEntry.getValue() == team) {
                players.add(uuidTeamEntry.getKey());
            }
        }
        return players;
    }
    public void openTeamGUI(Player player){

        Team t = getTeamFromPlayer(player);
        Team[] teams = Team.values();

        for (Team team : teams) {
            if (!(team == Team.SPECTATOR)){
                ArrayList<UUID> players = getPlayersInTeam(team);
                int maxplayers = Bingo.getBingoManager().getBingosettings().getMaxplayersinteam();

                ItemBuilder item = new ItemBuilder(team.getMat()).setDisplayname("§9Team " + team.getTeamid()).setLocalizedName(team.name());

                for (int i = 0; i < maxplayers ; i++) {
                    if (players.get(i) != null){
                        item.addLore("§8- §9" + Bukkit.getOfflinePlayer(players.get(i)).getName());
                    }else {
                        item.addLore("§8- §9-");
                    }
                }
                if (team.getTeamid() == t.getTeamid()){
                    item.enchant(Enchantment.ARROW_DAMAGE , 1).addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                inv.addItem(item.build());
            }
        }
        player.openInventory(inv);
    }
}
