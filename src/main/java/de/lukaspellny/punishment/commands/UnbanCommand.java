package de.lukaspellny.punishment.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.lukaspellny.punishment.logic.PunishHistory;
import de.lukaspellny.punishment.logic.IPChecker;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class UnbanCommand implements SimpleCommand {

    public UnbanCommand(ProxyServer proxy) {
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!source.hasPermission("network.command.unban")) {
            source.sendMessage(Component.text("You don't have permission to use this command."));
            return;
        }

        if (args.length < 1) {
            source.sendMessage(Component.text("Usage: /unban <player>"));
            return;
        }

        String playerName = args[0];

        PunishHistory history = PunishHistory.getHistory(playerName);
        if (!history.isBanned()) {
            source.sendMessage(Component.text("Player " + playerName + " is not banned."));
            return;
        }

        history.unbanPlayer();
        String ip = IPChecker.getIP(playerName);
        if (ip != null) {
            IPChecker.unbanIP(ip);
        }

        source.sendMessage(Component.text("Player " + playerName + " has been unbanned."));
    }
}
