package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire page for a given research discipline.
 * 
 * @author Daedalus4096
 */
public class DisciplineButton extends AbstractTopicButton {
    protected ResearchDiscipline discipline;

    public DisciplineButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, ResearchDiscipline discipline) {
        super(widthIn, heightIn, 123, 12, text, screen, new Handler());
        this.discipline = discipline;
    }
    
    public ResearchDiscipline getDiscipline() {
        return this.discipline;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof DisciplineButton) {
                DisciplineButton gdb = (DisciplineButton)button;
                
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(gdb.getScreen().getMenu().getTopic());
                
                // Set the new grimoire topic and open a new screen for it
                gdb.getScreen().getMenu().setTopic(gdb.getDiscipline());
                gdb.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    gdb.getScreen().getMenu(),
                    gdb.getScreen().getPlayerInventory(),
                    gdb.getScreen().getTitle()
                ));
            }
        }
    }
}
