package com.cazadorsecreto.game.scoreboard;

import com.cazadorsecreto.game.GameManager;
import com.cazadorsecreto.game.player.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final GameManager gameManager;
    private final Map<UUID, Scoreboard> playerScoreboards;
    private final org.bukkit.scoreboard.ScoreboardManager bukkitScoreboardManager;

    public ScoreboardManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerScoreboards = new HashMap<>();
        this.bukkitScoreboardManager = Bukkit.getScoreboardManager();
    }

    public void createScoreboard(Player player) {
        Scoreboard scoreboard = bukkitScoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(
                "cazador",
                Criteria.DUMMY,
                Component.text("Cazador Secreto").toString()
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
        playerScoreboards.put(player.getUniqueId(), scoreboard);
        updateScoreboard(player);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = playerScoreboards.get(player.getUniqueId());
        if (scoreboard == null) {
            createScoreboard(player);
            return;
        }

        Objective objective = scoreboard.getObjective("cazador");
        if (objective == null) return;

        objective.unregister();
        objective = scoreboard.registerNewObjective(
                "cazador",
                Criteria.DUMMY,
                Component.text("Cazador Secreto").toString()
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        PlayerData playerData = gameManager.getPlayerData(player);

        objective.getScore("Ronda: " + gameManager.getCurrentRound() + "/" + gameManager.getMaxRounds()).setScore(4);
        objective.getScore("Tiempo: " + formatTime(gameManager.getTimeLeft())).setScore(3);
        objective.getScore("Jugadores: " + gameManager.getAlivePlayersCount()).setScore(2);
        objective.getScore("Puntos: " + playerData.getScore()).setScore(1);
    }

    public void updateAllScoreboards() {
        playerScoreboards.keySet().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                updateScoreboard(player);
            }
        });
    }

    public void removeScoreboard(Player player) {
        playerScoreboards.remove(player.getUniqueId());
        player.setScoreboard(bukkitScoreboardManager.getNewScoreboard());
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public void displayFinalScores(CommandSender sender) {
        Map<UUID, PlayerData> playersData = gameManager.getAllPlayersData();

        sender.sendMessage("§6§l=== PUNTUACIONES FINALES ===");
        playersData.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().getScore(), e1.getValue().getScore()))
                .forEach(entry -> {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if (player != null) {
                        sender.sendMessage("§e" + player.getName() + ": §a" + entry.getValue().getScore() + " puntos");
                    }
                });
    }
}