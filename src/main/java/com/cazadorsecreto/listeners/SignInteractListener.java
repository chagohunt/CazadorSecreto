package com.cazadorsecreto.listeners;

import com.cazadorsecreto.CazadorSecreto;
import com.cazadorsecreto.game.GameManager;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignInteractListener implements Listener {

    private final GameManager gameManager;

    public SignInteractListener(CazadorSecreto plugin) {
        this.gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (!(event.getClickedBlock().getState() instanceof Sign sign)) return;

        String[] lines = sign.getLines();
        if (lines[0].equalsIgnoreCase("[Cazador]")) {
            if (gameManager.isGameRunning()) {
                event.getPlayer().sendMessage("¡El juego ya está en progreso!");
                return;
            }

            if (gameManager.getLobbyPlayerCount() < 2) {
                event.getPlayer().sendMessage("Se necesitan al menos 2 jugadores para comenzar");
                return;
            }

            gameManager.startGameCountdown();
            event.getPlayer().sendMessage("¡Iniciando partida en 10 segundos!");
        }
    }
}