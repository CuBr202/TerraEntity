package org.confluence.terraentity.data.mappeddata;

import org.confluence.terraentity.entity.boss.EyeOfCthulhu;
import org.confluence.terraentity.entity.boss.hillofflesh.HillOfFlesh;
import org.confluence.terraentity.registries.mappeddata.MappedData;
import org.confluence.terraentity.registries.mappeddata.MappedDataType;
import org.confluence.terraentity.registries.mappeddata.MappedKey;
import org.confluence.terraentity.registries.mappeddata.MappedDataTypes;

import java.util.Map;

public final class BossSkillMapDatas extends MappedData {

    BossSkillMapDatas(Map<MappedKey<?>, Object> data, String comment) {
        super(data, comment);
    }

    @Override
    public MappedDataType getType() {
        return MappedDataTypes.BOSS_SKILL_MAP_DATAS.get();
    }

    static MappedDataType.Builder builder = MappedDataType.builder()
            .setComment("This file contains all default values for boss parameters. This can be customized by data packs. For lists containing 4 elements, " +
                    "they may express the values different from the difficulty levels: (classic, expert, master, ftw), equivalent to (simple, normal, hard, ftw) .");

    public static MappedKey<EyeOfCthulhu.SkillParams> EYE_OF_CTHULHU_PARAMS = builder
            .registerCodec("eye_of_cthulhu_params", EyeOfCthulhu.SkillParams.CODEC)
            .withDefaultValue(EyeOfCthulhu.SkillParams::getDefaultParams)
//            .withOnReload(data->System.out.println(data.toString()))
            ;
    public static MappedKey<HillOfFlesh.SkillParams> HILL_OF_FLESH_PARAMS = builder
            .registerCodec("hill_of_flesh_params", HillOfFlesh.SkillParams.CODEC)
            .withDefaultValue(HillOfFlesh.SkillParams::getDefaultParams);


    public static MappedDataType buildType() {
        return builder.build(BossSkillMapDatas::new);
    }

}
