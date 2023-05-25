package co.uk.robuxtrex.listeners;

import co.uk.robuxtrex.App;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ManualListener {
    public void main(String[] args)
    {
        final FileConfiguration config = App.getInstance().getConfig();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> players = config.getStringList("players");
    
                for(String name : players) {
                    Player player = Bukkit.getPlayerExact(name);
        
                    if(player == null) {
                        continue;
                    } else if(name != player.getName() && player != null) {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    }
        
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 255));
		        }
            }
        }, 0, config.getInt("delay"));
    }
}
