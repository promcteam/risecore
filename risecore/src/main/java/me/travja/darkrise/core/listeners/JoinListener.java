package me.travja.darkrise.core.listeners;

import me.travja.darkrise.core.Core;
import me.travja.darkrise.core.legacy.cmds.DelayedCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        DelayedCommand.invoke(Core.getInstance(), event.getPlayer(), Core.getOnJoin());

        if (!event.getPlayer().hasPlayedBefore()) {
            DelayedCommand.invoke(Core.getInstance(), event.getPlayer(), Core.getFirstJoin());
        }
    }

}
