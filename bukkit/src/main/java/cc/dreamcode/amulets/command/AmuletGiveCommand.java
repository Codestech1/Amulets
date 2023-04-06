package cc.dreamcode.amulets.command;

import cc.dreamcode.amulets.amulet.Amulet;
import cc.dreamcode.amulets.config.MessageConfig;
import cc.dreamcode.amulets.config.PluginConfig;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            return;
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

        ItemStack itemStack = ItemBuilder.of(amulet.getItemStack())
                .fixColors()
                .toItemStack();

        if (useCommandPlayer) {
            String argCommandPlayer = args[1];
            Player commandPlayer = Bukkit.getPlayer(argCommandPlayer);

            if (commandPlayer == null) {
                this.messageConfig.playerNotFound.send(player);
                return;
            }

            commandPlayer.getInventory().addItem(itemStack);
            if (this.pluginConfig.sendMessageReceived) {
                this.messageConfig.amuletGivenSuccess.send(
                        player,
                        new MapBuilder<String, Object>()
                                .put("amulet", amulet.getAmuletDisplayName())
                                .put("player", commandPlayer.getName())
                                .build()
                );
                this.messageConfig.amuletReceived.send(
                        commandPlayer,
                        new MapBuilder<String, Object>()
                                .put("amulet", amulet.getAmuletDisplayName())
                                .build()
                );
            }
            return;
        }

        player.getInventory().addItem(itemStack);
        this.messageConfig.amuletReceived.send(
                player,
                new MapBuilder<String, Object>()
                        .put("amulet", amulet.getAmuletDisplayName())
                        .build()
        );
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        if (args.length == 1) {
            String amuletArg = args[0];
            List<Amulet> amuletList = this.pluginConfig.amulets;
            List<String> argList = new ArrayList<>();
            for (Amulet amulet : amuletList) {
                if (amulet.getAmuletId().startsWith(amuletArg)) {
                    argList.add(amulet.getAmuletId());
                }
            }
            return argList;
        }

        if (args.length == 2) {
            String playerName = args[1];
            List<Player> playerList = Bukkit.matchPlayer(playerName);
            return playerList.stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }

        return null;
    }
}
