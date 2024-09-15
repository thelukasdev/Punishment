package de.lukaspellny.punishment.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import de.lukaspellny.punishment.logic.PunishHistory;
import net.kyori.adventure.text.Component;

public class ClearHistoryCommand implements SimpleCommand {

    public ClearHistoryCommand(ProxyServer proxy) {
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource source = invocation.source();

        if (!source.hasPermission("network.command.clearhistory")) {
            source.sendMessage(Component.text("You don't have permission to use this command."));
            return;
        }

        if (args.length != 1) {
            source.sendMessage(Component.text("Usage: /history clear <player>"));
            return;
        }

        String playerName = args[0];
        PunishHistory history = PunishHistory.getHistory(playerName);

        if (history == null || history.getPunishments().isEmpty()) {
            source.sendMessage(Component.text("No punishment history found for " + playerName));
            return;
        }

        PunishHistory.clearHistory(playerName);
        source.sendMessage(Component.text("Punishment history cleared for " + playerName));
    }
}