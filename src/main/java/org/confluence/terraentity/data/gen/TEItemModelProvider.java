package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.DeferredRegister;

import org.confluence.terraentity.init.TEItems;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.confluence.terraentity.TerraEntity.MODID;


public class TEItemModelProvider extends ItemModelProvider {

    public TEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    private Map<DeferredRegister<Item>,List<String>> createDir(DeferredRegister<Item> reg, String... packPaths) {
        return Map.of(reg, Arrays.stream(packPaths).toList());
    }
    private void genModels(List<Map<DeferredRegister<Item>,List<String>>> list, String parent){
        list.forEach(mp-> mp.forEach((items, packPaths) -> {
            items.getEntries().forEach(item -> {
                String path = item.getId().getPath().toLowerCase();
                for(String ignored : packPaths){
                    try {
                        withExistingParent(path, parent);
                        break;
                    } catch (Exception e) { }
                }
            });
        }));
    }
    @Override
    protected void registerModels() {

        List<Map<DeferredRegister<Item>,List<String>>> customModels = List.of(
                createDir(TEItems.SPAWN_EGGS,"egg/")
        );
        genModels(customModels,"minecraft:item/template_spawn_egg");


    }

}
