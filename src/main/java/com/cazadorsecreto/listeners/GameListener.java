package com.cazadorsecreto.listeners;

import com.cazadorsecreto.CazadorSecreto;
import com.cazadorsecreto.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {

    private final GameManager gameManager;

    public GameListener(CazadorSecreto plugin) {
        this.gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!gameManager.isGameRunning() || !gameManager.getPlayerData(player).isInGame()) return;

        // Cancelar daño por caída
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
            return;
        }

        // Manejar muerte del jugador
        if (player.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            gameManager.handlePlayerDeath(player);
        }
    }

    @EventHandler
    public void onHealthRegen(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (gameManager.isGameRunning() && gameManager.getPlayerData(player).isInGame()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (gameManager.isGameRunning() && gameManager.getPlayerData(player).isInGame()) {
            event.setCancelled(true);
            player.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (gameManager.isGameRunning()) {
            gameManager.handlePlayerJoinDuringGame(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (gameManager.isGameRunning() && gameManager.getPlayerData(player).isInGame()) {
            gameManager.handlePlayerQuitDuringGame(player);
        }
    }
}