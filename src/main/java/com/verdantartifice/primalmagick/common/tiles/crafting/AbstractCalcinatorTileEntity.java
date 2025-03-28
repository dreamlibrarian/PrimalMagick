package com.verdantartifice.primalmagick.common.tiles.crafting;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.blocks.crafting.AbstractCalcinatorBlock;
import com.verdantartifice.primalmagick.common.capabilities.ITileResearchCache;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.capabilities.TileResearchCache;
import com.verdantartifice.primalmagick.common.containers.CalcinatorContainer;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.base.IOwnedTileEntity;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Base definition of a calcinator tile entity.  Provides the melting functionality for the corresponding
 * block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.crafting.AbstractCalcinatorBlock}
 * @see {@link net.minecraft.tileentity.FurnaceTileEntity}
 */
public abstract class AbstractCalcinatorTileEntity extends TileInventoryPM implements MenuProvider, IOwnedTileEntity {
    protected static final int OUTPUT_CAPACITY = 9;
    
    protected int burnTime;
    protected int burnTimeTotal;
    protected int cookTime;
    protected int cookTimeTotal;
    protected UUID ownerUUID;
    protected ITileResearchCache researchCache;
    
    protected LazyOptional<ITileResearchCache> researchCacheOpt = LazyOptional.of(() -> this.researchCache);
    
    protected Set<SimpleResearchKey> relevantResearch = Collections.emptySet();
    protected final Predicate<SimpleResearchKey> relevantFilter = k -> this.getRelevantResearch().contains(k);
    
    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData calcinatorData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return AbstractCalcinatorTileEntity.this.burnTime;
            case 1:
                return AbstractCalcinatorTileEntity.this.burnTimeTotal;
            case 2:
                return AbstractCalcinatorTileEntity.this.cookTime;
            case 3:
                return AbstractCalcinatorTileEntity.this.cookTimeTotal;
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
            case 0:
                AbstractCalcinatorTileEntity.this.burnTime = value;
                break;
            case 1:
                AbstractCalcinatorTileEntity.this.burnTimeTotal = value;
                break;
            case 2:
                AbstractCalcinatorTileEntity.this.cookTime = value;
                break;
            case 3:
                AbstractCalcinatorTileEntity.this.cookTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    
    public AbstractCalcinatorTileEntity(BlockEntityType<? extends AbstractCalcinatorTileEntity> tileEntityType, BlockPos pos, BlockState state) {
        super(tileEntityType, pos, state, OUTPUT_CAPACITY + 2);
        this.researchCache = new TileResearchCache();
    }
    
    protected boolean isBurning() {
        return this.burnTime > 0;
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        
        this.burnTime = compound.getInt("BurnTime");
        this.burnTimeTotal = compound.getInt("BurnTimeTotal");
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.researchCache.deserializeNBT(compound.getCompound("ResearchCache"));
        
        this.ownerUUID = null;
        if (compound.contains("OwnerUUID")) {
            String ownerUUIDStr = compound.getString("OwnerUUID");
            if (!ownerUUIDStr.isEmpty()) {
                this.ownerUUID = UUID.fromString(ownerUUIDStr);
            }
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("BurnTimeTotal", this.burnTimeTotal);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.put("ResearchCache", this.researchCache.serializeNBT());
        if (this.ownerUUID != null) {
            compound.putString("OwnerUUID", this.ownerUUID.toString());
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide) {
            this.relevantResearch = assembleRelevantResearch();
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbstractCalcinatorTileEntity entity) {
        boolean burningAtStart = entity.isBurning();
        boolean shouldMarkDirty = false;
        
        if (burningAtStart) {
            entity.burnTime--;
        }
        if (!level.isClientSide) {
            ItemStack inputStack = entity.items.get(0);
            ItemStack fuelStack = entity.items.get(1);
            if (entity.isBurning() || !fuelStack.isEmpty() && !inputStack.isEmpty()) {
                // If the calcinator isn't burning, but has meltable input in place, light it up
                if (!entity.isBurning() && entity.canCalcinate(inputStack)) {
                    entity.burnTime = ForgeHooks.getBurnTime(fuelStack, null);
                    entity.burnTimeTotal = entity.burnTime;
                    if (entity.isBurning()) {
                        shouldMarkDirty = true;
                        if (fuelStack.hasContainerItem()) {
                            // If the fuel has a container item (e.g. a lava bucket), place the empty container in the fuel slot
                            entity.items.set(1, fuelStack.getContainerItem());
                        } else if (!fuelStack.isEmpty()) {
                            // Otherwise, shrink the fuel stack
                            fuelStack.shrink(1);
                            if (fuelStack.isEmpty()) {
                                entity.items.set(1, fuelStack.getContainerItem());
                            }
                        }
                    }
                }
                
                // If the calcinator is burning and has meltable input in place, process it
                if (entity.isBurning() && entity.canCalcinate(inputStack)) {
                    entity.cookTime++;
                    if (entity.cookTime == entity.cookTimeTotal) {
                        entity.cookTime = 0;
                        entity.cookTimeTotal = entity.getCookTimeTotal();
                        entity.doCalcination();
                        shouldMarkDirty = true;
                    }
                } else {
                    entity.cookTime = 0;
                }
            } else if (!entity.isBurning() && entity.cookTime > 0) {
                // Decay any cooking progress if the calcinator isn't lit
                entity.cookTime = Mth.clamp(entity.cookTime - 2, 0, entity.cookTimeTotal);
            }
            
            if (burningAtStart != entity.isBurning()) {
                // Update the tile's block state if the calcinator was lit up or went out this tick
                shouldMarkDirty = true;
                level.setBlock(pos, state.setValue(AbstractCalcinatorBlock.LIT, Boolean.valueOf(entity.isBurning())), Block.UPDATE_ALL);
            }
        }
        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }

    protected void doCalcination() {
        ItemStack inputStack = this.items.get(0);
        if (!inputStack.isEmpty() && this.canCalcinate(inputStack)) {
            // Merge the items already in the output inventory with the new output items from the melting
            List<ItemStack> currentOutputs = this.items.subList(2, this.items.size());
            List<ItemStack> newOutputs = this.getCalcinationOutput(inputStack, false);
            List<ItemStack> mergedOutputs = ItemUtils.mergeItemStackLists(currentOutputs, newOutputs);
            for (int index = 0; index < Math.min(mergedOutputs.size(), OUTPUT_CAPACITY); index++) {
                ItemStack out = mergedOutputs.get(index);
                this.items.set(index + 2, (out == null ? ItemStack.EMPTY : out));
            }
            
            // Shrink the input stack
            inputStack.shrink(1);
        }
    }

    protected abstract int getCookTimeTotal();

    public static boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null) > 0;
    }

