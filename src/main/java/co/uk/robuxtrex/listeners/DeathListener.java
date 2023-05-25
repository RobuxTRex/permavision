package co.uk.robuxtrex.listeners;

import co.uk.robuxtrex.App;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        FileConfiguration config = App.getInstance().getConfig();

        List<String> players = config.getStringList("players");

        for(String name : players) {
            if(name == "*") {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 255));
                }    
            }
            Player player = Bukkit.getPlayerExact(name);

            if(player == null) {
                continue;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 255));
        }
    }
}
