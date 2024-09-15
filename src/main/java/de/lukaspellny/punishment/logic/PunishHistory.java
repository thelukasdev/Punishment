package de.lukaspellny.punishment.logic;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class PunishHistory {

    private static Map<String, PunishHistory> histories = new HashMap<>();
    private List<PunishmentEntry> punishments;
    private boolean banned;

    public PunishHistory() {
        this.punishments = new ArrayList<>();
        this.banned = false;
    }

    public static PunishHistory getHistory(String player) {
        return histories.getOrDefault(player, new PunishHistory());
    }

    public List<PunishmentEntry> getPunishments() {
        return punishments;
    }

    public int getTimesPunished() {
        return punishments.size();
    }

    public void addPunishment(String reason) {
        punishments.add(new PunishmentEntry(reason));
        if (getTimesPunished() >= 7) {
            this.banned = true;
        }

    }

    public boolean isBanned() {
        return banned;
    }

    public void unbanPlayer() {
        this.banned = false;
        saveToConfig();
    }

    public static void clearHistory(String player) {
        histories.remove(player);
        saveToConfig();
    }

    public static void loadFromConfig() {

    }

    public static void saveToConfig() {
        // --> Muss hier noch die Conifg Logic implementieren...
    }
}
