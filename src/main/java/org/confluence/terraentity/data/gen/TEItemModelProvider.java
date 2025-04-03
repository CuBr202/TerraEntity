package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.TriConsumer;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.*;

import java.util.*;

import static org.confluence.terraentity.TerraEntity.MODID;


public class TEItemModelProvider extends ItemModelProvider {
    private static final ResourceLocation MISSING_ITEM = TerraEntity.space("item/missing");

    public TEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    private Map<DeferredRegister.Items,List<String>> createDir(DeferredRegister.Items reg, String... packPaths) {
        return Map.of(reg, Arrays.stream(packPaths).toList());
    }
    private void genModels(List<Map<DeferredRegister.Items,List<String>>> list, String parent, TriConsumer<String,String, String> appender){
        list.forEach(mp-> mp.forEach((items, packPaths) -> {
            items.getEntries().forEach(item -> {
                String path = item.getId().getPath().toLowerCase();
                for(String resourcePath : packPaths){
                        appender.accept(parent, resourcePath, path);
                }
            });
        }));
    }
    @Override
    protected void registerModels() {

        // spawn eggs
        genModels(List.of(
                createDir(TESpawnEggItems.ITEMS,"egg/")
        ),"item/generated", (parent, resourcePath, path) -> {
            try {
                withExistingParent(path, parent).texture("layer0", TerraEntity.asResource("item/" + resourcePath + path));
            } catch (Exception e) {
                withExistingParent(path, "minecraft:item/template_spawn_egg");
            }
        });

        // handed items
        genModels(List.of(
                createDir(TESummonItems.ITEMS,"summon/"),
                createDir(TEWhipItems.ITEMS,"whip/"),
                createDir(TEBoomerangItems.ITEMS,"boomerang/")
        ),"item/handheld", (parent, resourcePath, path) -> {
            try {
                withExistingParent(path, parent).texture("layer0", TerraEntity.asResource("item/" + resourcePath + path));
            } catch (Exception e) {
                withExistingParent(path, MISSING_ITEM);
                System.out.println("Failed to generate model for " + path + " in " + resourcePath);
            }
        });

        // general
        genModels(List.of(
                createDir(TERiddenItems.ITEMS,"rideable/")
        ),"item/generated", (parent, resourcePath, path) -> {
            try {
                withExistingParent(path, parent).texture("layer0", TerraEntity.asResource("item/" + resourcePath + path));
            } catch (Exception e) {
                withExistingParent(path, MISSING_ITEM);
                System.out.println("Failed to generate model for " + path + " in " + resourcePath);
            }
        });
    }
}
