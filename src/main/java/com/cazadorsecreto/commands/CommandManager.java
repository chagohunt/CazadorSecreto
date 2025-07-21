package com.cazadorsecreto.commands;

import com.cazadorsecreto.CazadorSecreto;
import com.cazadorsecreto.config.MessagesConfig;
import com.cazadorsecreto.game.GameManager;
import com.cazadorsecreto.game.lobby.GameLobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private final CazadorSecreto plugin;
    private final GameManager gameManager;
    private final GameLobby gameLobby;
    private final MessagesConfig messages;

    public CommandManager(CazadorSecreto plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.gameLobby = plugin.getGameLobby();
        this.messages = plugin.getMessagesConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "join":
                handleJoinCommand(sender);
                break;
            case "leave":
                handleLeaveCommand(sender);
                break;
            case "start":
                handleStartCommand(sender);
                break;
            case "stop":
                handleStopCommand(sender);
                break;
            case "forcestop":
                handleForceStopCommand(sender);
                break;
            case "setlobby":
                handleSetLobbyCommand(sender);
                break;
            case "setarena":
                handleSetArenaCommand(sender, args);
                break;
            case "settime":
                handleSetTimeCommand(sender, args);
                break;
            case "score":
                handleScoreCommand(sender);
                break;
            case "reload":
                handleReloadCommand(sender);
                break;
            case "help":
                sendHelp(sender);
                break;
            default:
                sender.sendMessage(messages.getMessage("commands.unknown_command"));
        }
        return true;
    }

    private void handleJoinCommand(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messages.getMessage("commands.player_only"));
            return;
        }

        if (gameManager.isGameRunning()) {
            sender.sendMessage(messages.getMessage("game.running"));
            return;
        }

        gameLobby.addPlayer(player);
        sender.sendMessage(messages.getMessage("commands.join"));
    }

    private void handleLeaveCommand(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messages.getMessage("commands.player_only"));
            return;
        }

        gameLobby.removePlayer(player);
        sender.sendMessage(messages.getMessage("commands.leave"));
    }

    private void handleStartCommand(CommandSender sender) {
        if (!sender.hasPermission("cazador.admin")) {
            sender.sendMessage(messages.getMessage("commands.no_permission"));
            return;
        }

        if (gameManager.isGameRunning()) {
            sender.sendMessage(messages.getMessage("game.already_running"));
            return;
        }

        if (gameLobby.getPlayerCount() < 2) {
            sender.sendMessage(messages.getMessage("game.not_enough_players"));
            return;
        }

        gameManager.startGame(gameLobby.getPlayersInLobby());
    }

    private void handleStopCommand(CommandSender sender) {
        if (!gameManager.isGameRunning()) {
            sender.sendMessage(messages.getMessage("game.not_running"));
            return;
        }

        gameManager.endGame();
    }

    private void handleForceStopCommand(CommandSender sender) {
        if (!sender.hasPermission("cazador.admin")) {
            sender.sendMessage(messages.getMessage("commands.no_permission"));
            return;
        }

        gameManager.endGame();
        sender.sendMessage(messages.getMessage("game.force_stopped"));
    }

    private void handleSetLobbyCommand(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messages.getMessage("commands.player_only"));
            return;
        }

        if (!sender.hasPermission("cazador.admin")) {
            sender.sendMessage(messages.getMessage("commands.no_permission"));
            return;
        }

        gameLobby.setLobbyLocation(player.getLocation());
        sender.sendMessage(messages.getMessage("commands.lobby_set"));
    }

    private void handleSetArenaCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messages.getMessage("commands.player_only"));
            return;
        }

        if (!sender.hasPermission("cazador.admin")) {
            sender.sendMessage(messages.getMessage("commands.no_permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(messages.getMessage("commands.arena_usage"));
            return;
        }

        String position = args[1].toLowerCase();
        if (position.equals("pos1") || position.equals("pos2")) {
            plugin.getConfig().set("arena." + position, player.getLocation());
            plugin.saveConfig();
            sender.sendMessage(messages.getMessage("commands.arena_" + position + "_set"));
        } else {
            sender.sendMessage(messages.getMessage("commands.arena_invalid_pos"));
        }
    }

    private void handleSetTimeCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cazador.admin")) {
            sender.sendMessage(messages.getMessage("commands.no_permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(messages.getMessage("commands.settime_usage"));
            return;
        }

        try {
            int time = Integer.parseInt(args[1]);
            plugin.getConfig().set("game.duration", time);
            plugin.saveConfig();
            sender.sendMessage(messages.getMessage("commands.settime_success", time));
        } catch (NumberFormatException e) {
            sender.sendMessage(messages.getMessage("commands.invalid_number"));
        }
    }

    private void handleScoreCommand(CommandSender sender) {
        if (!gameManager.isGameRunning()) {
            sender.sendMessage(messages.getMessage("game.not_running"));
            return;
        }

        gameManager.getScoreboardManager().displayScores(sender);
    }

    private void handleReloadCommand(CommandSender sender) {
        if (!sender.hasPermission("cazador.admin")) {
            sender.sendMessage(messages.getMessage("commands.no_permission"));
            return;
        }

        plugin.reloadConfig();
        messages.reloadMessages();
        sender.sendMessage(messages.getMessage("commands.reloaded"));
    }

    private void sendHelp(CommandSender sender) {
        List<String> helpMessages = Arrays.asList(
                "§6§l=== Comandos Cazador Secreto ===",
                "§e/cazador join §7- Unirse al juego",
                "§e/cazador leave §7- Salir del juego",
                "§e/cazador start §7- Iniciar partida",
                "§e/cazador stop §7- Detener partida",
                "§e/cazador forcestop §7- Forzar detención (Admin)",
                "§e/cazador setlobby §7- Establecer lobby (Admin)",
                "§e/cazador setarena <pos1|pos2> §7- Establecer arena (Admin)",
                "§e/cazador settime <segundos> §7- Cambiar duración (Admin)",
                "§e/cazador score §7- Ver puntuaciones",
                "§e/cazador reload §7- Recargar configuración (Admin)",
                "§e/cazador help §7- Mostrar esta ayuda"
        );

        helpMessages.forEach(sender::sendMessage);
    }
}