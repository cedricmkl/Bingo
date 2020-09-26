package de.coolepizza.bingo.manager;

import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.team.Team;
import de.coolepizza.bingo.team.TeamManager;
import de.coolepizza.bingo.utils.ItemSelector;
import de.coolepizza.bingo.utils.ScoreboardUtils;
import de.coolepizza.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ItemManager {
    public ArrayList<Material> needed = new ArrayList<>();
    public HashMap<Integer, ArrayList<Material>> items = new HashMap<>();

    public void start() {

        ArrayList<Material> temp = ItemSelector.getItems(Bingo.getBingoManager().getBingosettings().getDifficulty(), Bingo.getBingoManager().getBingosettings().getItems());

        while (temp.size() != Bingo.getBingoManager().getBingosettings().getItems()) {
            temp = ItemSelector.getItems(Bingo.getBingoManager().getBingosettings().getDifficulty(), Bingo.getBingoManager().getBingosettings().getItems());
        }

        Collections.shuffle(temp);

        needed = temp;

        for (Team value : Team.values()) {
            if (value != Team.SPECTATOR)
                items.put(value.getTeamid(), needed);
            updateScoreboard(value);
        }
        run();
    }

    public HashMap<Integer, ArrayList<Material>> getItems() {
        return items;
    }

    private void run() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Team playerteam = Bingo.getBingoManager().getTeamManager().getTeamFromPlayer(onlinePlayer);
                    for (ItemStack content : onlinePlayer.getInventory().getContents()) {
                        if (content != null){
                            ArrayList<Material> itemsfromteam = getItemsFromTeam(playerteam.getTeamid());
                            if (itemsfromteam.contains(content.getType())){
                                findItem(playerteam ,content.getType() , onlinePlayer);
                                itemsfromteam.remove(content.getType());
                                for (Team value : Team.values()) {
                                    updateScoreboard(value);
                                }
                                if (itemsfromteam.size() == 0){
                                    Bingo.getBingoManager().win(playerteam);
                                }
                            }
                        }
                    }
                }
            }
        };

        runnable.runTaskTimer(Bingo.getInstance(), 0, 10);
    }

    public ArrayList<Material> getNeeded() {
        return needed;
    }

    public ArrayList<Material> getItemsFromTeam(int team) {
        return items.get(team);
    }

    public void setItems(Integer team, ArrayList<Material> materials) {
        items.put(team, materials);
    }

    public void findItem(Team team, Material material, Player who) {
        int itemsnow = Bingo.getBingoManager().getBingosettings().getItems() - getItemsFromTeam(team.getTeamid()).size();
        Bingo.getBingoManager().getTeamManager().getPlayersInTeam(team).forEach(uuid -> {
            if (Bukkit.getPlayer(uuid) != null) {
                Player player = Bukkit.getPlayer(uuid);
                player.sendTitle("§a" + Utils.getItemName(material), "wurde gefunden");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                Bukkit.broadcastMessage(Bingo.prefix + who.getName() + " aus dem Team §9" + team.getTeamid() + " §ahat §9" + Utils.getItemName(material) + " §agefunden! §8(§7"
                        + itemsnow + "§8/§7" + Bingo.getBingoManager().getBingosettings().getItems() + "§8)");
            }
        });
    }

    public void updateScoreboard(Team team) {
        if (team == Team.SPECTATOR) {
            return;
        }
        ArrayList<Material> teamitems = getItemsFromTeam(team.getTeamid());
        for (UUID uuid : Bingo.getBingoManager().getTeamManager().getPlayersInTeam(team)) {
            if (Bukkit.getPlayer(uuid) != null) {
                Player player = Bukkit.getPlayer(uuid);

                ScoreboardUtils.setScore(14, "§9Deine Plazierung: §a" + ". Platz", player.getScoreboard());
                ScoreboardUtils.insert(13, "§9Noch §7" + teamitems.size() + " §9Items");

                ScoreboardUtils.setScore(10, "§a", player.getScoreboard());
                ScoreboardUtils.setScore(9, "§a", player.getScoreboard());
                ScoreboardUtils.setScore(8, "§a", player.getScoreboard());
                ScoreboardUtils.setScore(7, "§a", player.getScoreboard());
                ScoreboardUtils.setScore(6, "§a", player.getScoreboard());
                ScoreboardUtils.setScore(5, "§a", player.getScoreboard());
                ScoreboardUtils.setScore(3 , "§9Dein Team: §7Team " + team.getTeamid() , player.getScoreboard());

                if (teamitems.size() >= 5) {
                    int x = 10;
                    for (Material teamitem : teamitems) {
                        ScoreboardUtils.setScore(x, "§8- §7" + Utils.getItemName(teamitem), player.getScoreboard());
                        x--;
                        if (x <= 6){
                            break;
                        }
                        int noch = teamitems.size() - 4;
                        ScoreboardUtils.setScore(5, "§8und " + noch + " Items mehr"  , player.getScoreboard());
                    }
                } else {
                    int x = 10;
                    for (Material teamitem : teamitems) {
                        ScoreboardUtils.setScore(x, "§8- §7" + Utils.getItemName(teamitem), player.getScoreboard());
                        x--;
                    }
                }
            }
        }
    }

    public int getPlace(Team team) {
        int itemsnow = Bingo.getBingoManager().getBingosettings().getItems() - getItemsFromTeam(team.getTeamid()).size();

        int place = 1;
        for (Map.Entry<Integer, ArrayList<Material>> teamArrayListEntry : items.entrySet()) {
            int itemsfromotherteam = Bingo.getBingoManager().getBingosettings().getItems() - getItemsFromTeam(teamArrayListEntry.getKey()).size();

            if (itemsfromotherteam > itemsnow) {
                place++;
            }
        }
        return place;
    }
}
