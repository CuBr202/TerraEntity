package org.confluence.terraentity.data.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Stream;

public class AmountIngredient extends AbstractIngredient {

    Ingredient ingredient;
    int amount;

    public AmountIngredient(ItemStack itemStack) {
        super(Stream.of(new Ingredient.ItemValue(itemStack)));
        ingredient = Ingredient.of(itemStack);
        this.amount = itemStack.getCount();
    }

    public AmountIngredient(Ingredient ingredient, int amount) {
        super(Arrays.stream(ingredient.getItems()).map(Ingredient.ItemValue::new));
        this.ingredient = ingredient;
        this.amount = amount;
    }

    protected AmountIngredient(Stream<? extends Value> pValues ,int amount) {
        super(pValues);
        ingredient = this;
        this.amount = amount;
    }

    AmountIngredient() {
        super(Stream.empty());
        ingredient = Ingredient.EMPTY;
        this.amount = 0;
    }

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

    public Integer amount() {
        return amount;
    }

    public Ingredient ingredient() {
        return ingredient;
    }
    public static ResourceLocation TYPE(){
        return TerraEntity.space("amount_ingredient");
    }

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

    public ItemStack[] getItemStacks(){
        return Arrays.stream(ingredient.getItems()).peek(i->i.setCount(amount)).toArray(ItemStack[]::new);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public @NotNull IIngredientSerializer<AmountIngredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", TYPE().toString());
        serialize(jsonObject, ingredient, amount);
        return jsonObject;
    }

    public static void serialize(JsonObject jsonObject, Ingredient ingredient, int amount) {
        JsonObject ings = new JsonObject();
        jsonObject.add("ingredient", ings);
        if( ingredient instanceof AmountIngredient am){
            am.toJsonIngredient().getAsJsonObject().asMap().forEach(ings::add);
        }else
            ingredient.toJson().getAsJsonObject().asMap().forEach(ings::add);
        jsonObject.addProperty("count", amount);
//        ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, itemStack).result().ifPresent(js->js.getAsJsonObject().asMap().forEach(jsonObject::add));
    }

    public JsonElement toJsonIngredient() {
        if (this.values.length == 1) {
            return this.values[0].serialize();
        } else {
            JsonArray jsonarray = new JsonArray();
            Value[] var2 = this.values;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Value ingredient$value = var2[var4];
                jsonarray.add(ingredient$value.serialize());
            }

            return jsonarray;
        }
    }

    public static AmountIngredient fromJson(JsonObject json, String name) {
        return AmountIngredient.Serializer.INSTANCE.parse(GsonHelper.convertToJsonObject(GsonHelper.getNonNull(json,
                name).getAsJsonObject(), TYPE().toString()));
    }

    public static AmountIngredient fromJson(JsonObject json) {
        return AmountIngredient.Serializer.INSTANCE.parse(json);
    }

    public static AmountIngredient fromNetwork(FriendlyByteBuf buffer) {
        return Serializer.INSTANCE.parse(buffer);
    }


    public static class Serializer implements IIngredientSerializer<AmountIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public @NotNull AmountIngredient parse(FriendlyByteBuf buffer) {
            return new AmountIngredient(buffer.readItem());
        }

        @Override
        public @NotNull AmountIngredient parse(@NotNull JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            int amount = json.get("count").getAsInt();
            return new AmountIngredient(ingredient, amount);
        }

        @Override
        public void write(FriendlyByteBuf buffer, AmountIngredient ingredient) {
//            buffer.writeItem(ingredient.itemStack);
            ingredient.ingredient.toNetwork(buffer);
            buffer.writeInt(ingredient.amount);
        }
    }
}
