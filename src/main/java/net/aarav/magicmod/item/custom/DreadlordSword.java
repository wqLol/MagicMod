package net.aarav.magicmod.item.custom;


import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class DreadlordSword extends ProjectileWeaponItem {


    public DreadlordSword(Properties pProperties) {
        super(pProperties);
    }

    public static final int MAX_DRAW_DURATION = 20;
    public static final int DEFAULT_RANGE = 15;

    protected static List<ItemStack> cdraw(ItemStack pWeapon, ItemStack pAmmo, LivingEntity pShooter) {

            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pWeapon);
            int j = i == 0 ? 1 : 3;
            List<ItemStack> list = new ArrayList<>(j);
            ItemStack itemstack = pAmmo.copy();
            boolean infinite = pAmmo.getItem() instanceof ArrowItem arrow && arrow.isInfinite(pAmmo, pWeapon, pShooter);

            for (int k = 0; k < j; k++) {
                list.add(cuseAmmo(pWeapon, k == 0 ? pAmmo : itemstack, pShooter, k > 0 || infinite));
            }

            return list;

    }

    protected static ItemStack cuseAmmo(ItemStack pWeapon, ItemStack pAmmo, LivingEntity pShooter, boolean pIntangable) {
        return pAmmo;
//        boolean flag = !pIntangable && !hasInfiniteArrows(pWeapon, pAmmo, pShooter.hasInfiniteMaterials());
//        if (!flag) {
//            ItemStack itemstack1 = pAmmo.copyWithCount(1);
//            itemstack1.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
//            return itemstack1;
//        } else {
////            ItemStack itemstack = pAmmo.split(1);
//            return pAmmo;
//        }
    }


    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            ItemStack itemstack = player.getProjectile(pStack);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(pStack) - pTimeLeft;

                i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, true);
                if (i < 0) return;

                float f = getPowerForTime(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = cdraw(pStack, itemstack, player);
                    if (!pLevel.isClientSide() && !list.isEmpty()) {
//                        pEntityLiving.sendSystemMessage(Component.literal(draw(pStack, itemstack, player).toString()));
                        this.shoot(pLevel, player, player.getUsedItemHand(), pStack, list, f * 5.0F, 1.0F, f == 1.0F, null);
                    }

                    pLevel.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.WITHER_SHOOT,
                            SoundSource.PLAYERS,
                            0.4F,
                            1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    protected void shootProjectile(
            LivingEntity pShooter, Projectile pProjectile, int pIndex, float pVelocity, float pInaccuracy, float pAngle, @Nullable LivingEntity pTarget
    ) {

        pProjectile.shootFromRotation(pShooter, pShooter.getXRot(), pShooter.getYRot() + pAngle, 0.0F, pVelocity, 0.0f);
    }

    public static float getPowerForTime(int pCharge) {


        return 1.0f;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        boolean flag = !pPlayer.getProjectile(itemstack).isEmpty();
        var ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, flag);
        if (ret != null) return ret;
        if (!pPlayer.hasInfiniteMaterials() && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return  p_43017_ -> p_43017_.is(ItemTags.SKULLS);
//        return null;
    }


    @Override
    public int getDefaultProjectileRange() {
        return 125;

    }

    @Override
    protected void shoot(Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pWeapon, List<ItemStack> pProjectileItems, float pVelocity, float pInaccuracy, boolean pIsCrit, @org.jetbrains.annotations.Nullable LivingEntity pTarget) {
        {
            float f = 10.0F;
            float f1 = pProjectileItems.size() == 1 ? 0.0F : 20.0F / (float)(pProjectileItems.size() - 1);
            float f2 = (float)((pProjectileItems.size() - 1) % 2) * f1 / 2.0F;
            float f3 = 1.0F;

            for (int i = 0; i < pProjectileItems.size(); i++) {
                ItemStack itemstack = pProjectileItems.get(i);
                if (!itemstack.isEmpty()) {
                    float f4 = f2 + f3 * (float)((i + 1) / 2) * f1;
                    f3 = -f3;
                    pWeapon.hurtAndBreak(this.getDurabilityUse(itemstack), pShooter, LivingEntity.getSlotForHand(pHand));


                    WitherSkull projectile = new WitherSkull(EntityType.WITHER_SKULL, pLevel);


                    projectile.setPos(pShooter.getX(), pShooter.getEyeY() - (double)0.1F, pShooter.getZ());



                    this.shootProjectile(pShooter, projectile, i, pVelocity, pInaccuracy, f4, pTarget);
                    pLevel.addFreshEntity(projectile);
                }
            }
        }


    }
}



