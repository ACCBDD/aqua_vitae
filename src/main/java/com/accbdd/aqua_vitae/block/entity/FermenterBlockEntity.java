package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.component.FermentingPropertiesComponent;
import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.registry.ModFluids;
import com.accbdd.aqua_vitae.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;

public class FermenterBlockEntity extends BaseSingleFluidTankEntity {
    public static final int CAPACITY = 8000;
    public static final int CYCLE_LENGTH = 20;

    private int cycleProgress;

    public FermenterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FERMENTER.get(), pos, blockState, CAPACITY);
    }

    public void tickServer() {
        if (cycleProgress++ > CYCLE_LENGTH) {
            cycleProgress = 0;
            FluidStack fluid = getFluid();
            if (fluid.isEmpty())
                return;
            if (getBlockState().getValue(BlockStateProperties.CRAFTING)) {
                if (fluid.has(ModComponents.FERMENTING_PROPERTIES)) {
                    setFluid(FluidUtils.ferment(fluid));
                } else if (fluid.has(ModComponents.PRECURSOR_PROPERTIES)) {
                    PrecursorPropertiesComponent precursor = fluid.get(ModComponents.PRECURSOR_PROPERTIES);
                    FluidStack alcohol = new FluidStack(ModFluids.ALCOHOL, fluid.getAmount(), fluid.getComponentsPatch());
                    alcohol.set(ModComponents.FERMENTING_PROPERTIES, new FermentingPropertiesComponent(0, precursor.flavors(), precursor.properties()));
                    alcohol.set(ModComponents.ALCOHOL_PROPERTIES, new AlcoholPropertiesComponent(precursor.properties().color(), 0, 0, precursor.flavors(), precursor.ingredients()));
                    alcohol.remove(ModComponents.PRECURSOR_PROPERTIES);
                    setFluid(alcohol);
                }
            } else if (fluid.has(ModComponents.FERMENTING_PROPERTIES)) {
                setFluid(FluidUtils.stress(fluid));
            }
        }
    }
}
