package com.rukluf.telegram;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Telegram extends JavaPlugin implements CommandExecutor {

    private final Map<UUID, Boolean> telegramStatus = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("telegram").setExecutor(this);
        getLogger().info("Telegram Plugin by " + getConfig().getString("author") + " enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Alleen spelers kunnen dit commando uitvoeren.");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (args.length == 0 || !(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("send"))) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on":
                if (telegramStatus.getOrDefault(uuid, false)) {
                    player.sendMessage(ChatColor.AQUA + "Telegram " + ChatColor.GRAY + "is al aan!");
                } else {
                    telegramStatus.put(uuid, true);
                    player.sendMessage(ChatColor.AQUA + "Telegram " + ChatColor.GRAY + "is succesvol aangezet!");
                }
                break;

            case "off":
                if (!telegramStatus.getOrDefault(uuid, false)) {
                    player.sendMessage(ChatColor.AQUA + "Telegram " + ChatColor.GRAY + "is al uitgezet!");
                } else {
                    telegramStatus.put(uuid, false);
                    player.sendMessage(ChatColor.AQUA + "Telegram " + ChatColor.GRAY + "is succesvol uitgezet!");
                }
                break;

            case "send":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.AQUA + "Telegram " + ChatColor.GRAY + "Gebruik: /telegram send <bericht>");
                    return true;
                }
                if (!telegramStatus.getOrDefault(uuid, false)) {
                    player.sendMessage(ChatColor.AQUA + "Telegram " + ChatColor.GRAY + "Doe eerst /telegram on");
                    return true;
                }
                String bericht = String.join(" ", args).substring(args[0].length() + 1);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (telegramStatus.getOrDefault(p.getUniqueId(), false)) {
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "Telegram" + ChatColor.GRAY + "] " + ChatColor.WHITE + player.getName() + ": " + ChatColor.GRAY + bericht);
                    }
                }
                break;

            case "help":
                sendHelp(player);
                break;

            default:
                sendHelp(player);
                break;
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "---------------------");
        player.sendMessage(ChatColor.AQUA + "Telegram Help");
        player.sendMessage(ChatColor.DARK_GRAY + "/telegram on " + ChatColor.GRAY + "- Doe Telegram aan!");
        player.sendMessage(ChatColor.DARK_GRAY + "/telegram off " + ChatColor.GRAY + "- Doe Telegram uit!");
        player.sendMessage(ChatColor.DARK_GRAY + "/telegram send <bericht> " + ChatColor.GRAY + "- Stuur bericht in Telegram");
        player.sendMessage(ChatColor.DARK_GRAY + "---------------------");
    }
}