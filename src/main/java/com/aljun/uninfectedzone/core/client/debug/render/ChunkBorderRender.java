package com.aljun.uninfectedzone.core.client.debug.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChunkBorderRender {
    private final Minecraft minecraft;

    public ChunkBorderRender(Minecraft p_113356_) {
        this.minecraft = p_113356_;
    }

    public void render(double x, double y, double z) {

        int cellBorder = FastColor.ARGB32.color(255, 0, 155, 155);
        int yellow = FastColor.ARGB32.color(255, 255, 255, 0);

        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Entity entity = this.minecraft.gameRenderer.getMainCamera().getEntity();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        double d0 = 0;
        if (this.minecraft.level != null) {
            d0 = (double) this.minecraft.level.getMinBuildHeight() - y;
        }
        double d1 = 0;
        if (this.minecraft.level != null) {
            d1 = (double) this.minecraft.level.getMaxBuildHeight() - y;
        }
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        ChunkPos chunkpos = entity.chunkPosition();


        double d2 = (double) chunkpos.getMinBlockX() - x;
        double d3 = (double) chunkpos.getMinBlockZ() - z;
        RenderSystem.lineWidth(1.0F);
        bufferbuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        //红色竖线
        /*for (int i = -16; i <= 32; i += 16) {
            for (int j = -16; j <= 32; j += 16) {
                bufferbuilder.vertex(d2 + (double) i, d0, d3 + (double) j).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
                bufferbuilder.vertex(d2 + (double) i, d0, d3 + (double) j).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                bufferbuilder.vertex(d2 + (double) i, d1, d3 + (double) j).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                bufferbuilder.vertex(d2 + (double) i, d1, d3 + (double) j).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
            }
        }*/

        //南北纵线
        /*for (int l = 2; l < 16; l += 2) {
            int i2 = l % 4 == 0 ? cellBorder : yellow;
            bufferbuilder.vertex(d2 + (double) l, d0, d3).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(d2 + (double) l, d0, d3).color(i2).endVertex();
            bufferbuilder.vertex(d2 + (double) l, d1, d3).color(i2).endVertex();
            bufferbuilder.vertex(d2 + (double) l, d1, d3).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(d2 + (double) l, d0, d3 + 16.0D).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(d2 + (double) l, d0, d3 + 16.0D).color(i2).endVertex();
            bufferbuilder.vertex(d2 + (double) l, d1, d3 + 16.0D).color(i2).endVertex();
            bufferbuilder.vertex(d2 + (double) l, d1, d3 + 16.0D).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
        }*/

        //东西纵线
        /*for (int i1 = 2; i1 < 16; i1 += 2) {
            int j2 = i1 % 4 == 0 ? cellBorder : yellow;
            bufferbuilder.vertex(d2, d0, d3 + (double) i1).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(d2, d0, d3 + (double) i1).color(j2).endVertex();
            bufferbuilder.vertex(d2, d1, d3 + (double) i1).color(j2).endVertex();
            bufferbuilder.vertex(d2, d1, d3 + (double) i1).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(d2 + 16.0D, d0, d3 + (double) i1).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(d2 + 16.0D, d0, d3 + (double) i1).color(j2).endVertex();
            bufferbuilder.vertex(d2 + 16.0D, d1, d3 + (double) i1).color(j2).endVertex();
            bufferbuilder.vertex(d2 + 16.0D, d1, d3 + (double) i1).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
        }*/

        //东西南北横线
        /*for (int j1 = this.minecraft.level.getMinBuildHeight(); j1 <= this.minecraft.level.getMaxBuildHeight(); j1 += 2) {
            double d4 = (double) j1 - y;
            int k = j1 % 8 == 0 ? cellBorder : yellow;
            bufferbuilder.vertex(d2, d4, d3).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(d2, d4, d3).color(k).endVertex();
            bufferbuilder.vertex(d2, d4, d3 + 16.0D).color(k).endVertex();
            bufferbuilder.vertex(d2 + 16.0D, d4, d3 + 16.0D).color(k).endVertex();
            bufferbuilder.vertex(d2 + 16.0D, d4, d3).color(k).endVertex();
            bufferbuilder.vertex(d2, d4, d3).color(k).endVertex();
            bufferbuilder.vertex(d2, d4, d3).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
        }*/

        tesselator.end();
        RenderSystem.lineWidth(2.0F);
        bufferbuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        for (int k1 = 0; k1 <= 16; k1 += 16) {
            for (int k2 = 0; k2 <= 16; k2 += 16) {
                bufferbuilder.vertex(d2 + (double) k1, d0, d3 + (double) k2).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
                bufferbuilder.vertex(d2 + (double) k1, d0, d3 + (double) k2).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
                bufferbuilder.vertex(d2 + (double) k1, d1, d3 + (double) k2).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
                bufferbuilder.vertex(d2 + (double) k1, d1, d3 + (double) k2).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
            }
        }

        for (int l1 = this.minecraft.level.getMinBuildHeight(); l1 <= this.minecraft.level.getMaxBuildHeight(); l1 += 16) {
            double d5 = (double) l1 - y;
            bufferbuilder.vertex(d2, d5, d3).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(d2, d5, d3).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(d2, d5, d3 + 16.0D).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(d2 + 16.0D, d5, d3 + 16.0D).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(d2 + 16.0D, d5, d3).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(d2, d5, d3).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(d2, d5, d3).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
        }

        tesselator.end();
        RenderSystem.lineWidth(1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
    }
}
