package com.cazadorsecreto.game.lobby;

import com.cazadorsecreto.CazadorSecreto;
import com.cazadorsecreto.game.player.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LobbyManager {

    private final CazadorSecreto plugin;
    private final Map<UUID, PlayerData> playersInLobby;
    private Location lobbySpawn;

    public LobbyManager(CazadorSecreto plugin) {
        this.plugin = plugin;
        this.playersInLobby = new HashMap<>();
        loadLobbyLocation();
    }

    public void addPlayer(Player player) {
        PlayerData playerData = new PlayerData(player);
        playersInLobby.put(player.getUniqueId(), playerData);
        teleportToLobby(player);
        giveLobbyItems(player);
    }

    public void removePlayer(Player player) {
        playersInLobby.remove(player.getUniqueId());
        resetPlayer(player);
    }

    public boolean isInLobby(Player player) {
        return playersInLobby.containsKey(player.getUniqueId());
    }

    public void setLobbySpawn(Location location) {
        this.lobbySpawn = location;
        plugin.getConfig().set("lobby.spawn", location);
        plugin.saveConfig();
    }

    public void teleportToLobby(Player player) {
        if (lobbySpawn != null) {
            player.teleport(lobbySpawn);
        }
    }

    private void giveLobbyItems(Player player) {
        player.getInventory().clear();
        // Item para iniciar juego
        ItemStack startItem = new ItemStack(org.bukkit.Material.EMERALD);
        ItemMeta meta = startItem.getItemMeta();
        meta.setDisplayName(org.bukkit.ChatColor.GREEN + "Iniciar Juego");
        startItem.setItemMeta(meta);
        player.getInventory().setItem(0, startItem);
    }

    private void resetPlayer(Player player) {
        player.getInventory().clear();
        player.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
    }

    private void loadLobbyLocation() {
        this.lobbySpawn = plugin.getConfig().getLocation("lobby.spawn");
    }

    public int getPlayerCount() {
        return playersInLobby.size();
    }

    public Map<UUID, PlayerData> getPlayersInLobby() {
        return playersInLobby;
    }
}