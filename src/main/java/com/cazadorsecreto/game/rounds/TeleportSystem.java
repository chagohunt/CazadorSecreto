package com.cazadorsecreto.game.rounds;

import com.cazadorsecreto.game.GameManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class TeleportSystem {

    private final GameManager gameManager;
    private final Random random;

    public TeleportSystem(GameManager gameManager) {
        this.gameManager = gameManager;
        this.random = new Random();
    }

    public void teleportPlayersToArena(List<Player> players) {
        for (Player player : players) {
            Location randomLocation = generateRandomLocation();
            player.teleport(randomLocation);
            applyStartEffects(player);

            // Programar eliminación de efectos después de 3 segundos
            new BukkitRunnable() {
                @Override
                public void run() {
                    removeStartEffects(player);
                }
            }.runTaskLater(gameManager.getPlugin(), 60L); // 60 ticks = 3 segundos
        }
    }

    private Location generateRandomLocation() {
        Location pos1 = gameManager.getPlugin().getConfig().getLocation("arena.pos1");
        Location pos2 = gameManager.getPlugin().getConfig().getLocation("arena.pos2");

        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        double x = minX + (maxX - minX) * random.nextDouble();
        double z = minZ + (maxZ - minZ) * random.nextDouble();
        double y = pos1.getWorld().getHighestBlockYAt((int)x, (int)z) + 1;

        return new Location(pos1.getWorld(), x, y, z);
    }

    private void applyStartEffects(Player player) {
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                60,  // Duración en ticks (3 segundos)
                1,    // Amplificador
                false, // No mostrar partículas
                false  // No mostrar icono
        ));
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOWNESS,
                60,
                255,  // Máxima potencia (inmovilidad completa)
                false,
                false
        ));
    }

    private void removeStartEffects(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.SLOWNESS);
    }

    public void teleportToLobby(Player player) {
        Location lobbyLocation = gameManager.getPlugin().getConfig().getLocation("lobby.spawn");
        if (lobbyLocation != null) {
            player.teleport(lobbyLocation);
            removeStartEffects(player);
        }
    }
}