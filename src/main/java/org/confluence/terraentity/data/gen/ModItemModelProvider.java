package org.confluence.terraentity.data.gen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.confluence.terraentity.init.ModItems;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.confluence.terraentity.TerraEntity.MODID;


public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    private Map<DeferredRegister.Items,List<String>> createDir(DeferredRegister.Items reg, String... packPaths) {
        return Map.of(reg, Arrays.stream(packPaths).toList());
    }
    private void genModels(List<Map<DeferredRegister.Items,List<String>>> list, String parent){
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

        List<Map<DeferredRegister.Items,List<String>>> customModels = List.of(
                createDir(ModItems.SPAWN_EGGS,"egg/")
        );
        genModels(customModels,"minecraft:item/template_spawn_egg");


    }

}
