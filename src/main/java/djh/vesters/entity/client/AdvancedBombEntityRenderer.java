package djh.vesters.entity.client;

import djh.vesters.VestersClient;
import djh.vesters.block.custom.AdvancedBombBlock;
import djh.vesters.entity.custom.AdvancedBombEntity;
import djh.vesters.entity.models.AdvancedBombEntityModel;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.TntEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AdvancedBombEntityRenderer extends EntityRenderer<AdvancedBombEntity> {

    public AdvancedBombEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(AdvancedBombEntity entity) {
        return new Identifier("vesters", "textures/entity/advancedbomb/advancedbomb.png");
    }
}