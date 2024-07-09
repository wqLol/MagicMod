package net.aarav.magicmod.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import static java.lang.Math.round;

public class ObamiumItem extends Item {

    public ObamiumItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        double radians = Math.toRadians(round(pPlayer.getYRot()));
        pPlayer.lerpMotion(7.0f * -Math.sin(radians),-2.0f*Math.toRadians(round(pPlayer.getXRot())),7.0f* Math.cos(radians));
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
