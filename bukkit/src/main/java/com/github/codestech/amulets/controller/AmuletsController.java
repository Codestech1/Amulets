package com.github.codestech.amulets.controller;

import com.github.codestech.amulets.BukkitAmuletsPlugin;
import com.github.codestech.amulets.amulet.Amulet;
import com.github.codestech.amulets.config.MessageConfig;
import com.github.codestech.amulets.config.PluginConfig;
import com.github.codestech.amulets.manager.PermanentEffectsManager;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class AmuletsController implements Listener {

    private @Inject BukkitAmuletsPlugin bukkitAmuletsPlugin;
    private @Inject PluginConfig pluginConfig;
    private @Inject MessageConfig messageConfig;
    private @Inject PermanentEffectsManager effectsManager;

    @EventHandler
    public void onAmuletUse(PlayerInteractEvent event) {
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack == null) {
            return;
        }

        Optional<Amulet> optAmulet = this.pluginConfig.amulets
                .stream()
                .filter(amulet -> {
                    ItemStack amuletItemStack = ItemBuilder.of(amulet.getItemStack())
                            .fixColors()
                            .setAmount(itemStack.getAmount())
                            .toItemStack();
                    return itemStack.equals(amuletItemStack);
                })
                .findFirst();

        if (!optAmulet.isPresent()) {
            return;
        }


        event.setCancelled(true);

        if (!bukkitAmuletsPlugin.isAmuletsEnabled()) {
            this.messageConfig.amuletsDisabledUsage.send(player);
            return;
        }

        Amulet amulet = optAmulet.get();

        if (amulet.isPermanentEffectOffhand()) {
            this.messageConfig.amuletsOnlyOffhand.send(player);
            return;
        }

        itemStack.setAmount(itemStack.getAmount() - 1);
        amulet.getAmuletEffects().forEach(potionEffect -> potionEffect.apply(player));

        this.messageConfig.amuletUsed.send(
                player,
                new MapBuilder<String, Object>()
                        .put("amulet", amulet.getAmuletDisplayName())
                        .build()
        );
    }
}
