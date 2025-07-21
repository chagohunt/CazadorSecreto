package com.cazadorsecreto;

import com.cazadorsecreto.commands.CommandManager;
import com.cazadorsecreto.config.ConfigManager;
import com.cazadorsecreto.config.MessagesConfig;
import com.cazadorsecreto.game.GameManager;
import com.cazadorsecreto.game.lobby.GameLobby;
import com.cazadorsecreto.listeners.*;
import com.cazadorsecreto.npcs.NPCTracker;
import org.bukkit.plugin.java.JavaPlugin;

public class CazadorSecreto extends JavaPlugin {

    private static CazadorSecreto instance;
    private GameManager gameManager;
    private GameLobby gameLobby;
    private NPCTracker npcTracker;
    private ConfigManager configManager;
    private MessagesConfig messagesConfig;

    @Override
    public void onEnable() {
        instance = this;

        // Inicializar configuraciones
        configManager = new ConfigManager(this);
        messagesConfig = new MessagesConfig(this);

        // Inicializar sistemas principales
        gameLobby = new GameLobby(this);
        npcTracker = new NPCTracker(this);
        gameManager = new GameManager(this);

        // Registrar comandos
        getCommand("cazador").setExecutor(new CommandManager(this));

        // Registrar listeners
        registerListeners();

        getLogger().info("Plugin Cazador Secreto activado!");
    }

    @Override
    public void onDisable() {
        if (gameManager != null && gameManager.isGameRunning()) {
            gameManager.endGame();
        }
        getLogger().info("Plugin Cazador Secreto desactivado!");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new SignInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new ChestListener(this), this);
        getServer().getPluginManager().registerEvents(new LobbyListener(this), this);
    }

    public static CazadorSecreto getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public GameLobby getGameLobby() {
        return gameLobby;
    }

    public NPCTracker getNpcTracker() {
        return npcTracker;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }
}