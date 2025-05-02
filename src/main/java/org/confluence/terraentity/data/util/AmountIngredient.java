package org.confluence.terraentity.data.util;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.TerraEntity;

public record AmountIngredient(Ingredient ingredient, int amount) {

    public static Codec<Ingredient> INGREDIENT_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<Ingredient, T>> decode(DynamicOps<T> dynamicOps, T t) {
            MapLike<T> map = dynamicOps.getMap(t).result().get();
            T i = map.get("item");
            Ingredient ing = null;
            if (i!= null) {
                var item = dynamicOps.getStringValue(i).result().get();
                ing = Ingredient.of(ForgeRegistries.ITEMS.getValue(TerraEntity.parse(item)));
            }else{
                T i2 = map.get("tag");
                if (i2!= null) {
                    var tag = TagKey.codec(Registries.ITEM).decode(dynamicOps, i2).result().get().getFirst();
                    ing = Ingredient.of(tag);
                }
            }
            if(ing != null){
                return DataResult.success(Pair.of(ing, t));
            }

            return DataResult.error(()-> t + " is not a valid ingredient");
        }

        @Override
        public <T> DataResult<T> encode(Ingredient ingredient, DynamicOps<T> dynamicOps, T t) {
            Ingredient.Value[] values = ingredient.values;
            var builder = dynamicOps.mapBuilder();
            for (Ingredient.Value value : values) {
                JsonObject obj = value.serialize();
                String key = obj.keySet().iterator().next();
                var v = obj.getAsJsonPrimitive(key);
                builder.add(key, dynamicOps.createString(v.getAsString()));
            }
            return builder.build(t);
        }
    };



//    public static final Ingredient EMPTY = new Ingredient(new AmountIngredient(Ingredient.EMPTY, 0));
    public static final MapCodec<AmountIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            INGREDIENT_CODEC.fieldOf("ingredient").forGetter(AmountIngredient::ingredient),
            ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(AmountIngredient::amount)
    ).apply(instance, AmountIngredient::new));


//    public static final StreamCodec<RegistryFriendlyByteBuf, AmountIngredient> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    public static AmountIngredient of(int amount, Ingredient ingredient) {
        return new AmountIngredient(ingredient, amount);
    }

    public static AmountIngredient of(int amount, ItemLike... items) {
        return new AmountIngredient(Ingredient.of(items), amount);
    }

    public static AmountIngredient of(int amount, ItemStack... stacks) {
        return new AmountIngredient(Ingredient.of(stacks), amount);
    }

    public static AmountIngredient of(int amount, TagKey<Item> tag) {
        return new AmountIngredient(Ingredient.of(tag), amount);
    }

}
