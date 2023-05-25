package co.uk.robuxtrex;

import co.uk.robuxtrex.App;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ApplyPotions {
    private static ApplyPotions instance; {
        FileConfiguration config = App.getInstance().getConfig();

        List<String> selectedPlayers = config.getStringList("players");

        for(String name : selectedPlayers) {
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
        
        instance = this;
    }

    public static ApplyPotions getInstance() {
        return instance;
    }
}
