package co.uk.robuxtrex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import co.uk.robuxtrex.listeners.DeathListener;
import co.uk.robuxtrex.listeners.JoinListener;
import co.uk.robuxtrex.listeners.UpdateChecker;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class App extends JavaPlugin {

    public String ParseColourCoding(String string) {
        return string.replaceAll("&", "§") + "§r";
    }

    public long ConvertFromIntToTicks(long time) {
        if(time == 0) return 2147483646;
        return time/50;
    }

    private static App instance; {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getCommand("permavision").setExecutor(new Commands());
        getLogger().info("Initialised PermaVision by RobuxTRex!");
        if(!config.getBoolean("enabled")) {
            getLogger().warning("PermaVision is disabled in the configuration file. Please enable the plugin or else it will not work as intended!");
            return;
        }

        if(config.getBoolean("debug") == true) {
            getLogger().warning("PermaVision is in debug mode! Please only use this during plugin error reporting & bug fixing. Or else, functionality may break unexpectedly!");
        }

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

        new UpdateChecker(this, 110061).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                getLogger().warning("PermaVision has detected a new version or a later one is present.");
                getLogger().warning("You can download the new update on our Spigot page: https://www.spigotmc.org/resources/permavision.110061/");
            }
        });

        BukkitScheduler scheduler = getServer().getScheduler();
        long duration = ConvertFromIntToTicks(config.getInt("delay"));
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                final FileConfiguration config = App.getInstance().getConfig();
                List<String> players = config.getStringList("players");
            
                for(String name : players) {
                    Player player = Bukkit.getPlayerExact(name);

                    if(player == null) {
                        continue;
                    } else if(name.equals(player.getName())) {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    } // really doesn't make sense... but it works! so i don't care honestly.

                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 255));
                }
            }
        }, 0L, duration);
    }
    @Override
    public void onDisable() {
        getLogger().info("Disabling PermaVision by RobuxTRex!");
    }
}