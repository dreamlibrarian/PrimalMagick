package com.verdantartifice.primalmagick.common.blocks.misc;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Block definition for a plain wooden table.  Wood tables are decorative blocks used as components
 * for other devices, such as the analysis table.
 * 
 * @author Daedalus4096
 */
public class WoodTableBlock extends Block {
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagick.MODID, "block/wood_table"));

    public WoodTableBlock() {
        super(Block.Properties.of(Material.WOOD).strength(1.5F, 6.0F).sound(SoundType.WOOD));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
