package com.cazadorsecreto.game;

import com.cazadorsecreto.CazadorSecreto;
import com.cazadorsecreto.game.lobby.GameLobby;
import com.cazadorsecreto.game.player.PlayerData;
import com.cazadorsecreto.game.rounds.RoundSystem;
import com.cazadorsecreto.game.scoreboard.ScoreboardManager;
import com.cazadorsecreto.npcs.NPCTracker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GameManager {

    private final CazadorSecreto plugin;
    private final GameLobby gameLobby;
    private final NPCTracker npcTracker;
    private final RoundSystem roundSystem;
    private final ScoreboardManager scoreboardManager;

    private boolean gameRunning = false;
    private int currentRound = 0;
    private final int maxRounds = 5;
    private int gameDuration = 300;
    private int timeLeft;
    private BukkitTask gameTask;
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public GameManager(CazadorSecreto plugin) {
        this.plugin = plugin;
        this.gameLobby = plugin.getGameLobby();
        this.npcTracker = new NPCTracker(plugin);
        this.roundSystem = new RoundSystem(this);
        this.scoreboardManager = new ScoreboardManager(this);
    }
    public CazadorSecreto getPluginInstance() {
        return plugin;
    }

    public NPCTracker getNpcTrackerInstance() {
        return npcTracker;
    }

    // Método corregido para teleportPlayers
    public void teleportPlayers(List<Player> players) {
        roundSystem.teleportPlayersToArena(players);
    }

    // Método modificado para resetPlayer
    private void resetPlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setHealth(20.0);
        player.setFoodLevel(20);
        // Cambiado de teleportToSpawn a teleportToLobby
        gameLobby.teleportToLobby(player);
    }

    // Método agregado para getSpectatorLocation
    public Location getSpectatorLocation() {
        return gameLobby.getSpawnLocation();
    }

    // Resto de los métodos permanecen igual...
    public void startGame(List<Player> players) {
        this.gameRunning = true;
        this.currentRound = 1;
        this.timeLeft = gameDuration;

        players.forEach(player -> {
            PlayerData playerData = new PlayerData(player);
            playerDataMap.put(player.getUniqueId(), playerData);
            preparePlayer(player);
        });

        npcTracker.spawnNPCs(players.size());
        startRound();
        startGameTimer();
    }

    public void endGame() {
        if (!gameRunning) return;

        if (gameTask != null) {
            gameTask.cancel();
        }

        playerDataMap.keySet().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                resetPlayer(player);
            }
        });

        npcTracker.removeAllNPCs();
        playerDataMap.clear();
        gameRunning = false;
        currentRound = 0;
    }

    public void startRound() {
        if (currentRound > maxRounds) {
            endGame();
            return;
        }

        playerDataMap.keySet().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                prepareRoundPlayer(player);
            }
        });

        roundSystem.teleportPlayers(new ArrayList<>(playerDataMap.keySet()));
        scoreboardManager.updateAllScoreboards();
    }

    public void endRound() {
        currentRound++;
        startRound();
    }

    private void preparePlayer(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setHealth(8.0);
        player.setFoodLevel(20);
    }

    private void prepareRoundPlayer(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setHealth(8.0);
    }

    private void resetPlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setHealth(20.0);
        player.setFoodLevel(20);
        gameLobby.teleportToSpawn(player);
    }

    private void startGameTimer() {
        gameTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeLeft <= 0) {
                    endRound();
                    return;
                }

                scoreboardManager.updateAllScoreboards();
                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), k -> new PlayerData(player));
    }

    public void addScore(Player player, int points) {
        PlayerData data = getPlayerData(player);
        data.addScore(points);
        scoreboardManager.updateScoreboard(player);
    }

    public int getLobbyPlayerCount() {
        return gameLobby.getPlayerCount();
    }

    public void startGameCountdown() {
        // Implementación del countdown
    }

    public int getAlivePlayersCount() {
        return (int) playerDataMap.values().stream()
                .filter(PlayerData::isAlive)
                .count();
    }

    public Map<UUID, PlayerData> getAllPlayersData() {
        return playerDataMap;
    }

    public boolean isPlayerInGame(Player player) {
        return playerDataMap.containsKey(player.getUniqueId());
    }

    public void handlePlayerDeath(Player player) {
        PlayerData data = getPlayerData(player);
        data.setAlive(false);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void handlePlayerJoinDuringGame(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage("La partida ya está en progreso. Eres espectador.");
    }

    public void handlePlayerQuitDuringGame(Player player) {
        playerDataMap.remove(player.getUniqueId());
    }

    public Location getSpectatorLocation() {
        return gameLobby.getLobbyLocation();
    }
}