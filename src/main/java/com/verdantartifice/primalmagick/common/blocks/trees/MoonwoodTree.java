package com.verdantartifice.primalmagick.common.blocks.trees;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.common.worldgen.features.TreeFeaturesPM;

import net.minecraft.Util;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

/**
 * Definition for a moonwood tree.  Used by moonwood saplings to spawn the moonwood tree worldgen feature.
 * They fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodTree extends AbstractPhasingTree {
    @Override
    protected Map<TimePhase, ConfiguredFeature<TreeConfiguration, ?>> getTreeFeaturesByPhase(Random rand, boolean largeHive) {
        return Util.make(new HashMap<>(), (map) -> {
            map.put(TimePhase.FULL, TreeFeaturesPM.TREE_MOONWOOD_FULL);
            map.put(TimePhase.WAXING, TreeFeaturesPM.TREE_MOONWOOD_WAXING);
            map.put(TimePhase.WANING, TreeFeaturesPM.TREE_MOONWOOD_WANING);
            map.put(TimePhase.FADED, TreeFeaturesPM.TREE_MOONWOOD_FADED);
        });
    }

    @Override
    protected TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getMoonPhase(world);
    }
}
