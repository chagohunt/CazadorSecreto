package com.cazadorsecreto.game.rounds;

import com.cazadorsecreto.game.GameManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class RoundSystem {

    private final GameManager gameManager;
    private final Random random;

    public RoundSystem(GameManager gameManager) {
        this.gameManager = gameManager;
        this.random = new Random();
    }

    public void teleportPlayers(List<Player> players) {
        players.forEach(player -> {
            Location randomLocation = getRandomLocationInArena();
            player.teleport(randomLocation);

            // Aplicar efectos de congelamiento (3 segundos)
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.BLINDNESS,
                    60,  // 3 segundos (20 ticks/segundo)
                    1,
                    false,
                    false
            ));

            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW,
                    60,
                    255,  // Máxima potencia
                    false,
                    false
            ));

            // Programar eliminación de efectos después de 3 segundos
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    player.removePotionEffect(PotionEffectType.SLOW);
                }
            }.runTaskLater(gameManager.getPluginInstance(), 60L); // Cambiado a getPluginInstance()
        });
    }

    private Location getRandomLocationInArena() {
        Location pos1 = gameManager.getPluginInstance().getConfig().getLocation("arena.pos1");
        Location pos2 = gameManager.getPluginInstance().getConfig().getLocation("arena.pos2");

        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        double x = minX + (maxX - minX) * random.nextDouble();
        double z = minZ + (maxZ - minZ) * random.nextDouble();
        double y = pos1.getWorld().getHighestBlockYAt((int)x, (int)z) + 1;

        return new Location(pos1.getWorld(), x, y, z);
    }

    public void prepareNewRound() {
        gameManager.getNpcTrackerInstance().respawnNPCs(); // Cambiado a getNpcTrackerInstance()
    }
}