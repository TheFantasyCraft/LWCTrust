package com.fantasycraft.lwcmodules;

import com.fantasycraft.lwcmodules.modules.ToggleModule;
import com.fantasycraft.lwcmodules.modules.TrustUntrustModule;
import com.griefcraft.lwc.LWCPlugin;
import lombok.Cleanup;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 7/20/2014.
 */
public class FantasyCraftPlugin extends JavaPlugin {


    @Getter
    private List<String> DisabledPlayers = new ArrayList<String>();

    private Map<String,List<String>> TrustedPlayers = new HashMap<String, List<String>>();

    @Getter
    private LWCPlugin lwcPlugin;

    @Override
    public void onEnable() {
        LoadData();
        lwcPlugin = (LWCPlugin) getServer().getPluginManager().getPlugin("LWC");
        lwcPlugin.getLWC().getModuleLoader().registerModule(this, new ToggleModule(this));
        lwcPlugin.getLWC().getModuleLoader().registerModule(this, new TrustUntrustModule(this));
    }

    @Override
    public void onDisable() {
        getLogger().finest("Saving Data...");
        SaveData();
    }

    public List<String> getTrustedPlayersFor(String Playername){

        if (TrustedPlayers.get(Playername) == null)
            TrustedPlayers.put( Playername , new ArrayList<String>());
        return TrustedPlayers.get(Playername);
    }

    public boolean addTrustedPlayerFor(String playername, String friend){
        if (getTrustedPlayersFor(playername).contains(friend))
            return false;
        else
        {
            getTrustedPlayersFor(playername).add(friend);
        }
        return true;

    }

    public boolean rmTrustedPlayerFor(String playername, String nofriendanymore /*Lol*/){
        if (!getTrustedPlayersFor(playername).contains(nofriendanymore.toLowerCase()))
            return false;
        else
        {
            TrustedPlayers.remove(nofriendanymore);
            getTrustedPlayersFor(playername).remove(nofriendanymore);
        }
        return true;
    }

    private List<String> getFriendFrom(String me){
        List<String> returnvalue = new ArrayList<String>();
        for (String owner : TrustedPlayers.keySet()){
            for (String friend : TrustedPlayers.get(owner))
                if (friend.equalsIgnoreCase(me))
                    returnvalue.add(owner);

        }
        return returnvalue;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

         String playername = sender.getName().toLowerCase();

        if (command.getName().equalsIgnoreCase("trustlist"))
        {
            if (args.length == 1)
                playername = args[0].toLowerCase();

            String Friends = ChatColor.GREEN + "Current friends: ";
            for (String friend : getTrustedPlayersFor(playername))
                Friends += friend + " ";
            sender.sendMessage(Friends);

           /* List<String> PeopleThatTrustYou = getTrustedPlayersFor(playername);
            if (PeopleThatTrustYou.size() != 0) {
                String FriendFrom = ChatColor.GREEN + "People That Trust you: ";
                for (String owner : PeopleThatTrustYou)
                    FriendFrom += owner + " ";
                sender.sendMessage(FriendFrom);
            }*/
            return true;
        }

        if (command.getName().equalsIgnoreCase("trust")) {
            if (args.length == 1) {
                String User = args[0].toLowerCase();
                addTrustedPlayerFor(playername, User);
                sender.sendMessage(ChatColor.GREEN + "Friend " + args[0] + " added!");
                if (getServer().getPlayer(User) != null)
                    getServer().getPlayer(User).sendMessage(ChatColor.GREEN + playername + " Trusted you for LWC protections");
            }else{
                sender.sendMessage(ChatColor.RED + "Usage: /trust <friend>");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("untrust")) {
            if (args.length == 1) {
                String User = args[0].toLowerCase();
                rmTrustedPlayerFor(playername, User);
                sender.sendMessage(ChatColor.RED + "Friend " + args[0] + " removed!");
                if (getServer().getPlayer(User) != null)
                    getServer().getPlayer(User).sendMessage(ChatColor.RED + playername + " don't Trust you anymore for LWC protections");
            }else{
                sender.sendMessage(ChatColor.RED + "Usage: /untrust <notafriendanymore>");
            }
            return true;
        }


        return true;
    }


    public void SaveData(){
        try {
            File file =  new File(getDataFolder(),"data.dat");
            @Cleanup
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(DisabledPlayers);
            stream.writeObject(TrustedPlayers);
        }
        catch (Exception e){
            getLogger().severe("Saving failed! " + e.getMessage());
        }
    }

    public void LoadData(){
        try {
            if (!getDataFolder().exists())
                getDataFolder().mkdir();
            File file = new File(getDataFolder(),"data.dat");


            if (!file.exists()){
                file.createNewFile();
                @Cleanup
                ObjectOutputStream tempstream = new ObjectOutputStream(new FileOutputStream(file));
                tempstream.writeObject(DisabledPlayers);
                tempstream.writeObject(TrustedPlayers);
            }
            @Cleanup
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));


            DisabledPlayers = (List<String>) stream.readObject();
            TrustedPlayers = (Map<String, List<String>>) stream.readObject();
        }
        catch (Exception e){
            getLogger().severe("Load failed! " + e.getMessage());
            getLogger().severe("Deleting data may fix this problem!");
            setEnabled(false);
        }

    }
}
