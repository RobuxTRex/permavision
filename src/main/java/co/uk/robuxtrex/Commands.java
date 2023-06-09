package co.uk.robuxtrex;

import co.uk.robuxtrex.App;
import co.uk.robuxtrex.listeners.UpdateChecker;

import java.io.InputStreamReader;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Commands implements CommandExecutor {

    String ParseColourCode(String string) {
        return string.replaceAll("&", "§") + "§r";
    }

    public String ParsePlaceHolder(String string, Player player, Plugin plugin, Integer ProvidedLength) {
        String returnValue = string.replaceAll("%VERSION%", plugin.getDescription().getVersion());
        returnValue = returnValue.replaceAll("%PLAYER%", player.getName());
        returnValue = returnValue.replaceAll("%ARGSPROVIDED%", "" + ProvidedLength);
        return returnValue;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender; 

        FileConfiguration config = App.getInstance().getConfig();
        FileConfiguration languageConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(App.getInstance().getResource("lang/" + config.getString("language") + ".yml")));
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PermaVision");

        String messagePrefix = ParseColourCode(languageConfig.getString("prefix"));
        String reloadSuccessMessage = ParseColourCode(languageConfig.getString("reloadSuccess"));
        String addedPlayerEffectMessage = ParseColourCode(languageConfig.getString("addedPlayerEffect"));
        String revokedPlayerEffectMessage = ParseColourCode(languageConfig.getString("revokedPlayerEffect"));
        String playerRevokedEffectMessage = ParseColourCode(languageConfig.getString("playerRevokedEffect"));
        String helpOneMessage = ParseColourCode(languageConfig.getString("helpOne"));
        String invalidArgs = ParseColourCode(languageConfig.getString("invalidArgs"));
        String unknownCommand = ParseColourCode(languageConfig.getString("unknownCommand"));

        if(args.length == 0 || args == null || args[0] == "") { player.sendMessage(ParsePlaceHolder(helpOneMessage, player, plugin, 0)); return true; }
        List<String> players = config.getStringList("players");

        Boolean validCommand = false;

        switch (args[0]) {
            case "help":
                player.sendMessage(ParsePlaceHolder(helpOneMessage, player, plugin, 0));

                validCommand = true;
                break;
            case "reload":
                plugin.reloadConfig();

                player.sendMessage(ParsePlaceHolder(messagePrefix + " " + reloadSuccessMessage, player, plugin, 0));

                validCommand = true;
                break;
            case "add":
                if (args.length != 2) player.sendMessage(ParsePlaceHolder(invalidArgs, player, plugin, args.length)); // Arguments: Player

                players = config.getStringList("players");
                players.add(args[1]);
                config.set("players", players);
                plugin.saveConfig(); 
                plugin.reloadConfig();
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 255));
                
                player.sendMessage(ParsePlaceHolder(messagePrefix + " " + addedPlayerEffectMessage, player, plugin, 0));
                
                validCommand = true;
                break;
            case "revoke":
                if (args.length != 2) player.sendMessage(ParsePlaceHolder(invalidArgs, player, plugin, args.length)); // Arguments: Player

                players = config.getStringList("players");
                players.remove(args[1]);
                config.set("players", players);
                plugin.saveConfig();
                plugin.reloadConfig();
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);

                player.sendMessage(ParsePlaceHolder(messagePrefix + " " + revokedPlayerEffectMessage, player, plugin, 0));
                player.sendMessage(ParsePlaceHolder(messagePrefix + " " + playerRevokedEffectMessage, player, plugin, 0));
                
                validCommand = true;
                break;
        }

        if(!validCommand)
        {
            player.sendMessage(ParsePlaceHolder(messagePrefix + " " + unknownCommand, player, plugin, 0));
        }

        return true;
    }
}
