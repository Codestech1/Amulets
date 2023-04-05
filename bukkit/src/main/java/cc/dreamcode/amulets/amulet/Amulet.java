package cc.dreamcode.amulets.amulet;

import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

@Data
public class Amulet {
    private final String amuletId;
    private final ItemStack itemStack;
    private final boolean
            hideEnchantments,
            hideAttributes;
    private final List<PotionEffect> amuletEffects;
}
