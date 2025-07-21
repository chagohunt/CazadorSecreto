package com.cazadorsecreto.npcs;

import com.cazadorsecreto.CazadorSecreto;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NPCTracker {

    private final CazadorSecreto plugin;
    private final List<NPC> npcs;
    private final Random random;

    public NPCTracker(CazadorSecreto plugin) {
        this.plugin = plugin;
        this.npcs = new ArrayList<>();
        this.random = new Random();
    }

    public void spawnNPCs(int amount) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        Location spawnLocation = plugin.getConfig().getLocation("arena.spawn");

        for (int i = 0; i < amount; i++) {
            NPC npc = registry.createNPC(EntityType.PLAYER, "NPC-" + (i + 1));
            npc.spawn(spawnLocation);
            npcs.add(npc);
        }
    }

    public void moveNPCs() {
        for (NPC npc : npcs) {
            if (npc.isSpawned()) {
                Location randomLocation = getRandomLocationInArena();
                npc.getNavigator().setTarget(randomLocation);
            }
        }
    }

    public void removeAllNPCs() {
        npcs.forEach(NPC::destroy);
        npcs.clear();
    }

    public boolean isNPC(org.bukkit.entity.Entity entity) {
        return npcs.stream().anyMatch(npc -> npc.getEntity() != null && npc.getEntity().equals(entity));
    }

    private Location getRandomLocationInArena() {
        Location pos1 = plugin.getConfig().getLocation("arena.pos1");
        Location pos2 = plugin.getConfig().getLocation("arena.pos2");

        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        double x = minX + (maxX - minX) * random.nextDouble();
        double z = minZ + (maxZ - minZ) * random.nextDouble();
        double y = pos1.getWorld().getHighestBlockYAt((int) x, (int) z) + 1;

        return new Location(pos1.getWorld(), x, y, z);
    }

    public List<NPC> getNPCs() {
        return npcs;
    }

    public void respawnNPCs() {
        removeAllNPCs();
        spawnNPCs(npcs.size());
    }
}