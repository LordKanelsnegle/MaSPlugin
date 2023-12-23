package gg.playtogether.mc;

import org.bukkit.plugin.java.JavaPlugin;

import gg.playtogether.mc.commands.CommandPull;
import gg.playtogether.mc.commands.CommandSave;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Enabling MaS Helper plugin.");
        this.getCommand("pull").setExecutor(new CommandPull());
        this.getCommand("save").setExecutor(new CommandSave());
    }
    @Override
    public void onDisable() {
        getLogger().info("Disabling MaS Helper plugin.");
    }
}