package org.confluence.terraentity.data.component;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.registries.datacomponent.IDataComponentType;

import java.util.function.Supplier;

/**
 * 不可破坏组件
 */
public record Unbreakable(boolean showText) implements IDataComponentType<Unbreakable> {

    public static final Supplier<Codec<Unbreakable>> CODEC = ()->
            Codec.BOOL.xmap(Unbreakable::new, Unbreakable::showText);


    public static Unbreakable of(boolean showText) {
        return new Unbreakable(showText);
    }

    public void writeToNBT(CompoundTag tag){
        tag.putBoolean("Unbreakable", true);
    }

    @Override
    public Codec<Unbreakable> codec() {
        return CODEC.get();
    }


//    public static EffectStrategyComponent of(EffectStrategy effect) {
//        return new EffectStrategyComponent(List.of(effect.getProvider()));
//    }

}
