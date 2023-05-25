package co.uk.robuxtrex.listeners;

import co.uk.robuxtrex.App;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.InputStreamReader;
import java.util.List;

public class JoinListener implements Listener {

    String ParseColourCode(String string) {
        return string.replaceAll("&", "§") + "§r";
    }

    public String ParsePlaceHolder(String string, Player player) {
        return string.replaceAll("%PLAYER%", player.getName());
    }

    FileConfiguration config = App.getInstance().getConfig();
    FileConfiguration languageConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(App.getInstance().getResource("lang/" + config.getString("language") + ".yml")));

    String messagePrefix = ParseColourCode(languageConfig.getString("prefix"));
    String joinMessage = ParseColourCode(languageConfig.getString("joinMessage"));

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(config.getBoolean("advertisement")) {

            Player plr = event.getPlayer();
            String plrName = plr.getName();
    
            plr.sendMessage("§6Welcome to the server, " + plrName + "!");    
        }

        List<String> players = config.getStringList("players");

        for(String name : players) {
            Player player = Bukkit.getPlayerExact(name);
            Player plr = event.getPlayer();

            if(player == plr) {
                plr.sendMessage(ParsePlaceHolder(joinMessage, plr));    
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 255));
            } else if (name == "*") {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 255));
            }
        }
    }
}
