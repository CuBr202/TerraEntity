package org.confluence.terraentity.entity.npc.mood;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.registries.TERegistries;

public class MoodInfos {
    public static DeferredRegister<MoodInfo> TYPES = DeferredRegister.create(TERegistries.MoodInfos.KEY, TerraEntity.MODID);


    public static final DeferredHolder<MoodInfo,MoodInfo> GUILD1 = TYPES.register("guild1", ()->new MoodInfo(TENpcEntities.GOBLIN_TINKERER, "guild1.2", Mood.HATE));
    public static final DeferredHolder<MoodInfo,MoodInfo> GUILD2 = TYPES.register("guild2", ()->new MoodInfo(TENpcEntities.MERCHANT, "guild2.2", Mood.LOVER));




}
