package org.confluence.terraentity.init.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.block.FigureBlock;
import org.confluence.terraentity.init.TEBlocks;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;

import java.util.function.Supplier;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEFigureBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);


    public static final RegistryObject<FigureBlock> FIGURE = register("figure", TEMonsterEntities.NYMPH.getId());

    public static final RegistryObject<FigureBlock> FIGURE2= register("figure2", TEMonsterEntities.BLOOD_ZOMBIE.getId());

    public static final RegistryObject<FigureBlock> FIGURE3= register("figure3",  TEBossEntities.EYE_OF_CTHULHU.getId(),0.5f);


    public static final Supplier<BlockEntityType<FigureBlock.FigureBlockEntity>> FIGURE_BLOCK_ENTITY =
            TEBlocks.BLOCK_ENTITIES.register("figure_block_entity", () -> BlockEntityType.Builder.of(FigureBlock.FigureBlockEntity::new,
                    FIGURE.get(), FIGURE2.get(), FIGURE3.get()
            ).build(null));

    private static RegistryObject<FigureBlock> register(String id, ResourceLocation entityId, float scale) {
        RegistryObject<FigureBlock> object = BLOCKS.register(id, ()-> new FigureBlock(entityId, scale,  BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().destroyTime(1.5F)));
        TEBlocks.BLOCKITEMS.register(id, ()->new BlockItem(object.get(), new Item.Properties()));
        return object;
    }

    private static RegistryObject<FigureBlock> register(String id, ResourceLocation entityId) {
        RegistryObject<FigureBlock> object = BLOCKS.register(id, ()-> new FigureBlock(entityId, BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().destroyTime(1.5F)));
        TEBlocks.BLOCKITEMS.register(id, ()->new BlockItem(object.get(), new Item.Properties()));
        return object;
    }


    public static void register(IEventBus bus) {
        TEFigureBlocks.BLOCKS.register(bus);

    }

}
