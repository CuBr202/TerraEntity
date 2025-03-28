package org.confluence.terraentity.data.gen.recipe;


import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.confluence.terraentity.init.item.TESummonItems;
import org.confluence.terraentity.init.item.TEWhipItems;


import java.util.concurrent.CompletableFuture;


public class TERecipeProvider extends RecipeProvider {

    public TERecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TESummonItems.HORNET_STAFF.get())
                .pattern("BAB")
                .pattern(" C ")
                .pattern(" C ")
                .define('A', Items.BEE_SPAWN_EGG)
                .define('B', Items.HONEY_BLOCK)
                .define('C', ItemTags.FLOWERS)
                .unlockedBy("has_bee_spawn_egg",has(Items.BEE_SPAWN_EGG))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEWhipItems.LEATHER_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.LEATHER)
                .unlockedBy("has_leather",has(Items.LEATHER))
                .save(recipeOutput);

//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEItems.BAMBOO_WHIP.get())
//                .pattern("  A")
//                .pattern("AAA")
//                .pattern("A  ")
//                .define('A', Items.BAMBOO)
//                .unlockedBy("has_bamboo",has(Items.BAMBOO))
//                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEWhipItems.AMBER_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.HONEY_BLOCK)
                .unlockedBy("has_honey_block",has(Items.HONEY_BLOCK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEWhipItems.AMETHYST_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.AMETHYST_CLUSTER)
                .unlockedBy("has_amethyst_cluster",has(Items.AMETHYST_CLUSTER))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEWhipItems.DIAMOND_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.DIAMOND)
                .unlockedBy("has_diamond",has(Items.DIAMOND))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEWhipItems.EMERALD_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.EMERALD)
                .unlockedBy("has_emerald",has(Items.EMERALD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEWhipItems.RUBY_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.REDSTONE_BLOCK)
                .unlockedBy("has_redstone_block",has(Items.REDSTONE_BLOCK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEWhipItems.SAPPHIRE_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.LAPIS_BLOCK)
                .unlockedBy("has_lapis_block",has(Items.LAPIS_BLOCK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TEWhipItems.TOPAZ_WHIP.get())
                .pattern("  A")
                .pattern("AAA")
                .pattern("A  ")
                .define('A', Items.GOLD_INGOT)
                .unlockedBy("has_gold_ingot",has(Items.GOLD_INGOT))
                .save(recipeOutput);
    }
}
