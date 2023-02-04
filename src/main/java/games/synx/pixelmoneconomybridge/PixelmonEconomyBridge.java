package games.synx.pixelmoneconomybridge;

import games.synx.pixelmoneconomybridge.event.subscribe.ConnectionListeners;
import games.synx.pixelmoneconomybridge.integration.VaultBankAccountManager;
import games.synx.pixelmoneconomybridge.updater.PlayerUpdateThread;
import com.pixelmonmod.pixelmon.api.economy.BankAccountManager;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class PixelmonEconomyBridge extends JavaPlugin {

    private Economy economy;
    private BankAccountManager accountManager;

    @Override
    public void onEnable() {

        Bukkit.getScheduler().runTaskLater(this, () -> {

            final RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
            if (economyProvider == null) {
                getLogger().log(Level.SEVERE, "PixelmonEconomyBridge could not find Vault. Please install Vault to use this plugin!");
                return;
            }

            economy = economyProvider.getProvider();
            accountManager = new VaultBankAccountManager(this);
            BankAccountProxy.setAccountManager(accountManager);

            Bukkit.getPluginManager().registerEvents(new ConnectionListeners(this), this);

            PlayerUpdateThread.initialize(this);

        }, 1L); //Only execute after the entire server is up and running
    }

    public BankAccountManager getAccountManager() {
        return accountManager;
    }

    public Economy getVaultEconomy() {
        return this.economy;
    }
}
