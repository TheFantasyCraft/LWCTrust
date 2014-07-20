package com.fantasycraft.lwcmodules.modules;

import com.fantasycraft.lwcmodules.FantasyCraftPlugin;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import lombok.experimental.Value;
import org.bukkit.ChatColor;

/**
 * Created by thomas on 7/20/2014.
 */

@Value
public class ToggleModule extends JavaModule {

    private FantasyCraftPlugin plugin;

    @Override
    public void onCommand(LWCCommandEvent event) {
        if (!event.hasFlag("toggle"))
            return;


        event.setCancelled(true);
        String Name = event.getSender().getName();

        //System.out.println(Name);


        if (plugin.getDisabledPlayers().contains(Name)) {
            plugin.getDisabledPlayers().remove(Name);
            event.getSender().sendMessage(ChatColor.GREEN + "LWC Enabled");
        }
        else {
            plugin.getDisabledPlayers().add(Name);
            event.getSender().sendMessage(ChatColor.RED + "LWC disabled");
        }
    }

    @Override
    public void onRegisterProtection(LWCProtectionRegisterEvent event) {
        if (plugin.getDisabledPlayers().contains(event.getPlayer().getName()))
            event.setCancelled(true);
    }
}
