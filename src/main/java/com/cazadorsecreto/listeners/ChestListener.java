package com.cazadorsecreto.listeners;

import com.cazadorsecreto.CazadorSecreto;
import com.cazadorsecreto.game.GameManager;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChestListener implements Listener {

    private final GameManager gameManager;
    private final Random random;
    private final List<Material> objectiveItems = Arrays.asList(
            Material.DIAMOND,
            Material.EMERALD,
            Material.GOLD_INGOT
    );

    public ChestListener(CazadorSecreto plugin) {
        this.gameManager = plugin.getGameManager();
        this.random = new Random();
    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof Chest)) return;
        if (!gameManager.isGameRunning()) return;

        Inventory chest = event.getInventory();
        if (chest.firstEmpty() == -1) return;

        // Limpiar cofre antes de agregar nuevos items
        chest.clear();

        // Agregar 1-3 items objetivo aleatorios
        int itemsToAdd = 1 + random.nextInt(3);
        for (int i = 0; i < itemsToAdd; i++) {
            Material randomItem = objectiveItems.get(random.nextInt(objectiveItems.size()));
            chest.setItem(random.nextInt(chest.getSize()), new ItemStack(randomItem));
        }

        // Agregar algunos items de relleno (25% de probabilidad)
        if (random.nextDouble() < 0.25) {
            chest.setItem(random.nextInt(chest.getSize()),
                    new ItemStack(Material.IRON_INGOT, 1 + random.nextInt(3)));
        }
    }
}