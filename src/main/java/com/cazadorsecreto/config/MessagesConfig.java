package com.cazadorsecreto.config;

import com.cazadorsecreto.CazadorSecreto;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MessagesConfig {

    private final CazadorSecreto plugin;
    private FileConfiguration messagesConfig;
    private File messagesFile;

    public MessagesConfig(CazadorSecreto plugin) {
        this.plugin = plugin;
        setupMessagesFile();
    }

    private void setupMessagesFile() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            try {
                messagesFile.createNewFile();
                plugin.saveResource("messages.yml", true);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create messages.yml file");
            }
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void reloadMessages() {
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&',
                messagesConfig.getString(path, "&cMessage not found: " + path));
    }

    public String getMessage(String path, Object... replacements) {
        String message = messagesConfig.getString(path, "&cMessage not found: " + path);
        return ChatColor.translateAlternateColorCodes('&',
                String.format(message, replacements));
    }

    public List<String> getMessagesList(String path) {
        return messagesConfig.getStringList(path);
    }

    public void saveMessages() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save messages.yml file");
        }
    }
}