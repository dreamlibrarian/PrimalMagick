package com.verdantartifice.primalmagick.common.components;

import java.util.function.UnaryOperator;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod data component types.
 * 
 * @author Daedalus4096
 */
public class DataComponentsPM {
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, PrimalMagick.MODID);
    
    public static void init() {
        DATA_COMPONENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<DataComponentType<WandCore>> WAND_CORE = register("wand_core", builder -> builder.persistent(WandCore.CODEC).networkSynchronized(WandCore.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<WandCap>> WAND_CAP = register("wand_cap", builder -> builder.persistent(WandCap.CODEC).networkSynchronized(WandCap.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<WandGem>> WAND_GEM = register("wand_gem", builder -> builder.persistent(WandGem.CODEC).networkSynchronized(WandGem.STREAM_CODEC));
    
    public static final RegistryObject<DataComponentType<WandCore>> WAND_CORE_APPEARANCE = register("wand_core_appearance", builder -> builder.persistent(WandCore.CODEC).networkSynchronized(WandCore.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<WandCap>> WAND_CAP_APPEARANCE = register("wand_cap_appearance", builder -> builder.persistent(WandCap.CODEC).networkSynchronized(WandCap.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<WandGem>> WAND_GEM_APPEARANCE = register("wand_gem_appearance", builder -> builder.persistent(WandGem.CODEC).networkSynchronized(WandGem.STREAM_CODEC));
    
    private static <T> RegistryObject<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return DATA_COMPONENT_TYPES.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }
}
