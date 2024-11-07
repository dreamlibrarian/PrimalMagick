package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItemNeoforge;
import com.verdantartifice.primalmagick.platform.services.IItemPrototypeService;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ItemPrototypeServiceNeoforge implements IItemPrototypeService {
    @Override
    public Supplier<ArcanometerItem> arcanometer() {
        return ArcanometerItemNeoforge::new;
    }

    @Override
    public <T extends Block> Supplier<ManaFontBlockItem> manaFont(Supplier<T> blockSupplier, Item.Properties properties) {
        return () -> new ManaFontBlockItemNeoforge(blockSupplier.get(), properties);
    }

    @Override
    public <T extends Block> Supplier<SpellcraftingAltarBlockItem> spellcraftingAltar(Supplier<T> blockSupplier, Item.Properties properties) {
        return () -> new SpellcraftingAltarBlockItemNeoforge(blockSupplier.get(), properties);
    }

    @Override
    public Supplier<PrimaliteShieldItem> primaliteShield(Item.Properties properties) {
        return () -> new PrimaliteShieldItemNeoforge(properties);
    }

    @Override
    public Supplier<HexiumShieldItem> hexiumShield(Item.Properties properties) {
        return () -> new HexiumShieldItemNeoforge(properties);
    }

    @Override
    public Supplier<HallowsteelShieldItem> hallowsteelShield(Item.Properties properties) {
        return () -> new HallowsteelShieldItemNeoforge(properties);
    }

    @Override
    public Supplier<PrimaliteTridentItem> primaliteTrident(Item.Properties properties) {
        return () -> new PrimaliteTridentItemNeoforge(properties);
    }

    @Override
    public Supplier<HexiumTridentItem> hexiumTrident(Item.Properties properties) {
        return () -> new HexiumTridentItemNeoforge(properties);
    }

    @Override
    public Supplier<HallowsteelTridentItem> hallowsteelTrident(Item.Properties properties) {
        return () -> new HallowsteelTridentItemNeoforge(properties);
    }

    @Override
    public Supplier<ForbiddenTridentItem> forbiddenTrident(Item.Properties properties) {
        return () -> new ForbiddenTridentItemNeoforge(properties);
    }
}
