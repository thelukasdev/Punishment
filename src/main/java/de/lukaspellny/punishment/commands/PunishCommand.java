package de.lukaspellny.punishment.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.lukaspellny.punishment.logic.IPChecker;
import de.lukaspellny.punishment.logic.PunishHistory;
import net.kyori.adventure.text.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

public class PunishCommand implements SimpleCommand {

    private final com.velocitypowered.api.proxy.ProxyServer proxy;

    public PunishCommand(com.velocitypowered.api.proxy.ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!source.hasPermission("network.command.punish")) {
            source.sendMessage(Component.text("You don't have permission to use this command."));
            return;
        }

        if (args.length < 2) {
            source.sendMessage(Component.text("Usage: /punish <player> <reason>"));
            return;
        }

        String playerName = args[0];
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
        if (optionalPlayer.isEmpty()) {
            source.sendMessage(Component.text("Player not found."));
            return;
        }

        Player player = optionalPlayer.get();
        PunishHistory history = PunishHistory.getHistory(player.getGameProfile().getName());

        int timesPunished = history.getTimesPunished();
        switch (timesPunished) {
            case 0:
            case 1:
                player.disconnect(Component.text("You have been kicked for: " + reason));
                source.sendMessage(Component.text(player.getGameProfile().getName() + " has been kicked for: " + reason));
                break;
            case 2:
                banPlayer(player, Duration.ofMinutes(10), reason);
                break;
            case 3:
                banPlayer(player, Duration.ofHours(1), reason);
                break;
            case 4:
                banPlayer(player, Duration.ofDays(1), reason);
                break;
            case 5:
                banPlayer(player, Duration.ofDays(7), reason);
                break;
            case 6:
                banPlayer(player, null, reason);
                String ip = IPChecker.getIP(player.getGameProfile().getName());
                IPChecker.banIP(ip);
                break;
            default:
                source.sendMessage(Component.text("An unknown error occurred."));
                return;
        }

        history.addPunishment(reason);
        notifyAdmins(player.getGameProfile().getName(), reason, timesPunished + 1);
    }

    private void banPlayer(Player player, Duration duration, String reason) {
        if (duration == null) {
            player.disconnect(Component.text("You have been permanently banned for: " + reason));
        } else {
            long minutes = duration.toMinutes();
            String durationText = (minutes >= 60) ? (minutes / 60) + " hours" : minutes + " minutes";
            player.disconnect(Component.text("You have been banned for " + durationText + " for: " + reason));
        }
    }

    private void notifyAdmins(String player, String reason, int timesPunished) {
        Component message = Component.text(player + " has been punished (" + timesPunished + " offenses) for: " + reason);
        proxy.getAllPlayers().stream()
                .filter(p -> p.hasPermission("network.punish.notifications"))
                .forEach(admin -> admin.sendMessage(message));
    }
}
