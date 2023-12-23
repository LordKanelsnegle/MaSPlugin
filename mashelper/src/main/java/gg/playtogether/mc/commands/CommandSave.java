package gg.playtogether.mc.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import com.github.shynixn.structureblocklib.api.enumeration.StructureRestriction;


public class CommandSave implements CommandExecutor {
    private final StructureBlockLibApi structureBlockLibApi;
    private final Plugin plugin;
    private final World world;
    private final Location[] locations;
    private HashMap<UUID,String> pendingSaves;
    private HashMap<UUID,Integer> saveCounters;

    public CommandSave(Plugin plugin, StructureBlockLibApi structureBlockLibApi) {
        this.plugin = plugin;
        this.structureBlockLibApi = structureBlockLibApi;
        this.world = Bukkit.getWorld("world");
        this.locations = new Location[]{
            new Location(world, 1052, 19, 954),
            new Location(world, 1052, 19, 1002),
            new Location(world, 1004, 19, 954),
            new Location(world, 1004, 19, 1002),
        };
        pendingSaves = new HashMap<UUID,String>();
        saveCounters = new HashMap<UUID,Integer>();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        final Player player = (Player) sender;

        if (args.length == 1) {
            String argument = args[0].toLowerCase();

            if (argument.equals("confirm")) {

                if (pendingSaves.keySet().contains(player.getUniqueId())) {
                    String map = pendingSaves.get(player.getUniqueId());
                    player.sendMessage("Saving all 4 quadrants of " + map + "...");
                    saveCounters.put(player.getUniqueId(), 0);
                    for (int i = 0; i < 4; i++) {
                        String structure = map + Integer.toString(i + 1);
                        structureBlockLibApi
                            .saveStructure(plugin)
                            .at(locations[i])
                            .sizeX(48)
                            .sizeY(48)
                            .sizeZ(48)
                            .includeEntities(true)
                            .restriction(StructureRestriction.SINGLE_48)
                            .saveToWorld(world.getName(), "mas", structure)
                            .onException(c -> c.printStackTrace())
                            .onResult(e -> checkSaveCompleted(player, structure));
                    }
                    pendingSaves.remove(player.getUniqueId());
                } else {
                    player.sendMessage("You have no pending save requests.");
                }

            } else if (argument.equals("cancel")) {

                if (pendingSaves.keySet().contains(player.getUniqueId())) {
                    pendingSaves.remove(player.getUniqueId());
                    player.sendMessage("Save request cancelled successfully.");
                } else {
                    player.sendMessage("You have no pending save requests.");
                }

            } else {
                pendingSaves.put(player.getUniqueId(), argument);
                player.sendMessage("DOUBLE CHECK YOUR SPELLING. To save map '" + argument + "', type '/save confirm'. Otherwise, use '/save cancel'.");
            }

            return true;
        }

        return false;
    }

    private void checkSaveCompleted(Player player, String structure) {
        player.sendMessage("" + ChatColor.GREEN + "Saved structure '" + structure + "'.");
        int count = saveCounters.get(player.getUniqueId());
        count++;
        if (count == 4) {
            count = 0;
            player.sendMessage("Reloading structure data...");
            Bukkit.reloadData();
            player.sendMessage("" + ChatColor.GREEN + "Structure data up-to-date.");
        }
        saveCounters.put(player.getUniqueId(), count);
    }
}
 