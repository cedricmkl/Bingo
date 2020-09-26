package de.coolepizza.bingo.manager;

import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.team.Team;
import de.coolepizza.bingo.utils.ItemSelector;
import de.coolepizza.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemManager {
    public ArrayList<Material> needed = new ArrayList<>();
    public HashMap<Team, ArrayList<Material>> items = new HashMap<>();

    public void start(){

        ArrayList<Material> temp = ItemSelector.getItems(Bingo.getBingoManager().getBingosettings().getDifficulty() , Bingo.getBingoManager().getBingosettings().getItems());

        while (temp.size() != Bingo.getBingoManager().getBingosettings().getItems()){
            temp = ItemSelector.getItems(Bingo.getBingoManager().getBingosettings().getDifficulty() , Bingo.getBingoManager().getBingosettings().getItems());
        }

        needed = temp;

        for (Team value : Team.values()) {
            if (value != Team.SPECTATOR)
                items.put(value , needed);
        }
        run();
    }
    private void run(){
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Team team = Bingo.getBingoManager().getTeamManager().getTeamFromPlayer(onlinePlayer);
                    if (team != Team.SPECTATOR){
                        ArrayList<Material> teamitems = getItemsFromTeam(team);
                        for (ItemStack content : onlinePlayer.getInventory().getContents()) {
                            if (teamitems.contains(content.getType())){
                                teamitems.remove(content.getType());
                                setItems(team , teamitems);
                                findItem(team , content.getType() , onlinePlayer);
                            }
                        }
                    }
                }
            }
        };

        runnable.runTaskTimer(Bingo.getInstance() , 0 , 10);
    }

    public ArrayList<Material> getNeeded() {
        return needed;
    }

    public ArrayList<Material> getItemsFromTeam(Team team) {
        return items.get(team);
    }

    public void setItems(Team team , ArrayList<Material> materials) {
        items.put(team , materials);
    }

    public void findItem(Team team , Material material , Player who) {
        int itemsnow = Bingo.getBingoManager().getBingosettings().getItems() - getItemsFromTeam(team).size();
        Bingo.getBingoManager().getTeamManager().getPlayersInTeam(team).forEach(uuid -> {
            if (Bukkit.getPlayer(uuid) != null){
                Player player  = Bukkit.getPlayer(uuid);
                player.sendTitle("§a" + Utils.getItemName(material) , "wurde gefunden");
                player.playSound(player.getLocation() , Sound.ENTITY_PLAYER_LEVELUP , 1, 1);
                Bukkit.broadcastMessage(Bingo.prefix +who.getName() +  " aus dem Team §9" + team.getTeamid() + " §ahat §9" + Utils.getItemName(material) + " §agefunden! §8(§7"
                        + itemsnow  +"§8/§7" + Bingo.getBingoManager().getBingosettings().getItems() + "§8)");
            }
        });
    }

}
