package com.fantasycraft.lwcmodules.modules;

import com.fantasycraft.lwcmodules.FantasyCraftPlugin;
import com.griefcraft.model.Permission;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCAccessEvent;
import com.griefcraft.scripting.event.LWCProtectionDestroyEvent;
import lombok.experimental.Value;
import org.bukkit.ChatColor;

/**
 * Created by thomas on 7/20/2014.
 */

@Value
public class TrustUntrustModule extends JavaModule {
    private FantasyCraftPlugin plugin;

    @Override
    public void onAccessRequest(LWCAccessEvent event) {
        String Owner = event.getProtection().getOwner();
        String Player = event.getPlayer().getName();

        if (plugin.getTrustedPlayersFor(Owner).contains(Player))
            event.setAccess(Permission.Access.PLAYER);
    }

    @Override
    public void onDestroyProtection(LWCProtectionDestroyEvent event) {
        String Owner = event.getProtection().getOwner();
        String Player = event.getPlayer().getName();

        if (plugin.getTrustedPlayersFor(Owner).contains(Player)) {
            event.getProtection().remove();
            event.setCancelled(false);
            if (!Boolean.parseBoolean(plugin.getLwcPlugin().getLWC().resolveProtectionConfiguration(event.getProtection().getBlock().getType(), "quiet"))) {
              event.getPlayer().sendMessage(ChatColor.RED + "Removed block from trused player: " +  Owner );
            }

        }
    }
}
