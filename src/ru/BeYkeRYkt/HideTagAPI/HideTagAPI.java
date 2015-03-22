package ru.BeYkeRYkt.HideTagAPI;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import ru.BeYkeRYkt.HideTagAPI.event.PlayerActionTagEvent;
import ru.BeYkeRYkt.HideTagAPI.event.PlayerActionTagEvent.ActionType;

public class HideTagAPI extends JavaPlugin implements Listener {

    private static HideTagAPI plugin;
    private static Scoreboard scoreboard;
    private static String teamName = "HideTagAPI";
    private static boolean autoHide;

    @Override
    public void onEnable() {
        HideTagAPI.plugin = this;
        HideTagAPI.scoreboard = getServer().getScoreboardManager().getNewScoreboard();
        createTeam(scoreboard);

        PluginDescriptionFile pdfFile = getDescription();
        try {
            FileConfiguration fc = getConfig();
            if (!new File(getDataFolder(), "config.yml").exists()) {
                fc.options().header("HideTagAPI v" + pdfFile.getVersion() + " Configuration" + "\nby BeYkeRYkt");
                fc.addDefault("AutoJoinHide", true);
                fc.options().copyDefaults(true);
                saveConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HideTagAPI.autoHide = getConfig().getBoolean("AutoJoinHide");
        getServer().getPluginManager().registerEvents(this, this);
    }

    public static HideTagAPI getInstance() {
        return plugin;
    }

    private static void createTeam(Scoreboard scoreboard) {
        Team team = scoreboard.registerNewTeam(teamName);
        team.setNameTagVisibility(NameTagVisibility.NEVER);
    }

    public static void hideTag(Player player) {
        PlayerActionTagEvent event = new PlayerActionTagEvent(player, ActionType.HIDE);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        if (player.getScoreboard() != null) {
            if (player.getScoreboard().getTeam(teamName) != null) {
                player.getScoreboard().getTeam(teamName).addPlayer(player);
            } else {
                createTeam(player.getScoreboard());
                player.getScoreboard().getTeam(teamName).addPlayer(player);
            }
        } else {
            player.setScoreboard(scoreboard);
            player.getScoreboard().getTeam(teamName).addPlayer(player);
        }
    }

    public static void showTag(Player player) {
        PlayerActionTagEvent event = new PlayerActionTagEvent(player, ActionType.SHOW);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        if (player.getScoreboard() != null) {
            if (player.getScoreboard().getTeam(teamName) != null) {
                player.getScoreboard().getTeam(teamName).removePlayer(player);
            }
        }
    }

    public static Scoreboard getPluginScoreboard() {
        return scoreboard;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        getServer().getScheduler().runTaskLater(this, new Runnable() {

            @Override
            public void run() {
                if (autoHide) {
                    hideTag(player);
                }
            }
        }, 5);
    }
}