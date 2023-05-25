package co.uk.robuxtrex;

import co.uk.robuxtrex.App;

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

    public String ParsePlaceHolder(String string, Player player) {
        return string.replaceAll("%PLAYER%", player.getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args == null || args[0] == null) return false;

        Player player = (Player) sender; 

        FileConfiguration config = App.getInstance().getConfig();
        FileConfiguration languageConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(App.getInstance().getResource("lang/" + config.getString("language") + ".yml")));
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PermaVision");

        String messagePrefix = ParseColourCode(languageConfig.getString("prefix"));
        String reloadSuccessMessage = ParseColourCode(languageConfig.getString("reloadSuccess"));
        String addedPlayerEffectMessage = ParseColourCode(languageConfig.getString("addedPlayerEffect"));
        String revokedPlayerEffectMessage = ParseColourCode(languageConfig.getString("revokedPlayerEffect"));

        List<String> players = config.getStringList("players");

        switch (args[0]) {
            case "reload":
                if (args.length != 1) return false; // No arguments!
                
                plugin.reloadConfig();

                player.sendMessage(ParsePlaceHolder(messagePrefix + " " + reloadSuccessMessage, player));

                break;
            case "add":
                if (args.length != 2) return false; // Arguments: Player

                players = config.getStringList("players");
                players.add(args[1]);
                config.set("players", players);
                plugin.saveConfig(); 
                plugin.reloadConfig();
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 255));
                
                player.sendMessage(ParsePlaceHolder(messagePrefix + " " + addedPlayerEffectMessage, player));

                break;
            case "revoke":
                if (args.length != 2) return false; // Arguments: Player

                int index = 0;
                players = config.getStringList("players");
        
                for(String plr : players) {
                    if(plr == player.getName()) {
                        players.remove(index++);
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    }
                }

                config.set("players", players);
                plugin.saveConfig();
                plugin.reloadConfig();

                player.sendMessage(ParsePlaceHolder(messagePrefix + " " + revokedPlayerEffectMessage, player));

                break;
        }
        return true;
    }
}
