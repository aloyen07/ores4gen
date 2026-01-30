package ru.aloyenz.ores4gen.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.aloyenz.ores4gen.core.Generators;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {

    @Redirect(method = "flow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;"))
    public BlockState stoneGeneration(Block instance) {
        return Generators.getStoneGenerator().pickBlock().getDefaultState();
    }
}
