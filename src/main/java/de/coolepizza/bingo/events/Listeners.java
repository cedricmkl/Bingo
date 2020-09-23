package de.coolepizza.bingo.events;

import de.coolepizza.bingo.Bingo;
import de.coolepizza.bingo.utils.ScoreboardUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        ScoreboardUtils.setCurrentScoreboard(e.getPlayer() , "§lBINGO");
        e.getPlayer().sendMessage("§aDieser Server nutzt " + Bingo.getInstance().getDescription().getName() + " v" + Bingo.getInstance().getDescription().getVersion()  + " by CoolePizza!");
    }
}
