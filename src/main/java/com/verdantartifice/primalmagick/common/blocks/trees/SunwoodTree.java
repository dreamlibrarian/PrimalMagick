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
 * Definition for a sunwood tree.  Used by sunwood saplings to spawn the sunwood tree worldgen feature.
 * They fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodTree extends AbstractPhasingTree {
    @Override
    protected Map<TimePhase, ConfiguredFeature<TreeConfiguration, ?>> getTreeFeaturesByPhase(Random rand, boolean largeHive) {
        return Util.make(new HashMap<>(), (map) -> {
            map.put(TimePhase.FULL, TreeFeaturesPM.TREE_SUNWOOD_FULL);
            map.put(TimePhase.WAXING, TreeFeaturesPM.TREE_SUNWOOD_WAXING);
            map.put(TimePhase.WANING, TreeFeaturesPM.TREE_SUNWOOD_WANING);
            map.put(TimePhase.FADED, TreeFeaturesPM.TREE_SUNWOOD_FADED);
        });
    }

    @Override
    protected TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getMoonPhase(world);
    }
}
