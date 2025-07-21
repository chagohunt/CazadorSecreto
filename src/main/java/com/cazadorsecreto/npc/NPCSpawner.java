package com.cazadorsecreto.npcs;

import com.cazadorsecreto.CazadorSecreto;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NPCSpawner {

    private final CazadorSecreto plugin;
    private final List<NPC> activeNPCs;
    private final Random random;

    public NPCSpawner(CazadorSecreto plugin) {
        this.plugin = plugin;
        this.activeNPCs = new ArrayList<>();
        this.random = new Random();
    }

    public void spawnInitialNPCs(int count) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        Location spawnLocation = plugin.getConfig().getLocation("arena.center");

        for (int i = 0; i < count; i++) {
            NPC npc = registry.createNPC(EntityType.PLAYER, "Cazador-" + (i + 1));
            npc.spawn(getRandomSpawnLocation(spawnLocation));
            activeNPCs.add(npc);
        }
    }

    public void respawnNPCs() {
        activeNPCs.forEach(npc -> {
            if (npc.isSpawned()) {
                npc.despawn();
            }
            npc.spawn(getRandomSpawnLocation(plugin.getConfig().getLocation("arena.center")));
        });
    }

    public void startNPCMovement() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (activeNPCs.isEmpty()) {
                    this.cancel();
                    return;
                }

                activeNPCs.forEach(npc -> {
                    if (npc.isSpawned()) {
                        npc.getNavigator().setTarget(getRandomMoveLocation());
                    }
                });
            }
        }.runTaskTimer(plugin, 0L, 100L); // Movimiento cada 5 segundos
    }

    private Location getRandomSpawnLocation(Location center) {
        return center.clone().add(
                random.nextInt(20) - 10,
                0,
                random.nextInt(20) - 10
        );
    }

    private Location getRandomMoveLocation() {
        Location center = plugin.getConfig().getLocation("arena.center");
        return center.clone().add(
                random.nextInt(30) - 15,
                0,
                random.nextInt(30) - 15
        );
    }

    public void clearAllNPCs() {
        activeNPCs.forEach(npc -> {
            if (npc.isSpawned()) {
                npc.despawn();
            }
            npc.destroy();
        });
        activeNPCs.clear();
    }

    public boolean isNPC(org.bukkit.entity.Entity entity) {
        return activeNPCs.stream().anyMatch(npc -> npc.getEntity() != null && npc.getEntity().equals(entity));
    }

    public List<NPC> getActiveNPCs() {
        return activeNPCs;
    }
}