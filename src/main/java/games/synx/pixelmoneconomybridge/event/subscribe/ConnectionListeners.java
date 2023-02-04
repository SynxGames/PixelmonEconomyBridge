package games.synx.pixelmoneconomybridge.event.subscribe;

import games.synx.pixelmoneconomybridge.PixelmonEconomyBridge;
import games.synx.pixelmoneconomybridge.updater.PlayerUpdateThread;
import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListeners implements Listener {

    private final PixelmonEconomyBridge plugin;

    public ConnectionListeners(PixelmonEconomyBridge plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLoginEvent(PlayerJoinEvent event) {
        plugin.getAccountManager()
                .getBankAccount(event.getPlayer().getUniqueId())
                .ifPresent(BankAccount::updatePlayer);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        PlayerUpdateThread.PLAYERS_LAST_MONEY_AMOUNT.remove(event.getPlayer().getUniqueId());
    }

}
