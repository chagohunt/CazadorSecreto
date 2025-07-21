package com.cazadorsecreto.npcs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NPCDamageListener implements Listener {

    private final NPCTracker npcTracker;

    public NPCDamageListener(NPCTracker npcTracker) {
        this.npcTracker = npcTracker;
    }

    @EventHandler
    public void onNPCDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        if (npcTracker.isNPC(event.getEntity())) {
            event.setCancelled(true);

            // Aplicar penalización al jugador
            player.damage(2.0); // 1 corazón de daño (2 medios corazones)
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOWNESS,
                    100,  // 5 segundos (20 ticks/segundo)
                    1,    // Nivel 1
                    false,
                    false
            ));

            player.sendMessage("§c¡Penalización! Has perdido 1 corazón por golpear a un NPC");
        }
    }
}