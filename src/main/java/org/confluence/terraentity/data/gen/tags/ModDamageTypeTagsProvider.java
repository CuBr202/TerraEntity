package org.confluence.terraentity.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.init.TETags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static org.confluence.terraentity.TerraEntity.MODID;


public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, MODID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider holderLookup) {

        tag(DamageTypeTags.IS_PLAYER_ATTACK)
                .add(TETags.DamageTypes.SUMMONER)
                ;

        tag(DamageTypeTags.NO_KNOCKBACK)
                .add(TETags.DamageTypes.SUMMONER)
                ;

    }



}
