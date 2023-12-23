package gg.playtogether.mc;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;

import gg.playtogether.mc.commands.CommandPull;
import gg.playtogether.mc.commands.CommandSave;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("pull").setExecutor(new CommandPull());
        this.getCommand("save").setExecutor(new CommandSave(this, StructureBlockLibApi.INSTANCE));
        getLogger().info("MaS Helper plugin enabled!");
    }
    @Override
    public void onDisable() {
        getLogger().info("MaS Helper plugin disabled.");
    }
}