package com.cazadorsecreto.listeners;

import com.cazadorsecreto.CazadorSecreto;
import com.cazadorsecreto.game.GameManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    private final GameManager gameManager;

    public PlayerDeathListener(CazadorSecreto plugin) {
        this.gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!gameManager.isGameRunning() || !gameManager.getPlayerData(player).isInGame()) {
            return;
        }

        // Configurar evento de muerte
        event.setDeathMessage(null);
        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.getDrops().clear();

        // Manejar muerte en el GameManager
        gameManager.handlePlayerDeath(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (!gameManager.isGameRunning() || !gameManager.getPlayerData(player).isInGame()) {
            return;
        }

        // Configurar respawn como espectador
        player.setGameMode(GameMode.SPECTATOR);
        event.setRespawnLocation(gameManager.getSpectatorLocation());

        // Mensaje al jugador
        player.sendMessage("§cHas sido eliminado. Espera a la siguiente ronda o el final del juego.");

        // Actualizar scoreboard
        gameManager.getScoreboardManager().updateScoreboard(player);
    }
}