    protected boolean canCalcinate(ItemStack inputStack) {
        if (inputStack != null && !inputStack.isEmpty()) {
            SourceList sources = AffinityManager.getInstance().getAffinityValues(inputStack, this.level);
            if (sources == null || sources.isEmpty()) {
                // An item without affinities cannot be melted
                return false;
            } else {
                // Merge the items already in the output inventory with the new output items from the melting
                List<ItemStack> currentOutputs = this.items.subList(2, this.items.size());
                List<ItemStack> newOutputs = this.getCalcinationOutput(inputStack, true);   // Force dreg generation to prevent random overflow
                List<ItemStack> mergedOutputs = ItemUtils.mergeItemStackLists(currentOutputs, newOutputs);
                return (mergedOutputs.size() <= OUTPUT_CAPACITY);
            }
        } else {
            return false;
        }
    }
    
    @Nonnull
    protected abstract List<ItemStack> getCalcinationOutput(ItemStack inputStack, boolean alwaysGenerateDregs);
    
    @Nonnull
    protected ItemStack getOutputEssence(EssenceType type, Source source, int count) {
        if (this.isSourceKnown(source)) {
            return EssenceItem.getEssence(type, source, count);
        } else {
            // If the calcinator's owner hasn't discovered the given source, only produce alchemical waste
            return new ItemStack(ItemsPM.ALCHEMICAL_WASTE.get(), count);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new CalcinatorContainer(windowId, playerInv, this, this.calcinatorData);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        boolean flag = !stack.isEmpty() && stack.sameItem(slotStack) && ItemStack.tagMatches(stack, slotStack);
        if (index == 0 && !flag) {
            this.cookTimeTotal = this.getCookTimeTotal();
            this.cookTime = 0;
            this.setChanged();
        }
    }

    @Override
    public void setTileOwner(Player owner) {
        this.ownerUUID = owner.getUUID();
        this.researchCache.update(owner, this.relevantFilter);
    }

    @Override
    public Player getTileOwner() {
        if (this.ownerUUID != null && this.hasLevel() && this.level instanceof ServerLevel serverLevel) {
            Player livePlayer = serverLevel.getServer().getPlayerList().getPlayer(this.ownerUUID);
            if (livePlayer != null && livePlayer.tickCount % 20 == 0) {
                // Update research cache with current player research
                this.researchCache.update(livePlayer, this.relevantFilter);
            }
            return livePlayer;
        }
        return null;
    }
    
    protected boolean isSourceKnown(@Nullable Source source) {
        if (source == null || source.getDiscoverKey() == null) {
            return true;
        } else {
            Player owner = this.getTileOwner();
            if (owner != null) {
                // Check the live research list if possible
                return source.isDiscovered(owner);
            } else {
                // Check the research cache if the owner is unavailable
                return this.researchCache.isResearchComplete(source.getDiscoverKey());
            }
        }
    }
    
    protected Set<SimpleResearchKey> getRelevantResearch() {
        return this.relevantResearch;
    }
    
    protected static Set<SimpleResearchKey> assembleRelevantResearch() {
        return Source.SORTED_SOURCES.stream().map(s -> s.getDiscoverKey()).filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == PrimalMagickCapabilities.RESEARCH_CACHE) {
            return this.researchCacheOpt.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.researchCacheOpt.invalidate();
    }
}
