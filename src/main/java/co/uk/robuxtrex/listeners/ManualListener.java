package co.uk.robuxtrex.listeners;

import co.uk.robuxtrex.App;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public class ManualListener extends BukkitRunnable {

    private final JavaPlugin plugin;

    public ManualListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run()
    {
        final FileConfiguration config = App.getInstance().getConfig();
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
}
