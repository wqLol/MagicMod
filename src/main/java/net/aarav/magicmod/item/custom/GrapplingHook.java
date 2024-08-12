package net.aarav.magicmod.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static java.lang.Math.round;

public class GrapplingHook extends FishingRodItem
{
    public GrapplingHook(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        if (pPlayer.fishing != null){
            Vec3 pos = pPlayer.fishing.position().add(pPlayer.position().scale(-1));
            pPlayer.setDeltaMovement(pos.normalize().scale(5));

        }
        return super.use(pLevel, pPlayer, pHand);
    }
}
