package me.travja.darkrise.core.listeners;

import me.travja.darkrise.core.Core;
import me.travja.darkrise.core.config.CommandBlock;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;

public class InteractListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        Collection<CommandBlock> onInteract = Core.getOnInteract(block.getType());//, block.getData());
        for (final CommandBlock commandBlock : onInteract) {
            commandBlock.invoke(e);
        }
    }

}
