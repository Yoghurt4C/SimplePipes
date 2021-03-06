package alexiil.mc.mod.pipes.client.render;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.mod.pipes.blocks.TileTank;
import alexiil.mc.mod.pipes.util.FluidSmoother.FluidStackInterp;

public class TankBlockEntityRenderer extends BlockEntityRenderer<TileTank> {
    @Override
    public void render(TileTank tile, double x, double y, double z, float partialTicks, int destroyStage) {
        FluidStackInterp forRender = tile.getFluidForRender(partialTicks);
        if (forRender == null) {
            return;
        }

        // gl state setup
        GuiLighting.disable();
        MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

        // buffer setup

        FluidVolume fluid = forRender.fluid;
        int blocklight = fluid.fluidKey == FluidKeys.LAVA ? 15 : 0;
        int combinedLight = tile.getWorld().getLightmapIndex(tile.getPos(), blocklight);
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, combinedLight & 0xFFFF, (combinedLight >> 16) & 0xFFFF);

        List<FluidRenderFace> faces = new ArrayList<>();

        double x0 = 0.126;
        double y0 = 0.001;
        double z0 = 0.126;
        double x1 = 0.874;
        double y1 = 0.001 + (12 / 16.0 - 0.002) * forRender.amount / tile.fluidInv.tankCapacity;
        double z1 = 0.874;

        EnumSet<Direction> sides = EnumSet.allOf(Direction.class);
        FluidRenderFace.appendCuboid(x0, y0, z0, x1, y1, z1, 16, sides, faces);
        forRender.fluid.getRenderer().render(forRender.fluid, faces, x, y, z);

        // gl state finish
        GuiLighting.enable();
    }

    private static boolean isFullyConnected(TileTank thisTank, Direction face, float partialTicks) {
        BlockPos pos = thisTank.getPos().offset(face);
        BlockEntity oTile = thisTank.getWorld().getBlockEntity(pos);
        if (oTile instanceof TileTank) {
            TileTank oTank = (TileTank) oTile;
            // if (!TileTank.canTanksConnect(thisTank, oTank, face)) {
            // return false;
            // }
            FluidStackInterp forRender = oTank.getFluidForRender(partialTicks);
            if (forRender == null) {
                return false;
            }
            FluidVolume fluid = forRender.fluid;
            if (fluid == null || forRender.amount <= 0) {
                return false;
            } else if (thisTank.getFluidForRender(partialTicks) == null
                || !fluid.equals(thisTank.getFluidForRender(partialTicks).fluid)) {
                return false;
            }
            // if (fluid.getFluid().isGaseous(fluid)) {
            // face = face.getOpposite();
            // }
            return forRender.amount >= oTank.fluidInv.getMaxAmount(0) || face == Direction.UP;
        } else {
            return false;
        }
    }
}
