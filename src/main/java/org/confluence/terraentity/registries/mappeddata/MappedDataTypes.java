package org.confluence.terraentity.registries.mappeddata;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.mappeddata.BossSkillMapDatas;
import org.confluence.terraentity.registries.TERegistries;

public class MappedDataTypes {
    public static final DeferredRegister<MappedDataType> TYPES = DeferredRegister.create(TERegistries.MAPPED_DATAS, TerraEntity.MODID);

    public static DeferredHolder<MappedDataType, MappedDataType> BOSS_SKILL_MAP_DATAS = TYPES.register("boss_skill_params",
            BossSkillMapDatas::buildType);

}
