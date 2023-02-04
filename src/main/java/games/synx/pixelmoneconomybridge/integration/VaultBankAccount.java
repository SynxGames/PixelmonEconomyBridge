package games.synx.pixelmoneconomybridge.integration;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.UUID;

public class VaultBankAccount implements BankAccount {

    private final Economy economy;
    private final UUID uuid;
    private final OfflinePlayer offlinePlayer;

    public VaultBankAccount(Economy economy, UUID uuid, OfflinePlayer offlinePlayer) {
        this.economy = economy;
        this.uuid = uuid;
        this.offlinePlayer = offlinePlayer;
    }

    @Override
    public UUID getIdentifier() {
        return this.uuid;
    }

    @Override
    public BigDecimal getBalance() {
        return BigDecimal.valueOf(
                Math.floor(
                        economy.getBalance(offlinePlayer)
                )
        );
    }

    @Override
    public void setBalance(BigDecimal amount) {
        double current = economy.getBalance(offlinePlayer);
        double needed = amount.doubleValue() - current;
        if (needed == 0) return;
        if (needed > 0) {
            add(needed);
        } else {
            take(needed);
        }
    }

    @Override
    public boolean hasBalance(BigDecimal amount) {
        return amount.doubleValue() <= 0 || economy.has(offlinePlayer, amount.doubleValue());
    }

    @Override
    public boolean take(BigDecimal amount) {
        if (amount.doubleValue() <= 0) {
            return true;
        }
        if (!hasBalance(amount)) {
            return false;
        }
        EconomyResponse response = economy.withdrawPlayer(offlinePlayer, amount.doubleValue());
        return response.transactionSuccess();
    }

    @Override
    public boolean add(BigDecimal amount) {
        EconomyResponse response = economy.depositPlayer(offlinePlayer, amount.doubleValue());
        return response.transactionSuccess();
    }
}
