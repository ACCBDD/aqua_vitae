package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.recipe.IngredientColor;
import com.accbdd.aqua_vitae.registry.Keg;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class KegBlockEntity extends BaseAgingBlockEntity {
    private static final String KEG_TAG = "keg";
    private ResourceLocation kegLoc;

    public KegBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.KEG.get(),
                pos,
                blockState,
                4000,
                List.of(),
                new IngredientColor(0, 0));
        this.kegLoc = null;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains(KEG_TAG) && getLevel() != null) {
            Keg keg = getLevel().registryAccess().registryOrThrow(AquaVitae.KEG_REGISTRY).getOptional(ResourceLocation.parse(tag.getString(KEG_TAG))).orElse(Keg.NULL);
            setFlavors(keg.flavorAdds());
            setColor(keg.color());
        }
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (kegLoc != null)
            tag.putString(KEG_TAG, kegLoc.toString());
    }

    public ResourceLocation getKegLoc() {
        return kegLoc;
    }

    public void setKegLoc(ResourceLocation kegLoc) {
        this.kegLoc = kegLoc;
        if (getLevel() != null) {
            Keg keg = getLevel().registryAccess().registryOrThrow(AquaVitae.KEG_REGISTRY).getOptional(kegLoc).orElse(Keg.NULL);
            setFlavors(keg.flavorAdds());
            setColor(keg.color());
        }
    }
}
