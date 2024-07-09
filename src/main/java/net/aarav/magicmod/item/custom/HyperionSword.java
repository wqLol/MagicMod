package net.aarav.magicmod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.http.cookie.Cookie;

import java.util.List;

public class HyperionSword extends SwordItem {

    private static int TPDISTANCE=10;
    private static int DAMAGERAD = 5;
    private static float DMGAMT = 120f;

    public HyperionSword(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        TeleportForward(pLevel,pPlayer);

        List<LivingEntity> entities = pLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, pPlayer, new AABB(pPlayer.blockPosition()).inflate(DAMAGERAD));
        boolean dmged = false;
        for (LivingEntity entity : entities) {
            DamageSource dmgSource = pLevel.damageSources().generic();
            entity.hurt(dmgSource, DMGAMT);
            dmged=true;
        }
        if (dmged || (pPlayer.onGround() && pPlayer.getXRot() > 50)){
            pLevel.playSound(pPlayer,pPlayer.getX(),pPlayer.getY(),pPlayer.getZ(), SoundEvents.GENERIC_EXPLODE.get(), SoundSource.PLAYERS,0.7f,1.5f);
            pLevel.addParticle(ParticleTypes.EXPLOSION, pPlayer.getX(), pPlayer.getY() + 0.5, pPlayer.getZ(), 4.0, 4.0, 4.0);
        }


        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public void  TeleportForward(Level pLevel,Player pPlayer){
//        pPlayer.setPos(pPlayer.position().add(pPlayer.getForward()));
        Vec3 vec3;
        Vec3i bp =  Vec3i.ZERO;



        for (int i =1; i <= TPDISTANCE; i++) {
            vec3 = (pPlayer.position().add(pPlayer.getForward().scale(i)));
            bp = new Vec3i(((int)vec3.x),((int)vec3.y),((int)vec3.z));
            if (i==TPDISTANCE){
                pPlayer.setPos((pPlayer.position().add(pPlayer.getForward().scale(i-1))));
                pPlayer.setPos(pPlayer.position().x,bp.getY(), pPlayer.position().z);
                break;
            }

            if (!pLevel.isEmptyBlock(new BlockPos(bp))) {

                if (i == 1){
                    break;
                }
                pPlayer.setPos((pPlayer.position().add(pPlayer.getForward().scale(i-1))));
                pPlayer.setPos(pPlayer.position().x,bp.getY(), pPlayer.position().z);

                break;
            };
        }
        vec3 = pPlayer.position();
        bp = new Vec3i(((int)vec3.x),((int)vec3.y),((int)vec3.z));
        BlockPos blockPos = new BlockPos(bp);
        while (!pLevel.isEmptyBlock(blockPos)){

            vec3 = pPlayer.position();
            bp = new Vec3i(((int)vec3.x),((int)vec3.y),((int)vec3.z));
            blockPos = new BlockPos(bp);
            pPlayer.setPos(pPlayer.position().add(0,0.500901,0));
        }
        pLevel.playSound(pPlayer,pPlayer.getX(),pPlayer.getY(),pPlayer.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS,0.4f,1.0f);



    }
}
