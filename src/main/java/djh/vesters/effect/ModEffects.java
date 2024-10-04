package djh.vesters.effect;

import djh.vesters.Vesters;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static final StatusEffect EXPLOSION_COUNTDOWN = new ExplosionCountdownEffect();
    public static void registerModEffects(){
        Vesters.LOGGER.info("Registering Status Effects for "+Vesters.MOD_ID);

        Registry.register(Registries.STATUS_EFFECT, new Identifier(Vesters.MOD_ID, "explosion_countdown"), EXPLOSION_COUNTDOWN);
    }
}
