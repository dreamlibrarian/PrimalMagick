package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.entities.companions.pixies.AbstractPixieEntity;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItemForge;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItemForge;
import com.verdantartifice.primalmagick.common.items.misc.PixieItemForge;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItemForge;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenBowItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenBowItemForge;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItemForge;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItemForge;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItemForge;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItemForge;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItemForge;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItemForge;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItemForge;
import com.verdantartifice.primalmagick.common.items.tools.TieredBowItem;
import com.verdantartifice.primalmagick.common.items.tools.TieredBowItemForge;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItemForge;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItemForge;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItemForge;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.platform.services.IItemPrototypeService;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.Supplier;

public class ItemPrototypeServiceForge implements IItemPrototypeService {
    @Override
    public Supplier<ArcanometerItem> arcanometer() {
        return ArcanometerItemForge::new;
    }

    @Override
    public <T extends Block> Supplier<ManaFontBlockItem> manaFont(Supplier<T> blockSupplier, Item.Properties properties) {
        return () -> new ManaFontBlockItemForge(blockSupplier.get(), properties);
    }

    @Override
    public <T extends Block> Supplier<SpellcraftingAltarBlockItem> spellcraftingAltar(Supplier<T> blockSupplier, Item.Properties properties) {
        return () -> new SpellcraftingAltarBlockItemForge(blockSupplier.get(), properties);
    }

    @Override
    public Supplier<PrimaliteShieldItem> primaliteShield(Item.Properties properties) {
        return () -> new PrimaliteShieldItemForge(properties);
    }

    @Override
    public Supplier<HexiumShieldItem> hexiumShield(Item.Properties properties) {
        return () -> new HexiumShieldItemForge(properties);
    }

    @Override
    public Supplier<HallowsteelShieldItem> hallowsteelShield(Item.Properties properties) {
        return () -> new HallowsteelShieldItemForge(properties);
    }

    @Override
    public Supplier<PrimaliteTridentItem> primaliteTrident(Item.Properties properties) {
        return () -> new PrimaliteTridentItemForge(properties);
    }

    @Override
    public Supplier<HexiumTridentItem> hexiumTrident(Item.Properties properties) {
        return () -> new HexiumTridentItemForge(properties);
    }

    @Override
    public Supplier<HallowsteelTridentItem> hallowsteelTrident(Item.Properties properties) {
        return () -> new HallowsteelTridentItemForge(properties);
    }

    @Override
    public Supplier<ForbiddenTridentItem> forbiddenTrident(Item.Properties properties) {
        return () -> new ForbiddenTridentItemForge(properties);
    }

    @Override
    public Supplier<TieredBowItem> tieredBow(Tier tier, Item.Properties properties) {
        return () -> new TieredBowItemForge(tier, properties);
    }

    @Override
    public Supplier<ForbiddenBowItem> forbiddenBow(Item.Properties properties) {
        return () -> new ForbiddenBowItemForge(properties);
    }

    @Override
    public Supplier<MundaneWandItem> mundaneWand() {
        return MundaneWandItemForge::new;
    }

    @Override
    public Supplier<ModularWandItem> modularWand(Item.Properties properties) {
        return () -> new ModularWandItemForge(properties);
    }

    @Override
    public Supplier<ModularStaffItem> modularStaff(Item.Properties properties) {
        return () -> new ModularStaffItemForge(properties);
    }

    @Override
    public Supplier<SpawnEggItem> deferredSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Item.Properties props) {
        return () -> new ForgeSpawnEggItem(type, backgroundColor, highlightColor, props);
    }

    @Override
    public Supplier<SpawnEggItem> pixie(Supplier<EntityType<? extends AbstractPixieEntity>> typeSupplier, Source source, Item.Properties properties) {
        return () -> new PixieItemForge(typeSupplier, source, properties);
    }
}
