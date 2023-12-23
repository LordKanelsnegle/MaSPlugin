package gg.playtogether.mc.commands;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CommandPull implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        String filename = isWindows ? "pull.bat" : "pull.sh";
        String script = new File(filename).getAbsolutePath();
        String[] cmd = isWindows ? new String[]{"cmd.exe", "/c", script} : new String[]{"/bin/sh", script};
        try {
            Process pr = Runtime.getRuntime().exec(cmd);
            String output = new String(pr.getErrorStream().readAllBytes(), StandardCharsets.UTF_8); // git fetch outputs to stderr, so use errorStream
            if (!output.isEmpty()) {
                sender.sendMessage("Datapack updated. Attempting to reload...");
                Bukkit.reloadData();
                sender.sendMessage("[MaSHelper] " + ChatColor.GREEN + "Datapack reloaded successfully.");
            } else {
                sender.sendMessage("[MaSHelper] Already up to date.");
            }
        } catch (Exception ex) {
            Bukkit.getLogger().severe(ex.getMessage());
            sender.sendMessage("" + ChatColor.RED + "An error has occurred.");
        }
        return true;
    }
}
 