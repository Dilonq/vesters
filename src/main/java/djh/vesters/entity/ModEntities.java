package djh.vesters.entity;

import djh.vesters.Vesters;
import djh.vesters.entity.custom.MolotovProjectileEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<MolotovProjectileEntity> MOLOTOV_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(Vesters.MOD_ID, "molotov_projectile"), FabricEntityTypeBuilder.<MolotovProjectileEntity>create(SpawnGroup.MISC, MolotovProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f,0.25f)).build());
}