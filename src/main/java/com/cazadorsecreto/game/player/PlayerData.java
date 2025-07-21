package com.cazadorsecreto.game.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.UUID;

public class PlayerData {

    private final UUID playerId;
    private int score;
    private boolean isInLobby;
    private boolean isAlive;
    private Location previousLocation;
    private int kills;
    private int deaths;
    private int roundsWon;

    public PlayerData(Player player) {
        this.playerId = player.getUniqueId();
        this.score = 0;
        this.isInLobby = true;
        this.isAlive = true;
        this.previousLocation = player.getLocation();
        this.kills = 0;
        this.deaths = 0;
        this.roundsWon = 0;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public boolean isInLobby() {
        return isInLobby;
    }

    public void setInLobby(boolean inLobby) {
        isInLobby = inLobby;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Location getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(Location location) {
        this.previousLocation = location;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        this.kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        this.deaths++;
    }

    public int getRoundsWon() {
        return roundsWon;
    }

    public void addRoundWon() {
        this.roundsWon++;
    }

    public boolean isInGame() {
        return !isInLobby;
    }
}