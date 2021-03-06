package alexiil.mc.mod.pipes.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.IAttributeBlock;
import alexiil.mc.lib.attributes.fluid.impl.EmptyFluidExtractable;

public class BlockPump extends BlockBase implements BlockEntityProvider, IAttributeBlock {

    public static final EnumProperty<Direction> FACING = Properties.FACING;

    public BlockPump(Block.Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.with(FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new TilePump();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
        Direction facing = state.get(FACING);
        if (to.getSearchDirection() == facing) {
            to.offer(EmptyFluidExtractable.SUPPLIER);
        }
    }
}
