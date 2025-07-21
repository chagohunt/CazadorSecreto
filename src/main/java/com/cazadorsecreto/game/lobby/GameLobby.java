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

public class GameLobby {

    private final CazadorSecreto plugin;
    private final Map<UUID, PlayerData> lobbyPlayers;
    private Location lobbyLocation;
    private Location arenaLocation;

    public GameLobby(CazadorSecreto plugin) {
        this.plugin = plugin;
        this.lobbyPlayers = new HashMap<>();
        loadLocations();
    }

    public void addPlayer(Player player) {
        PlayerData playerData = new PlayerData(player);
        lobbyPlayers.put(player.getUniqueId(), playerData);
        teleportToLobby(player);
        giveLobbyItems(player);
        playersInLobby.put(player.getUniqueId(), new PlayerData(player));
        player.teleport(getSpawnLocation());
        playerData.setInLobby(true);
    }

    public void removePlayer(Player player) {
        lobbyPlayers.remove(player.getUniqueId());
        resetPlayer(player);
    }

    public boolean isInLobby(Player player) {
        return lobbyPlayers.containsKey(player.getUniqueId());
    }

    public void setLobbyLocation(Location location) {
        this.lobbyLocation = location;
        plugin.getConfig().set("lobby.location", location);
        plugin.saveConfig();
    }

    public void setArenaLocation(Location location) {
        this.arenaLocation = location;
        plugin.getConfig().set("arena.location", location);
        plugin.saveConfig();
    }

    public void teleportToLobby(Player player) {
        player.teleport(getSpawnLocation());
    }

    public Location getSpawnLocation() {
        return plugin.getConfig().getLocation("lobby.spawn");
    }

    public void teleportToArena(Player player) {
        if (arenaLocation != null) {
            player.teleport(arenaLocation);
        }
    }

    private void giveLobbyItems(Player player) {
        player.getInventory().clear();

        ItemStack joinItem = new ItemStack(org.bukkit.Material.COMPASS);
        ItemMeta joinMeta = joinItem.getItemMeta();
        joinMeta.setDisplayName(org.bukkit.ChatColor.GREEN + "Unirse al juego");
        joinItem.setItemMeta(joinMeta);
        player.getInventory().setItem(0, joinItem);

        ItemStack leaveItem = new ItemStack(org.bukkit.Material.RED_BED);
        ItemMeta leaveMeta = leaveItem.getItemMeta();
        leaveMeta.setDisplayName(org.bukkit.ChatColor.RED + "Salir del lobby");
        leaveItem.setItemMeta(leaveMeta);
        player.getInventory().setItem(8, leaveItem);
    }

    private void resetPlayer(Player player) {
        player.getInventory().clear();
        player.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
    }

    private void loadLocations() {
        this.lobbyLocation = plugin.getConfig().getLocation("lobby.location");
        this.arenaLocation = plugin.getConfig().getLocation("arena.location");
    }

    public int getPlayerCount() {
        return lobbyPlayers.size();
    }

    public Map<UUID, PlayerData> getLobbyPlayers() {
        return lobbyPlayers;
    }
}