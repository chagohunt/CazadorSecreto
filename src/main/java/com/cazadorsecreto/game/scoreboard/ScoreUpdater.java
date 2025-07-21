package com.cazadorsecreto.game.scoreboard;

import com.cazadorsecreto.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreUpdater extends BukkitRunnable {

    private final GameManager gameManager;
    private final ScoreboardManager scoreboardManager;

    public ScoreUpdater(GameManager gameManager, ScoreboardManager scoreboardManager) {
        this.gameManager = gameManager;
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public void run() {
        if (!gameManager.isGameRunning()) {
            this.cancel();
            return;
        }

        // Actualizar el tiempo restante primero
        gameManager.updateGameTime();

        // Actualizar scoreboards para todos los jugadores en el juego
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (gameManager.getPlayerData(player).isInGame()) {
                scoreboardManager.updateScoreboard(player);
            }
        }

        // Actualizar NPCs cada 5 segundos (100 ticks)
        if (gameManager.getCurrentTick() % 100 == 0) {
            gameManager.getNpcTracker().moveNPCs();
        }
    }

    public void startUpdater() {
        this.runTaskTimer(gameManager.getPlugin(), 0L, 20L); // Actualiza cada segundo (20 ticks)
    }
}