package cc.dreamcode.amulets.amulet;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class AmuletSerdes implements ObjectSerializer<Amulet> {
    @Override
    public boolean supports(@NonNull Class<? super Amulet> type) {
        return Amulet.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull Amulet object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("id", object.getAmuletId());
        data.add("itemStack", object.getItemStack());
        data.add("hideEnchantments", object.isHideEnchantments());
        data.add("hideAttributes", object.isHideAttributes());
        data.add("amuletEffects", object.getAmuletEffects());
    }

    @Override
    public Amulet deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        String id = data.get("id", String.class);
        ItemStack itemStack = data.get("itemStack", ItemStack.class);
        boolean hideEnchantments = data.get("hideEnchantments", boolean.class);
        boolean hideAttributes = data.get("hideAttributes", boolean.class);
        List<PotionEffect> amuletEffectList = data.getAsList("amuletEffects", PotionEffect.class);
        return new Amulet(id, itemStack, hideEnchantments, hideAttributes, amuletEffectList);
    }
}
