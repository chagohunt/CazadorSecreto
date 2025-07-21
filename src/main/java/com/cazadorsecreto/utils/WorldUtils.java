package com.cazadorsecreto.utils;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WorldUtils {

    public static void disableHunger(Player player) {
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 255, false, false));
    }

    public static void disableFallDamage(World world) {
        world.setGameRule(org.bukkit.GameRule.FALL_DAMAGE, false);
    }

    public static void disableNaturalRegeneration(World world) {
        world.setGameRule(org.bukkit.GameRule.NATURAL_REGENERATION, false);
    }

    public static void disableWeatherCycle(World world) {
        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
    }

    public static void setTimeLock(World world, long time) {
        world.setTime(time);
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
    }

    public static void resetPlayerEffects(Player player) {
        player.getActivePotionEffects().forEach(effect ->
                player.removePotionEffect(effect.getType()));
    }
}