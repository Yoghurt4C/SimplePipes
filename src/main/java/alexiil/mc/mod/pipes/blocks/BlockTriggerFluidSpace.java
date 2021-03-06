package alexiil.mc.mod.pipes.blocks;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import alexiil.mc.mod.pipes.container.SimplePipeContainers;

public class BlockTriggerFluidSpace extends BlockTriggerFluidInv {

    public BlockTriggerFluidSpace(Block.Settings settings) {
        super(settings);
    }

    @Override
    public TileTrigger createBlockEntity(BlockView view) {
        return new TileTriggerFluidSpace();
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
        BlockHitResult hit) {
        BlockEntity be = world.getBlockEntity(pos);

        if (be instanceof TileTriggerFluidSpace) {
            if (world.isClient) {
                return true;
            }
            ContainerProviderRegistry.INSTANCE.openContainer(SimplePipeContainers.TRIGGER_FLUID_INV_SPACE, player,
                (buffer) -> {
                    buffer.writeBlockPos(pos);
                });
            return true;
        }

        return super.activate(state, world, pos, player, hand, hit);
    }
}
