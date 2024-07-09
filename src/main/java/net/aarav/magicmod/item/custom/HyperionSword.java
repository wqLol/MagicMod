package net.aarav.magicmod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.apache.http.cookie.Cookie;

public class HyperionSword extends SwordItem {

    private static int TPDISTANCE=30;
    private static int TPTICKCOOLDOWN=5;
    private static int EXPLOSIONTICKCOOLDOWN = 5;
    private static int HEALTICKCOOLDOWN = 40;

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


//        if(!pLevel.isEmptyBlock(blockPos)){
//            pPlayer.setPos(pPlayer.position().add(0,1,0));
//        }


//        pPlayer.sendSystemMessage(Component.literal("helluh"));
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



    }
}
