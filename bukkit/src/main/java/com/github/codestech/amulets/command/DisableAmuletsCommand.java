package com.github.codestech.amulets.command;

import com.github.codestech.amulets.BukkitAmuletsPlugin;
import com.github.codestech.amulets.config.MessageConfig;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.bukkit.BukkitCommand;
import eu.okaeri.injector.annotation.Inject;
import java.util.List;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

@RequiredPermission(permission = "dream.amulets.disable")
public class DisableAmuletsCommand extends BukkitCommand {

    private @Inject BukkitAmuletsPlugin bukkitAmuletsPlugin;
    private @Inject MessageConfig messageConfig;

    public DisableAmuletsCommand() {
        super("disableamulets");
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        final boolean enabled = this.bukkitAmuletsPlugin.isAmuletsEnabled();

        this.bukkitAmuletsPlugin.setAmuletsEnabled(!enabled);

        if (enabled) {
            this.messageConfig.amuletsDisabled.send(sender);
            this.messageConfig.amuletsDisabledBroadcast.sendAll();
        } else {
            this.messageConfig.amuletsEnabled.send(sender);
            this.messageConfig.amuletsEnabledBroadcast.sendAll();
        }
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        return null;
    }
}
