package de.lukaspellny.punishment;

import com.google.common.eventbus.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.google.inject.Inject;
import de.lukaspellny.punishment.commands.UnbanCommand;
import de.lukaspellny.punishment.logic.PunishHistory;
import org.slf4j.Logger;
import de.lukaspellny.punishment.commands.PunishCommand;
import de.lukaspellny.punishment.commands.LookupCommand;
import de.lukaspellny.punishment.commands.ClearHistoryCommand;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id = "punishment", name = "Punishment", version = "1.0", description = "A punish system for Velocity")
public class Punishment {

    private final Logger logger;
    private final CommandManager commandManager;
    private final ProxyServer proxy;

    @Inject
    public Punishment(Logger logger, CommandManager commandManager, ProxyServer proxy) {
        this.logger = logger;
        this.commandManager = commandManager;
        this.proxy = proxy;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandMeta unbanMeta = commandManager.metaBuilder("unban").build();
        commandManager.register(unbanMeta, new UnbanCommand(proxy));

        CommandMeta punishMeta = commandManager.metaBuilder("punish").build();
        commandManager.register(punishMeta, new PunishCommand(proxy));

        CommandMeta lookupMeta = commandManager.metaBuilder("lookup").build();
        commandManager.register(lookupMeta, new LookupCommand(proxy));

        CommandMeta historyMeta = commandManager.metaBuilder("history").build();
        commandManager.register(historyMeta, new ClearHistoryCommand(proxy));

        PunishHistory.loadFromConfig();
    }
}
