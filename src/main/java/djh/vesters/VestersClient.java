package djh.vesters;

import djh.vesters.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VestersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.MOLOTOV_PROJECTILE, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.PBOMB_PROJECTILE, FlyingItemEntityRenderer::new);
	}

}