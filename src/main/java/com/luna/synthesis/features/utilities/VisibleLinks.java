package com.luna.synthesis.features.utilities;

import java.util.regex.*;

import com.luna.synthesis.Synthesis;
import com.luna.synthesis.core.Config;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VisibleLinks {

    private final Config config = Synthesis.getInstance().getConfig();

    private final Pattern domainPattern = Pattern.compile("((https?)://)?[-a-zA-Z0-9+&@#/%?=~_|!:,;]+\\.[-a-zA-Z0-9+&@#/%=~_|]");
    //modified from the java regex of https://urlregex.com/
    //it'll still falsely trigger on `...com` but fuck it

    // Low priority so it's compatible with bridge
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!config.utilitiesVisibleLinks) return;
        /**
         * Suggestion #97 by Doppelclick#5993
         * make it so that visible links must include top level domains:
         * https://en.wikipedia.org/wiki/List_of_Internet_top-level_domains,
         * as currently a simple ... can trigger the feature
         */
        if (!domainPattern.matcher(event.message.getUnformattedText()).find()) return;
        if (event.type == 0 || event.type == 1) {
            for (IChatComponent iChatComponent : event.message.getSiblings()) {
                if (iChatComponent.getChatStyle().getChatClickEvent() != null) {
                    if (iChatComponent.getChatStyle().getChatClickEvent().getAction().equals(ClickEvent.Action.OPEN_URL)) {
                        iChatComponent.getChatStyle().setColor(EnumChatFormatting.AQUA).setUnderlined(true);
                    }
                }
            }
        }
    }
}
