package de.coolepizza.bingo.events;

import com.google.gson.internal.$Gson$Preconditions;
import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.manager.BingoManager;
import de.coolepizza.bingo.manager.BingoSettings;
import de.coolepizza.bingo.team.Team;
import de.coolepizza.bingo.team.TeamManager;
import de.coolepizza.bingo.utils.ItemBuilder;
import de.coolepizza.bingo.utils.ScoreboardUtils;
import de.coolepizza.bingo.utils.TablistManager;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.builder.Diff;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage("§a» §7" + e.getPlayer().getName() + "");
        ScoreboardUtils.setCurrentScoreboard(e.getPlayer() , "§lBINGO");
        e.getPlayer().setPlayerListHeader("§lBINGO");
        e.getPlayer().sendMessage("§aDieser Server nutzt " + Bingo.getInstance().getDescription().getName() + " v" + Bingo.getInstance().getDescription().getVersion()  + " by CoolePizza!");
        if (Bingo.getBingoManager().bingoState == BingoManager.BingoState.SETTINGS && e.getPlayer().hasPermission("bingo.admin")){
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().addItem(new ItemBuilder(Material.NETHER_STAR).setDisplayname("§9Spiel Einstellungen").build());
        }else if (Bingo.getBingoManager().bingoState == BingoManager.BingoState.TEAM_JOIN ){
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().addItem(new ItemBuilder(Material.WHITE_BED).setDisplayname("§9Teamauswahl").build());
            if (e.getPlayer().hasPermission("orav.admin")){
                e.getPlayer().getInventory().setItem(8 , new ItemBuilder(Material.LIME_DYE).setDisplayname("§aRunde starten").build());
            }
        }
        if (Bingo.getInstance().wasReset()) {
            World w = Bukkit.getWorld("world");
            int spawnx = (int) w.getSpawnLocation().getX();
            int spawnz = (int) w.getSpawnLocation().getZ();

            if (Bingo.getBingoManager().bingoState != BingoManager.BingoState.INGAME) {
                int y = w.getHighestBlockYAt(spawnx, spawnz);
                e.getPlayer().teleport(new Location(w, spawnx + 5, y - 1, spawnz + 5));
            }
            Bingo.getBingoManager().getTeamManager().initPlayer(e.getPlayer());

            Team t = Bingo.getBingoManager().getTeamManager().getTeamFromPlayer(e.getPlayer());
            if (t != Team.SPECTATOR) {
                Bingo.getBingoManager().getItemManager().updateScoreboard(t);
            }
        }else {
            if(e.getPlayer().hasPermission("orav.admin")){
                e.getPlayer().sendMessage("§aWenn du die Welt zurücksetzen willst gebe /reset ein!");
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage("§a« §7" + e.getPlayer().getName() + "");
        if (Bingo.getBingoManager().getTeamManager().getTeamFromPlayer(e.getPlayer()) != Team.SPECTATOR && Bingo.getBingoManager().bingoState != BingoManager.BingoState.INGAME){
            Bingo.getBingoManager().getTeamManager().setTeam(e.getPlayer() , Team.SPECTATOR);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
            e.setFormat("§a" + e.getPlayer().getName() + "§7» " + e.getMessage());
        }

    @EventHandler
    public void onDamage(InventoryClickEvent e){
        if (Bingo.getTimer().isPaused()){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (Bingo.getTimer().isPaused()){
            e.setCancelled(true);
        }else {
            e.setCancelled(false);
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (Bingo.getTimer().isPaused()){
            e.setCancelled(true);
        }else {
            e.setCancelled(false);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (e.getItem() == null){
            return;
        }
        if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§9Spiel Einstellungen") && Bingo.getBingoManager().bingoState == BingoManager.BingoState.SETTINGS){
            Bingo.getBingoManager().getBingosettings().openSettingsInventory(e.getPlayer());
            e.setCancelled(true);
        }else if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§9Teamauswahl") && Bingo.getBingoManager().bingoState == BingoManager.BingoState.TEAM_JOIN){
            Bingo.getBingoManager().getTeamManager().openTeamGUI(e.getPlayer());
            e.setCancelled(true);
        }else if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aRunde starten") && Bingo.getBingoManager().bingoState == BingoManager.BingoState.TEAM_JOIN){
            Bingo.getBingoManager().startIngameState();
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockBreak(PlayerDropItemEvent e) {
        if (Bingo.getTimer().isPaused()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (Bingo.getTimer().isPaused()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if (Bingo.getTimer().isPaused()) {
            if (e.getEntityType() != EntityType.PLAYER)
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getView().getTitle() == "§9Bingo §7>> §9Einstellungen"){
            e.setCancelled(true);
            ItemStack itemStack = e.getCurrentItem();

            Player player = (Player) e.getWhoClicked();

            if (itemStack != null){
                String local = itemStack.getItemMeta().getLocalizedName();
                if (local.equalsIgnoreCase("max")){
                    player.playSound(player.getLocation() , Sound.BLOCK_ANVIL_USE , 1 ,1);
                }

                if (itemStack.getType() == Material.LIME_DYE){
                    Bingo.getBingoManager().startTeamState();
                    return;
                }

                if (local.equalsIgnoreCase("maxplayers+")){
                    Bingo.getBingoManager().getBingosettings().addMaxPlayers(player);
                }else if (local.equalsIgnoreCase("maxplayers-")){
                    Bingo.getBingoManager().getBingosettings().removeMaxPlayer(player);
                }else if (local.equalsIgnoreCase("items-")){
                    Bingo.getBingoManager().getBingosettings().removeItems(player);
                }else if (local.equalsIgnoreCase("items+")){
                    Bingo.getBingoManager().getBingosettings().addItems(player);
                }else if (local.startsWith("dif_")){
                    BingoSettings.BingoDifficulty difficulty = null;
                    String dif = local.replace("dif_" , "");
                    if (dif.equalsIgnoreCase("NORMAL")){
                        difficulty = BingoSettings.BingoDifficulty.NORMAl;
                    }else if (dif.equalsIgnoreCase("HARD")){
                        difficulty = BingoSettings.BingoDifficulty.HARD;
                    }else if (dif.equalsIgnoreCase("EASY")){
                        difficulty = BingoSettings.BingoDifficulty.EASY;
                    }
                    Bingo.getBingoManager().getBingosettings().setDifficulty(player , difficulty);
                }
            }
        }else if (e.getView().getTitle().equalsIgnoreCase("§9Teamauswahl")){
            if (e.getCurrentItem() != null){
                Team team = Team.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());
                Team playerteam = Bingo.getBingoManager().getTeamManager().getTeamFromPlayer((Player) e.getWhoClicked());

                if (team.getTeamid() == playerteam.getTeamid()){
                    e.getWhoClicked().sendMessage("§cDu bist bereits in dem Team!");
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation() , Sound.BLOCK_ANVIL_USE , 1 ,1);
                    e.getWhoClicked().closeInventory();
                    return;
                }
                if (Bingo.getBingoManager().getTeamManager().getPlayersInTeam(team).size() >= Bingo.getBingoManager().getBingosettings().getMaxplayersinteam()){
                    e.getWhoClicked().sendMessage("§cDas Team ist voll!");
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation() , Sound.BLOCK_ANVIL_BREAK , 1 ,1);
                    e.getWhoClicked().closeInventory();
                    return;
                }

                    Bingo.getBingoManager().getTeamManager().setTeam((Player) e.getWhoClicked(), team);
                    e.getWhoClicked().sendMessage("§aDu bist jetz in Team " + team.getTeamid() + " !");
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation() , Sound.BLOCK_NOTE_BLOCK_BIT , 1 ,1);
                    e.getWhoClicked().closeInventory();

            }
        }else if (e.getView().getTitle().equals("§9Bingo Items")){
            e.setCancelled(true);
        }
    }

}
