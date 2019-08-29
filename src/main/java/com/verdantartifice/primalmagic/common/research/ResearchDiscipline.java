package com.verdantartifice.primalmagic.common.research;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchDiscipline {
    protected String key;
    protected String unlockResearchKey;
    protected Map<String, ResearchEntry> entries = new HashMap<>();
    
    protected ResearchDiscipline(@Nonnull String key, @Nullable String unlockResearchKey) {
        this.key = key;
        this.unlockResearchKey = unlockResearchKey;
    }
    
    @Nullable
    public static ResearchDiscipline create(@Nullable String key, @Nullable String unlockResearchKey) {
        return (key == null) ? null : new ResearchDiscipline(key, unlockResearchKey);
    }
    
    @Nonnull
    public String getKey() {
        return this.key;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return "primalmagic.research_discipline." + this.key;
    }
    
    @Nullable
    public String getUnlockResearchKey() {
        return this.unlockResearchKey;
    }
    
    @Nullable
    public ResearchEntry getEntry(String key) {
        return this.entries.get(key);
    }
    
    @Nonnull
    public Collection<ResearchEntry> getEntries() {
        return Collections.unmodifiableCollection(this.entries.values());
    }
}
