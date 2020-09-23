package de.coolepizza.bingo.utils;


import de.coolepizza.bingo.Bingo;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EventListener;

public class Timer {
    public int remainingtime;
    public boolean paused;
    public Timer(){
        remainingtime = 3600;
        paused = true;
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(isPaused()){
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR , new TextComponent("§cWarte..."));
                    });
                }else {

                }
            }
        };
        runnable.runTaskTimer(Bingo.getInstance() , 0 , 20);
    }

    public int getRemainingtime() {
        return remainingtime;
    }

    public boolean isPaused() {
        return paused;
    }
}
