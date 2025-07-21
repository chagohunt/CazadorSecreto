package com.cazadorsecreto.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageUtils {

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message));
    }

    public static void sendBroadcast(String message) {
        Bukkit.broadcast(Component.text(message));
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.showTitle(
                Component.text(title).color(NamedTextColor.GOLD),
                Component.text(subtitle).color(NamedTextColor.YELLOW),
                fadeIn, stay, fadeOut
        );
    }

    public static void sendActionBar(Player player, String message) {
        player.sendActionBar(Component.text(message).color(NamedTextColor.YELLOW));
    }

    public static void sendMessageList(CommandSender sender, List<String> messages) {
        messages.forEach(msg -> sender.sendMessage(Component.text(msg)));
    }

    public static void sendErrorMessage(CommandSender sender, String message) {
        sendMessage(sender, "§c" + message);
    }

    public static void sendSuccessMessage(CommandSender sender, String message) {
        sendMessage(sender, "§a" + message);
    }

    public static void sendNoPermissionMessage(CommandSender sender) {
        sendErrorMessage(sender, "No tienes permiso para ejecutar este comando.");
    }

    public static void sendFormattedMessage(CommandSender sender, String message, NamedTextColor color, TextDecoration... decorations) {
        Component component = Component.text(message).color(color);
        for (TextDecoration decoration : decorations) {
            component = component.decorate(decoration);
        }
        sender.sendMessage(component);
    }
}