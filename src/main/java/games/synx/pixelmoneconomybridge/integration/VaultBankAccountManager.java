package games.synx.pixelmoneconomybridge.integration;

import games.synx.pixelmoneconomybridge.PixelmonEconomyBridge;
import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;

public class VaultBankAccountManager implements BankAccountManager {

    private final PixelmonEconomyBridge plugin;

    public VaultBankAccountManager(PixelmonEconomyBridge plugin) {
        this.plugin = plugin;
    }

    @Override
    public Optional<? extends BankAccount> getBankAccount(UUID uuid) {

        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        final Economy economy = plugin.getVaultEconomy();

        if (!economy.hasAccount(offlinePlayer)) {
            economy.createPlayerAccount(offlinePlayer);
        }

        return Optional.of(new VaultBankAccount(economy, uuid, offlinePlayer));
    }

}
