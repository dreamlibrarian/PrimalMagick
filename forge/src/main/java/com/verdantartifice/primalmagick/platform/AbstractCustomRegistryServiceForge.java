package com.verdantartifice.primalmagick.platform;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemForge;
import com.verdantartifice.primalmagick.platform.services.IRegistryService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Base implementation of a generic registry service for Forge. Provides basic access to custom registry
 * operations as needed by common code.
 *
 * @param <R> the type of object stored in the encapsulated Forge registry
 * @author Daedalus4096
 */
abstract class AbstractCustomRegistryServiceForge<R> implements IRegistryService<R> {
    protected abstract Supplier<DeferredRegister<R>> getDeferredRegisterSupplier();
    protected abstract Supplier<IForgeRegistry<R>> getRegistry();

    @Override
    public <T extends R> IRegistryItem<R, T> register(String name, Supplier<T> supplier) {
        return new RegistryItemForge<>(this.getDeferredRegisterSupplier().get().register(name, supplier));
    }

    @Override
    public @Nullable R get(ResourceLocation id) {
        return this.getRegistry().get().getValue(id);
    }

    @Override
    public boolean containsKey(ResourceLocation id) {
        return this.getRegistry().get().containsKey(id);
    }

    @Override
    public Codec<R> codec() {
        return ResourceLocation.CODEC.flatXmap(loc -> {
            return Optional.ofNullable(this.getRegistry().get().getValue(loc)).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry key in " + this.getRegistry().get().getRegistryKey() + ": " + loc;
                });
            });
        }, element -> {
            return this.getRegistry().get().getResourceKey(element).map(ResourceKey::location).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry element in " + this.getRegistry().get().getRegistryKey() + ": " + element;
                });
            });
        });
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, R> registryFriendlyStreamCodec() {
        return new StreamCodec<>() {
            @Override
            public R decode(RegistryFriendlyByteBuf pBuffer) {
                ResourceLocation id = ResourceLocation.parse(Utf8String.read(pBuffer, 32767));
                return AbstractCustomRegistryServiceForge.this.getRegistry().get().getValue(id);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf pBuffer, R pValue) {
                ResourceLocation id = AbstractCustomRegistryServiceForge.this.getRegistry().get().getKey(pValue);
                Utf8String.write(pBuffer, id.toString(), 32767);
            }
        };
    }

    @Override
    public StreamCodec<FriendlyByteBuf, R> friendlyStreamCodec() {
        return new StreamCodec<>() {
            @Override
            public R decode(FriendlyByteBuf pBuffer) {
                ResourceLocation id = ResourceLocation.parse(Utf8String.read(pBuffer, 32767));
                return AbstractCustomRegistryServiceForge.this.getRegistry().get().getValue(id);
            }

            @Override
            public void encode(FriendlyByteBuf pBuffer, R pValue) {
                ResourceLocation id = AbstractCustomRegistryServiceForge.this.getRegistry().get().getKey(pValue);
                Utf8String.write(pBuffer, id.toString(), 32767);
            }
        };
    }
}
