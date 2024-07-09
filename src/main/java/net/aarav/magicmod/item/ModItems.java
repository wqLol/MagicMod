package net.aarav.magicmod.item;

import net.aarav.magicmod.MagicMod;

import net.aarav.magicmod.item.custom.HyperionSword;
import net.aarav.magicmod.item.custom.ObamiumItem;
import net.aarav.magicmod.item.custom.DreadlordSword;
import net.aarav.magicmod.item.custom.SpiderBoots;
import net.minecraft.tags.TagKey;


import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MagicMod.MOD_ID);
    public static final RegistryObject<Item> OBAMIUM = ITEMS.register("obamium",
            () -> new ObamiumItem(new Item.Properties()));
    public static final RegistryObject<Item> SPIDER_BOOTS = ITEMS.register("spider_boots", () -> new SpiderBoots(ArmorMaterials.IRON, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(1133))));
    public static final RegistryObject<Item> HYPERION = ITEMS.register("hyperion",()->new HyperionSword(Tiers.IRON, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 50, 20))));
    public static final RegistryObject<Item> DREADLORD_SWORD = ITEMS.register("dreadlord_sword",
            () -> new DreadlordSword(new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(new Tier() {
                @Override
                public int getUses() {
                    return 100;
                }

                @Override
                public float getSpeed() {
                    return 0;
                }

                @Override
                public float getAttackDamageBonus() {
                    return 0;
                }

                @Override
                public TagKey<Block> getIncorrectBlocksForDrops() {
                    return null;
                }

                @Override
                public int getEnchantmentValue() {
                    return 0;
                }

                @Override
                public Ingredient getRepairIngredient() {
                    return Ingredient.of(Items.DIAMOND);
                }
            }, 20, 1))));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
