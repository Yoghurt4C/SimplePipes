/*
 * Copyright (c) 2019 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */
package alexiil.mc.mod.pipes.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class BlockPipeSided extends BlockPipe {

    public BlockPipeSided(Settings settings) {
        super(settings);
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
        BlockHitResult hitResult) {
        if (player.getStackInHand(hand).isEmpty()) {
            if (world.isClient) {
                return true;
            }
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof TilePipeSided) {
                return ((TilePipeSided) be).attemptRotation();
            }
        }
        return super.activate(state, world, pos, player, hand, hitResult);
    }

    @Override
    public abstract TilePipeSided createBlockEntity(BlockView view);

}
