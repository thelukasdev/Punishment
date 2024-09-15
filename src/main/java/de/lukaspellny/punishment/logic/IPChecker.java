package de.lukaspellny.punishment.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IPChecker {

    private static Map<String, String> playerIPs = new HashMap<>();
    private static Set<String> bannedIPs = new HashSet<>();

    public static void banIP(String ip) {
        bannedIPs.add(ip);
        playerIPs.entrySet().stream()
                .filter(entry -> entry.getValue().equals(ip))
                .forEach(entry -> {
                    System.out.println("Banning account: " + entry.getKey());
                });
        saveToConfig();
    }

    public static void unbanIP(String ip) {
        if (bannedIPs.contains(ip)) {
            bannedIPs.remove(ip);
            playerIPs.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(ip))
                    .forEach(entry -> {
                        System.out.println("Unbanning account: " + entry.getKey());
                    });
            saveToConfig();
        } else {
            System.out.println("IP " + ip + " is not banned.");
        }
    }

    public static String getIP(String player) {
        return playerIPs.getOrDefault(player, "unknown-ip");
    }

    public static void registerPlayerIP(String player, String ip) {
        playerIPs.put(player, ip);
    }

    public static boolean isIPBanned(String ip) {
        return bannedIPs.contains(ip);
    }

    public static void loadFromConfig() {

    }

    public static void saveToConfig() {

    }
}
