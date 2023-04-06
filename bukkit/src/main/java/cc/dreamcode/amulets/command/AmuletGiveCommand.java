package cc.dreamcode.amulets.command;

import cc.dreamcode.amulets.amulet.Amulet;
import cc.dreamcode.amulets.config.MessageConfig;
import cc.dreamcode.amulets.config.PluginConfig;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredPlayer
@RequiredPermission(permission = "dream-amulets.give")
public class AmuletGiveCommand extends BukkitCommand {

    private @Inject PluginConfig pluginConfig;
    private @Inject MessageConfig messageConfig;

    public AmuletGiveCommand() {
        super("amuletgive", "ag");
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        final Player player = (Player) sender;

        if (args.length < 1) {
            this.messageConfig.usage.send(
                    player,
                    new MapBuilder<String, Object>()
                            .put("usage", "/amuletgive <nazwa amuletu> [gracz]")
                            .build()
            );
        }

        final String amuletId = args[0];
        Amulet amulet = null;

        final boolean useCommandPlayer = args.length > 1;

        List<Amulet> amuletList = this.pluginConfig.amulets;
        for (Amulet amulets : amuletList) {
            if (!amulets.getAmuletId().equals(amuletId)) {
                continue;
            }
            amulet = amulets;
            break;
        }

        if (amulet == null) {
            this.messageConfig.amuletNotFound.send(player);
            return;
        }

        if (useCommandPlayer) {
            String argCommandPlayer = args[1];
            Player commandPlayer = Bukkit.getPlayer(argCommandPlayer);

            if (commandPlayer == null) {
                this.messageConfig.playerNotFound.send(player);
                return;
            }

            commandPlayer.getInventory().addItem(amulet.getItemStack());
            return;
        }

        player.getInventory().addItem(amulet.getItemStack());
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        return null;
    }
}
