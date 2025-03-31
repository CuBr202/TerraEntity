package org.confluence.terraentity.client.entity.model;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.confluence.terraentity.TerraEntity;

public class EntityBlockModelRegister {

    public static ModelResourceLocation SNATCHER_LEAF = ModelResourceLocation.standalone(TerraEntity.space("item/entity/snatcher_leaf"));


    public static void register(ModelEvent.RegisterAdditional event) {
        event.register(SNATCHER_LEAF);
    }
}
