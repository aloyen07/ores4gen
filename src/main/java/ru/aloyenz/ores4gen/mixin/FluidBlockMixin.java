package ru.aloyenz.ores4gen.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.aloyenz.ores4gen.core.Generators;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {

    // Obsidian or cobblestone generation redirection
    @Redirect(method = "receiveNeighborFluids", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;", ordinal = 0))
    public BlockState cobblestoneGenerationRedirect(Block instance) {
        if (instance.equals(Blocks.OBSIDIAN)) {
            return Generators.getObsidianGenerator().pickBlock().getDefaultState();
        }

        return Generators.getCobblestoneGenerator().pickBlock().getDefaultState();
    }

    @Redirect(method = "receiveNeighborFluids", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;", ordinal = 1))
    public BlockState basaltGenerationRedirect(Block instance) {
        return Generators.getBasaltGenerator().pickBlock().getDefaultState();
    }
}
