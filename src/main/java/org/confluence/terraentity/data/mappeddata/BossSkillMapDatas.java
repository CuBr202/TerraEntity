package org.confluence.terraentity.data.mappeddata;

import org.confluence.terraentity.entity.boss.EyeOfCthulhu;
import org.confluence.terraentity.registries.mappeddata.MappedData;
import org.confluence.terraentity.registries.mappeddata.MappedDataType;
import org.confluence.terraentity.registries.mappeddata.MappedKey;
import org.confluence.terraentity.registries.mappeddata.MappedDataTypes;

import java.util.Map;

public class BossSkillMapDatas extends MappedData {

    public BossSkillMapDatas(Map<MappedKey<?>, Object> data) {
        super(data);
    }
    @Override
    public MappedDataType getType() {
        return MappedDataTypes.BOSS_SKILL_MAP_DATAS.get();
    }



    public static MappedDataType.Builder builder = MappedDataType.builder();

    public static MappedKey<EyeOfCthulhu.SkillParams> EYE_OF_CTHULHU_SKILL = builder
            .registerCodec("eye_of_cthulhu_skill", EyeOfCthulhu.SkillParams.CODEC)
            .withDefaultValue(EyeOfCthulhu.SkillParams::getDefaultParams)
//            .withOnReload(data->System.out.println(data.toString()))
            ;




    public static MappedDataType buildType() {
        return builder.build(BossSkillMapDatas::new);
    }

}
