package de.coolepizza.bingo.utils;

import de.coolepizza.bingo.team.Team;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_16_R3.Scoreboard;
import net.minecraft.server.v1_16_R3.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TablistManager {
    private static Scoreboard s = new Scoreboard();
    public static void setTeam(Player p , Team team){
        String teamname = team.getTeamid() + p.getUniqueId().toString().substring(1 , 6);
        for (ScoreboardTeam teams : s.getTeams()) {
            if (teams.getPlayerNameSet().contains(p.getName())){
                teams.getPlayerNameSet().remove(p.getName());
            }
        }
            if (s.getTeam(teamname) != null){
                s.removeTeam(s.getTeam(teamname));
            }
            createTeam(team , teamname);
            s.getTeam(teamname).getPlayerNameSet().add(p.getName());
            sendPackets();
    }
    public static void sendPackets(){
        for (ScoreboardTeam team : s.getTeams()) {
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam(team , 1);
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam2 = new PacketPlayOutScoreboardTeam(team , 0);

            Bukkit.getOnlinePlayers().forEach(player -> {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutScoreboardTeam);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutScoreboardTeam2);
            });
        }
    }
        private static void createTeam(Team g , String teamname){
        s.createTeam(teamname);
        s.getTeam(teamname).setPrefix( new ChatComponentText(g.getScoreboardPrefix()));
    }
}
