package org.confluence.terraentity.mixinauxiliary;

import net.minecraft.client.gui.components.LerpingBossEvent;

import java.util.Map;
import java.util.UUID;

public interface IBossHealthOverlay {
    Map<UUID, LerpingBossEvent> terra_entity$getEvents();
}
