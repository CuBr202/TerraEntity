package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.TriConsumer;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEItems;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.confluence.terraentity.TerraEntity.MODID;


public class TEItemModelProvider extends ItemModelProvider {
    private static final ResourceLocation MISSING_ITEM = TerraEntity.space("item/missing");

    public TEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    private Map<DeferredRegister.Items,List<String>> createDir(DeferredRegister.Items reg, String... packPaths) {
        return Map.of(reg, Arrays.stream(packPaths).toList());
    }
    private void genModels(List<Map<DeferredRegister.Items,List<String>>> list, String parent, TriConsumer<String,String, ModelBuilder<ItemModelBuilder>> appender){
        list.forEach(mp-> mp.forEach((items, packPaths) -> {
            items.getEntries().forEach(item -> {
                String path = item.getId().getPath().toLowerCase();
                for(String resourcePath : packPaths){
                    try {
                        ModelBuilder<ItemModelBuilder> builder = withExistingParent(path, parent);
                        appender.accept(path, resourcePath, builder);
                        break;
                    } catch (Exception e) {
                        withExistingParent(path, MISSING_ITEM);
                        System.out.println("Failed to generate model for " + item.getId() + " in " + resourcePath);
                    }
                }
            });
        }));
    }
    @Override
    protected void registerModels() {

        List<Map<DeferredRegister.Items,List<String>>> eggModels = List.of(
                createDir(TEItems.SPAWN_EGGS,"egg/")
        );
        genModels(eggModels,"item/generated", (path, resourcePath, builder) -> {

            builder.texture("layer0", TerraEntity.asResource("item/" + resourcePath + path));

        });


        List<Map<DeferredRegister.Items,List<String>>> customModels = List.of(
                createDir(TEItems.SUMMON_ITEMS,"")
        );
        genModels(customModels,"item/handheld", (path, resourcePath, builder) -> {
            builder.texture("layer0", TerraEntity.asResource("item/" + resourcePath + path));
        });


    }

}
