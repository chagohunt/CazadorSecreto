package com.cazadorsecreto.listeners;

import com.cazadorsecreto.CazadorSecreto;
import com.cazadorsecreto.game.lobby.GameLobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class LobbyListener implements Listener {

    private final GameLobby gameLobby;
    private final CazadorSecreto plugin;

    public LobbyListener(CazadorSecreto plugin) {
        this.plugin = plugin;
        this.gameLobby = plugin.getGameLobby();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!gameLobby.isInLobby(event.getPlayer()) || event.getItem() == null) {
            return;
        }

        switch (event.getItem().getType()) {
            case EMERALD:
                // Cambiado a addPlayer() que debe existir en GameLobby
                gameLobby.addPlayer(event.getPlayer());
                event.getPlayer().sendMessage("§aTe has unido a la cola del juego!");
                break;
            case RED_BED:
                gameLobby.removePlayer(event.getPlayer());
                event.getPlayer().sendMessage("§cHas abandonado el lobby");
                break;
            default:
                event.setCancelled(true);
        }
    }

    // ... (otros métodos del listener permanecen igual)
}