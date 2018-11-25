package com.timedfly.commands;

import com.timedfly.TimedFly;
import com.timedfly.configurations.ConfigCache;
import com.timedfly.configurations.ItemsConfig;
import com.timedfly.configurations.Languages;
import com.timedfly.configurations.UpdateConfig;
import com.timedfly.utilities.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private TimedFly plugin;
    private Languages languages;
    private ItemsConfig itemsConfig;
    private UpdateConfig updateConfig;

    public MainCommand(TimedFly plugin, Languages languages, ItemsConfig itemsConfig, UpdateConfig updateConfig) {
        this.plugin = plugin;
        this.languages = languages;
        this.itemsConfig = itemsConfig;
        this.updateConfig = updateConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        final FileConfiguration languageConfig = languages.getLanguageFile();
        final FileConfiguration itemsConfig = this.itemsConfig.getItemsConfig();

        if (cmd.getName().equalsIgnoreCase("timedfly")) {
            if (args.length == 0) {
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
                sender.sendMessage(Message.color("&c&lTimedFly 3 &7created by &cBy_Jack"));
                sender.sendMessage(Message.color("&7To see all commands available use &c&l/tf help"));
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
                sender.sendMessage(Message.color("&c&l                  TimedFly 3"));
                sender.sendMessage("");
                sender.sendMessage(Message.color("&6» &a/tf help &7- show this help page"));
                sender.sendMessage(Message.color("&6» &a/tf reload &7- reloads the config file"));
                sender.sendMessage(Message.color("&6» &a/tf setTime <itemID> <minutes> &7- create a new timed fly"));
                sender.sendMessage(Message.color("&6» &a/tf setPrice <itemID> <price> &7- create a new timed fly"));
                sender.sendMessage(Message.color("&6» &a/tf setItem <itemID> &7- set the item that you have on hand"));
                sender.sendMessage(Message.color("&6» &a/tf permissions &7- see all available permissions"));
                sender.sendMessage(Message.color("&6» &a/tf list &7- see all the ItemID"));
                sender.sendMessage(Message.color("&6» &a/tf help2 &7- show next help page"));
                sender.sendMessage("");
                sender.sendMessage(Message.color("&7                 Version: &c" + plugin.getDescription().getVersion()));
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
                return true;
            }
            if (args[0].equalsIgnoreCase("help2")) {
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
                sender.sendMessage(Message.color("&c&l                  TimedFly 3"));
                sender.sendMessage(Message.color(""));
                sender.sendMessage(Message.color("&6» &a/tfly &7- opens the fly menu"));
                sender.sendMessage(Message.color("&6» &a/tfly set <player> <minutes> &7- set fly mode to another &7player, no cost"));
                sender.sendMessage(Message.color("&6» &a/tfly add <player> <minutes> &7- add minutes to another &7player, no cost"));
                sender.sendMessage(Message.color("&6» &a/tfly on &7- set fly mode to yourself, no time nor cost"));
                sender.sendMessage(Message.color("&6» &a/tfly off &7- unset fly mode to yourself"));
                sender.sendMessage(Message.color("&6» &a/tfly timeleft &7[player] - check how much time do you &7have &7left"));
                sender.sendMessage(Message.color("&6» &a/tf help3 &7- show next help page"));
                sender.sendMessage(Message.color(""));
                sender.sendMessage(Message.color("&7                 Version: &c" + plugin.getDescription().getVersion()));
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
                return true;
            }
            if (args[0].equalsIgnoreCase("help3")) {
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
                sender.sendMessage(Message.color("&c&l                  TimedFly 3"));
                sender.sendMessage(Message.color(""));
                sender.sendMessage(Message.color("&6» &a/tfly refund &7- get your money back"));
                sender.sendMessage(Message.color("&6» &a/tfly fix &7- get your flight ability back"));
                sender.sendMessage(Message.color("&6» &a/tfly stop &7- stop and save your time left"));
                sender.sendMessage(Message.color("&6» &a/tfly resume &7- resume your time left"));
                sender.sendMessage(Message.color(""));
                sender.sendMessage(Message.color("&7                 Version: &c" + plugin.getDescription().getVersion()));
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("timedfly.admin")) {
                    reloadFiles();
                    Message.sendMessage(sender, "&econfig.yml was succesfully reloaded.");
                    Message.sendMessage(sender, "&eitems.yml was succesfully reloaded.");
                    Message.sendMessage(sender, "&elang_" + ConfigCache.getLanguage() + ".yml was succesfully reloaded.");
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        plugin.getNMS().sendTitle(player, Message.color("All files reloaded"), 20, 40, 20);
                    }
                    return true;
                } else {
                    Message.sendMessage(sender, languageConfig.getString("Other.NoPermission.Message"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.Title")), 20, 40, 20);
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.SubTitle")), 20, 40, 20);
                    }
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("update-config")) {
                if (sender.hasPermission("timedfly.admin")) {
                    updateConfig.updateConfig();
                    Message.sendMessage(sender, "&econfig.yml was succesfully updated (If anything was missing).");
                    return true;
                } else {
                    Message.sendMessage(sender, languageConfig.getString("Other.NoPermission.Message"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.Title")), 20, 40, 20);
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.SubTitle")), 20, 40, 20);
                    }
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("list")) {
                if (sender.hasPermission("timedfly.admin")) {
                    ConfigurationSection section = itemsConfig.getConfigurationSection("Items");
                    int i = 0;
                    sender.sendMessage(Message.color("&8&m----------------------------------------"));
                    sender.sendMessage(
                            Message.color("&c                  TimedFly 3 ItemID List"));
                    sender.sendMessage(Message.color(""));
                    for (String list : section.getKeys(false)) {
                        i++;
                        sender.sendMessage(Message.color("&e" + i + ": &7" + list));
                    }
                    sender.sendMessage(Message.color("&8&m----------------------------------------"));
                    return true;
                } else {
                    Message.sendMessage(sender, languageConfig.getString("Other.NoPermission.Message"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.Title")), 20, 40, 20);
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.SubTitle")), 20, 40, 20);
                    }
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("setTime")) {
                if (sender.hasPermission("timedfly.admin")) {
                    if (args.length == 1 || args.length == 2) {
                        Message.sendMessage(sender, languageConfig.getString("Other.SetTime.Usage"));
                        return true;
                    }
                    String item = args[1];
                    String time = args[2];
                    if (itemsConfig.contains("Items." + item)) {
                        itemsConfig.set("Items." + item + ".Time", time);
                        this.itemsConfig.saveItemsConfig();
                        Message.sendMessage(sender, languageConfig.getString("Other.SetTime.Found")
                                .replace("%time%", time).replace("%itemid%", item));
                    } else {
                        Message.sendMessage(sender, languageConfig.getString("Other.SetTime.NotFound")
                                .replace("%time%", time).replace("%itemid%", item));
                    }
                } else {
                    Message.sendMessage(sender, languageConfig.getString("Other.NoPermission.Message"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.Title")), 20, 40, 20);
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.SubTitle")), 20, 40, 20);
                    }
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("setPrice")) {
                if (sender.hasPermission("timedfly.admin")) {
                    if (args.length == 1 || args.length == 2) {
                        Message.sendMessage(sender, languageConfig.getString("Other.SetPrice.Usage"));
                        return true;
                    }
                    String item = args[1];
                    int price = Integer.parseInt(args[2]);
                    if (itemsConfig.contains("Items." + item)) {
                        itemsConfig.set("Items." + item + ".Price", price);
                        this.itemsConfig.saveItemsConfig();
                        Message.sendMessage(sender, languageConfig.getString("Other.SetPrice.Found")
                                .replace("%price%", Integer.toString(price)).replace("%itemid%", item));
                    } else {
                        Message.sendMessage(sender, languageConfig.getString("Other.SetPrice.NotFound")
                                .replace("%price%", Integer.toString(price)).replace("%itemid%", item));
                    }
                } else {
                    Message.sendMessage(sender, languageConfig.getString("Other.NoPermission.Message"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.Title")), 20, 40, 20);
                        plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.SubTitle")), 20, 40, 20);
                    }
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("permissions")) {
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
                sender.sendMessage(Message.color("&c&l                  TimedFly 3 Permissions"));
                sender.sendMessage(Message.color(""));
                sender.sendMessage(Message.color("&6» &atimedfly.admin &7- access to all commands"));
                sender.sendMessage(Message.color("&6» &atimedfly.help &7- access to see the help of the plugin"));
                sender.sendMessage(Message.color("&6» &atimedfly.getupdate &7- get an update notification if available"));
                sender.sendMessage(Message.color("&6» &atimedfly.attack.bypass &7- bypass the restriction of not fly if the player attacks a mob"));
                sender.sendMessage(Message.color("&6» &atimedfly.fly.set &7-  access to set fly to another player"));
                sender.sendMessage(Message.color("&6» &atimedfly.fly.add &7-  access to add time to another player"));
                sender.sendMessage(Message.color("&6» &atimedfly.fly.onoff &7- access to enable or disable fly for your self"));
                sender.sendMessage(Message.color("&6» &atimedfly.limit.bypass &7- bypass the allowed time limit"));
                sender.sendMessage(Message.color("&6» &atimedfly.fly.stopresume &7- access to save, hide and resume the flight time left"));
                sender.sendMessage(Message.color(""));
                sender.sendMessage(Message.color("&8&m----------------------------------------"));
                return true;
            }
            if (args[0].equalsIgnoreCase("setItem")) {
                if (!(sender instanceof Player)) {
                    Message.sendMessage(sender, "&cOnly players can do this");
                    return true;
                }
                if (sender.hasPermission("timedfly.admin")) {
                    Player player = (Player) sender;
                    if (args.length == 1) {
                        Message.sendMessage(sender, languageConfig.getString("Other.SetItem.Usage"));
                        return true;
                    }
                    int itemid = Integer.parseInt(args[1]);
                    int data = player.getItemInHand().getDurability();
                    int ammount = player.getItemInHand().getAmount();
                    String material = player.getItemInHand().getType().name();
                    if (itemsConfig.contains("Items." + itemid)) {
                        itemsConfig.set("Items." + itemid + ".Material", material);
                        itemsConfig.set("Items." + itemid + ".Data", data);
                        itemsConfig.set("Items." + itemid + ".Ammount", ammount);
                        this.itemsConfig.saveItemsConfig();
                        Message.sendMessage(sender, languageConfig.getString("Other.SetItem.Found").replace("%itemid%", Integer.toString(itemid)));
                    } else {
                        Message.sendMessage(sender, languageConfig.getString("Other.SetItem.NotFound").replace("%itemid%", Integer.toString(itemid)));
                    }
                } else {
                    Message.sendMessage(sender, languageConfig.getString("Other.NoPermission.Message"));
                    Player player = (Player) sender;
                    plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.Title")), 20, 40, 20);
                    plugin.getNMS().sendTitle(player, Message.color(languageConfig.getString("Other.NoPermission.SubTitle")), 20, 40, 20);
                    return true;
                }
            }
        }
        return true;
    }

    private void reloadFiles() {
        plugin.reloadConfig();
        languages.createFiles(plugin);
        itemsConfig.createFiles(plugin);
        languages.reloadLanguages();
        itemsConfig.reloadItemsConfig();
        new ConfigCache(plugin);
    }
}
