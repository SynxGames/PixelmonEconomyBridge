package games.synx.pixelmoneconomybridge.updater;

import games.synx.pixelmoneconomybridge.PixelmonEconomyBridge;
import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class PlayerUpdateThread {

    private static BukkitTask bukkitTask;
    public static HashMap<UUID, Double> PLAYERS_LAST_MONEY_AMOUNT = new HashMap<>();

    public static void initialize(PixelmonEconomyBridge plugin) {
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }

        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {

            final HashSet<Player> playersToUpdate = new HashSet<>();

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Double currentBalance = plugin.getVaultEconomy().getBalance(onlinePlayer);
                Double oldBalance = PLAYERS_LAST_MONEY_AMOUNT.put(onlinePlayer.getUniqueId(), currentBalance);

                if (oldBalance == null || !oldBalance.equals(currentBalance)) {
                    playersToUpdate.add(onlinePlayer);
                }

            }

            if (playersToUpdate.size() == 0) {
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                for (Player player : playersToUpdate) {
                    if (player.isOnline()) {
                        plugin.getAccountManager()
                                .getBankAccount(player.getUniqueId())
                                .ifPresent(BankAccount::updatePlayer);
                    }
                }
            });

        }, 1, 10);

    }

}
