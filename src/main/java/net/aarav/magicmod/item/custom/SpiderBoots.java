package net.aarav.magicmod.item.custom;

import net.aarav.magicmod.item.ModItems;
import net.minecraft.core.Holder;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.Level;

import static java.lang.Math.round;
    
public class SpiderBoots extends ArmorItem {

    public int reload=RELOADTIME;
    private static int RELOADTIME = 20;

    public SpiderBoots(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        if (this.reload<RELOADTIME){
            this.reload++;
        }
        player.fallDistance=0;

        if (player.getInventory().armor.get(0).is(ModItems.SPIDER_BOOTS.get()) && player.isShiftKeyDown() && !player.onGround() && this.reload == RELOADTIME) {
            double radians = Math.toRadians(round(player.getYRot()));
            player.lerpMotion(1.0f * -Math.sin(radians),1.0f,1.0f* Math.cos(radians));
            this.reload=0;
        }
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

}
