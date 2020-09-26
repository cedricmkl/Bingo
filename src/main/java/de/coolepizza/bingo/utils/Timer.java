package de.coolepizza.bingo.utils;


import de.coolepizza.bingo.Bingo;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {
    public int time;
    public boolean paused;
    public String information;
    public Timer(){
        information = "§7Bingo §a" + Bingo.getInstance().getDescription().getVersion() + " §7by CoolePizza";
        time = 0;
        paused = true;
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(isPaused()){
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR , new TextComponent(information));
                    });
                }else {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR , new TextComponent(Utils.shortInteger(time)));
                    });
                    time++;
                }
            }
        };
        runnable.runTaskTimer(Bingo.getInstance() , 0 , 20);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
