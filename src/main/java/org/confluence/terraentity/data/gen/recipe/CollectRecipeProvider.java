package org.confluence.terraentity.data.gen.recipe;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CollectRecipeProvider extends RecipeProvider {
    private final List<AbstractRecipeProvider> subProviders;

    public CollectRecipeProvider(PackOutput output, Factory... factories) {
        super(output);
        this.subProviders = Arrays.stream(factories).map(factory -> factory.create(output)).toList();
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> output) {
        throw new UnsupportedOperationException();
    }


    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.allOf(subProviders.stream()
                .map(subProvider -> subProvider.run(output))
                .toArray(CompletableFuture[]::new));
    }

    @FunctionalInterface
    public interface Factory {
        AbstractRecipeProvider create(PackOutput output);
    }
}
