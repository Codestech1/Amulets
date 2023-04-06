package cc.dreamcode.amulets.controller;

import cc.dreamcode.amulets.amulet.Amulet;
import cc.dreamcode.amulets.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class AmuletsController implements Listener {

    private @Inject PluginConfig pluginConfig;

    @EventHandler
    public void onAmuletUse(PlayerInteractEvent event) {
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        List<Amulet> amuletList = this.pluginConfig.amulets;
        Amulet amulet = null;

        for (Amulet amulets : amuletList) {
            ItemStack amuletItemStack = amulets.getItemStack().clone();
            amuletItemStack.setAmount(itemStack.getAmount());
            if (!itemStack.equals(amuletItemStack)) {
                continue;
            }
            amulet = amulets;
            break;
        }

        if (amulet == null) {
            return;
        }

        event.setCancelled(true);
        itemStack.setAmount(itemStack.getAmount() - 1);

        for (PotionEffect effect : amulet.getAmuletEffects()) {
            effect.apply(player);
        }
    }
}
