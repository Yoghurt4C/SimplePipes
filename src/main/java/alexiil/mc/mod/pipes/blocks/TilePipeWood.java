/*
 * Copyright (c) 2019 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */
package alexiil.mc.mod.pipes.blocks;

import java.util.function.Function;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.Direction;

import alexiil.mc.lib.attributes.item.impl.EmptyItemExtractable;

public abstract class TilePipeWood extends TilePipeSided {

    private boolean lastRecv = true;

    public TilePipeWood(BlockEntityType<?> type, BlockPipe pipeBlock, Function<TilePipe, PipeFlow> flowConstructor) {
        super(type, pipeBlock, flowConstructor);
    }

    @Override
    protected boolean canConnect(Direction dir) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (world.isClient) {
            return;
        }
        Direction dir = currentDirection();
        if (dir == null) {
            return;
        }

        if (world.isReceivingRedstonePower(getPos())) {
            if (!lastRecv) {
                lastRecv = true;
                tryExtract(dir);
            }
        } else {
            lastRecv = false;
        }
    }

    protected abstract void tryExtract(Direction dir);
}
