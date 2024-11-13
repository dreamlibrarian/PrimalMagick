package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceTransmuterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.HoneyExtractorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntity;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IBlockEntityPrototypeService {
    // Crafting tiles
    BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> calcinator();
    BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> essenceFurnace();
    BlockEntityType.BlockEntitySupplier<ConcocterTileEntity> concocter();

    // Device tiles
    BlockEntityType.BlockEntitySupplier<DissolutionChamberTileEntity> dissolutionChamber();
    BlockEntityType.BlockEntitySupplier<EssenceTransmuterTileEntity> essenceTransmuter();
    BlockEntityType.BlockEntitySupplier<HoneyExtractorTileEntity> honeyExtractor();
    BlockEntityType.BlockEntitySupplier<InfernalFurnaceTileEntity> infernalFurnace();

    // Misc tiles
    BlockEntityType.BlockEntitySupplier<CarvedBookshelfTileEntity> carvedBookshelf();
}
