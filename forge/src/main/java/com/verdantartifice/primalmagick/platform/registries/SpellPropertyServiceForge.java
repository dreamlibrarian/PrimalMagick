package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyRegistration;
import com.verdantartifice.primalmagick.platform.services.ISpellPropertyService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the spell property registry service.
 *
 * @author Daedalus4096
 */
public class SpellPropertyServiceForge extends AbstractCustomRegistryServiceForge<SpellProperty> implements ISpellPropertyService {
    @Override
    protected Supplier<DeferredRegister<SpellProperty>> getDeferredRegisterSupplier() {
        return SpellPropertyRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<SpellProperty>> getRegistry() {
        return SpellPropertyRegistration.getRegistry();
    }
}
