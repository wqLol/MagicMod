package net.aarav.magicmod.item.custom;


import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import net.minecraft.server.commands.SummonCommand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.crafting.conditions.FalseCondition;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;


import javax.crypto.spec.PSource;
//import java.lang.runtime.TemplateRuntime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.primitives.Ints.max;
import static java.lang.Math.*;
import static net.aarav.magicmod.MagicMod.DecrementSpell;
import static net.aarav.magicmod.MagicMod.IncrementSpell;

public class ElementalWand extends Item {

    static final String[] spells = {"Fire", "Speed", "Lightning", "Dark Magic", "Earth", "Ice", "Shield"};
    private int currSpell = 0;

    private int __cooldownCounter = 0;
    static int Cooldown = 5;

    private int __MegaJumpCooldown = 40;
    static int MegaJumpCooldown = 20;
    private List<LivingEntity> cursedEntities = new ArrayList<>();

    private boolean ChangedPerspective = false;

    public ElementalWand(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        // COOLDOWNS

//        __MegaJumpCooldown = (min(MegaJumpCooldown, __MegaJumpCooldown + 1));


        Minecraft mc= Minecraft.getInstance();
        if (pIsSelected) {
            if ((pEntity instanceof Player)) {
//                pEntity.sendSystemMessage(Component.literal(Integer.toString(__MegaJumpCooldown)));

                if (mc.options.keyAttack.isDown()) {
                    Vec3 vec3 = Vec3.ZERO;
                            switch (currSpell) {
                        case 0:
                            vec3= pEntity.getForward().normalize().scale(7).add(pEntity.position());

                            pLevel.addParticle(ParticleTypes.FLAME, vec3.x, vec3.y + 1.7f, vec3.z, (random() - 0.5) * 0.1, (random() - 0.5) * 0.1, (random() - 0.5) * 0.1);
                            pLevel.addParticle(ParticleTypes.FLAME, vec3.x, vec3.y + 1.7f, vec3.z, (random() - 0.5) * 0.1, (random() - 0.5) * 0.1, (random() - 0.5) * 0.1);
                            pLevel.addParticle(ParticleTypes.FLAME, vec3.x, vec3.y + 1.7f, vec3.z, (random() - 0.5) * 0.1, (random() - 0.5) * 0.1, (random() - 0.5) * 0.1);
                            pLevel.addParticle(ParticleTypes.FLAME, vec3.x, vec3.y + 1.7f, vec3.z, (random() - 0.5) * 0.5, (random() - 0.5) * 0.5, (random() - 0.5) * 0.5);
                            pLevel.addParticle(ParticleTypes.FLAME, vec3.x, vec3.y + 1.7f, vec3.z, (random() - 0.5) * 0.5, (random() - 0.5) * 0.5, (random() - 0.5) * 0.5);
                            List<LivingEntity> entities = pLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, ((Player) pEntity), new AABB(new BlockPos(new Vec3i((int) vec3.x, (int) vec3.y, (int) vec3.z))).inflate(5));

                            for (LivingEntity e : entities) {
                                if (e != pEntity) {
                                    e.hurt(pLevel.damageSources().generic(), 10);
                                    e.setRemainingFireTicks(100);
                                }
                            }
                            break;
                        case 1:
                            if (__MegaJumpCooldown >= MegaJumpCooldown) {
                                vec3 = pEntity.getForward().scale(1);
                                pEntity.setDeltaMovement(vec3.x,vec3.y,vec3.z);
//                                __MegaJumpCooldown = 0;
                            }
                            break;

                        case 2:
                            LightningBolt c = new LightningBolt(EntityType.LIGHTNING_BOLT,pLevel);

                            c.setPos(vec3);
                            c.setPos(pEntity.position().add(pEntity.getForward().scale(14)));
                            pLevel.addFreshEntity(c);
                            break;

                        case 3:
                            vec3 = pEntity.position();
                            List<LivingEntity> nearbyEntities;
                            for (int i = 0;i < 20; i++) {
                                vec3 = pEntity.position().add(pEntity.getForward().scale(i));
                                nearbyEntities = pLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, ((Player) pEntity), new AABB(new BlockPos(new Vec3i((int) vec3.x, (int) vec3.y+1, (int) vec3.z))).inflate(4));
//                                entities.remove(pEntity);

                                nearbyEntities.remove(pEntity);



                                if (!nearbyEntities.isEmpty()) {
                                    Entity targetEntity = nearbyEntities.getFirst();

                                    // Step 1: Calculate the position 5 blocks in front of the player
                                    Vec3 playerForward = pEntity.getForward().scale(5); // 5 blocks forward
                                    Vec3 targetPosition = pEntity.position().add(playerForward);

                                    // Step 2: Calculate the direction vector (from entity to target position)
                                    Vec3 directionVector = targetPosition.subtract(targetEntity.position());

//                                    if (directionVector.length() <= 0.5) {
//                                        break;
//                                    }
                                    // Step 3: Normalize the direction vector
                                    directionVector  = directionVector.scale(0.5);

                                    Vec3 normalizedDirection = directionVector.normalize();

                                    // Step 4: Add motion to the entity in the direction of the normalized vector
                                    double speed = 2; // You can adjust the speed factor as needed

                                    targetEntity.setDeltaMovement(normalizedDirection.scale(speed));


//                                    targetEntity.setNoGravity(true);
                                        targetEntity.fallDistance = 0;
                                    break;
                                }



                            }

                            break;

                                case 4:
                                    int MaxRange = 30;
                                    BlockPos root = BlockPos.ZERO;
                                    for (int i =0 ; i <=MaxRange; i++) {
                                        if (i ==MaxRange) {
                                            break;
                                        }
                                        root = new BlockPos(
                                                new Vec3i(
                                                        (int)round(pEntity.position().x + pEntity.getForward().x * i),
                                                        (int)round(pEntity.position().y+pEntity.getForward().y * i),
                                                        (int)round(pEntity.position().z + pEntity.getForward().z * i)
                                                )
                                        );

                                        pLevel.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, root.getX(), root.getY(), root.getZ() , 0,-0.5,0);

                                        if (!pLevel.isEmptyBlock(root)) {break;}

                                    }


                                    recursiveExplodeBlock(pLevel,root,pLevel.getBlockState(root), new Vec3(0, 1,0), 8);


                    }
                }
                if (mc.options.keyUse.isDown()) {
                    Vec3 vec3 = Vec3.ZERO;
                    switch (currSpell) {

                        case 0:
                            vec3 = pEntity.getForward().normalize().scale(3).add(pEntity.position());
                            vec3.add(0,1,0);
                            Fireball c = new LargeFireball(pLevel, ((LivingEntity) pEntity), 0, 0, 0, 2);
                            c.setPos(vec3);

                            c.setDeltaMovement(pEntity.getForward().scale(5));
                            pLevel.addFreshEntity(c);
                            break;

                        case 1:

                            ((Player) pEntity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 40));
                            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
                            ChangedPerspective=true;
                            break;

                        case 2:
                            LightningBolt lb;
                            vec3 = pEntity.position();
                            List<LivingEntity> entities = pLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, ((Player) pEntity), new AABB(new BlockPos(new Vec3i((int) vec3.x, (int) vec3.y, (int) vec3.z))).inflate(5));

                            for (LivingEntity e : entities) {
                                if (e != pEntity) {
                                    e.hurt(pLevel.damageSources().generic(), 10);
                                    lb = new LightningBolt(EntityType.LIGHTNING_BOLT,pLevel);
                                    lb.setPos(e.position());
                                    pLevel.addFreshEntity(lb);
                                    e.setRemainingFireTicks(100);
                                }
                            }


                            break;
                        case 3:
                            // Calculate the position 3 blocks in front of the player
                            vec3 = pEntity.getForward().scale(3).add(pEntity.position());


                                // Get nearby living entities (excluding the player)
                                List<LivingEntity> choices = pLevel.getNearbyEntities(
                                        LivingEntity.class,
                                        TargetingConditions.DEFAULT,
                                        ((Player) pEntity),
                                        new AABB(new BlockPos(new Vec3i((int) vec3.x, (int) vec3.y, (int) vec3.z))).inflate(5)
                                );
                                choices.remove(pEntity);

                                // Add the choices to __StoredMobs and kill them
                                for (LivingEntity e : choices) {
//                                    e.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 1, 20));
                                    e.setPos(e.position().add(0, 0.1,0));
                                    e.setNoGravity(true);
                                    e.setYBodyRot(e.yBodyRot + 0.3f);
                                    e.setYBodyRot(e.yBodyRot + 0.3f);
                                    e.addEffect(new MobEffectInstance(MobEffects.POISON, 1, 20));
                                    e.hurt(pLevel.damageSources().generic(), 5);
                                    pLevel.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, e.getX() + Math.sin((e.tickCount )) * 2, e.getY() + 2, e.getZ()+ Math.cos((e.tickCount))* 2, 0,-0.5,0);
                                    pLevel.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER, e.getX() + Math.cos((e.tickCount )) * 2, e.getY() + 2, e.getZ()+ Math.sin((e.tickCount))* 2, 0,-0.5,0);
                                    if (!cursedEntities.contains(e)){ cursedEntities.add(e); }


                            }
//                            ChangedPerspective=true;
                            break;


//                            ((Player) pEntity).addEffect()
                        case 4:
                            vec3 = pEntity.position();
                            List<FallingBlockEntity> fallingEntities = pLevel.getEntitiesOfClass(
                                    FallingBlockEntity.class,
                                    new AABB(new BlockPos(new Vec3i((int) vec3.x, (int) vec3.y, (int) vec3.z))).inflate(100)
                            );


                            // Add the choices to __StoredMobs and kill them
                            for (FallingBlockEntity e : fallingEntities) {
//                                    e.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 1, 20));
                               e.setDeltaMovement(pEntity.getForward().scale(4));
                                e.setDeltaMovement(e.getDeltaMovement().add(random() -0.5,random() -0.5,random() -0.5));

                            }

                    }


                }     else if (ChangedPerspective) {
                    mc.options.setCameraType(CameraType.FIRST_PERSON);
                    ChangedPerspective=false;
                } else if (!cursedEntities.isEmpty()) {
                    for (LivingEntity e : cursedEntities) {
                        e.lerpMotion(0, -1, 0);

                        e.setNoGravity(false);
                    }
                    cursedEntities = new ArrayList<>();

                } if (mc.options.keyShift.isDown()) {
                    switch (currSpell) {
                        case 4:
                            int dist = 10;
                            Vec3 playerPos = pEntity.position();
                            Vec3 playerForward = pEntity.getForward();
                            playerForward = new Vec3(playerForward.x, 0 , playerForward.z);
                            BlockPos bp = new BlockPos( new Vec3i( (int) round(playerPos.x + playerForward.x * 10), (int) playerPos.y-1,(int) round(playerPos.z + playerForward.z * 10)  ));
                            BlockState block = pLevel.getBlockState(bp);

                            explodeBlock(pLevel, bp, block, new Vec3(0, 1, 0));








                    }
                } if (mc.options.keySprint.isDown()) {
                    switch (currSpell) {
                        case 4:
                            int MaxRange = 60;
                            BlockPos root = BlockPos.ZERO;
                            for (int i =0 ; i <=MaxRange; i++) {
                                if (i ==MaxRange) {
                                    break;
                                }
                                root = new BlockPos(
                                        new Vec3i(
                                                (int)round(pEntity.position().x + pEntity.getForward().x * i),
                                                (int)round(pEntity.position().y+pEntity.getForward().y * i),
                                                (int)round(pEntity.position().z + pEntity.getForward().z * i)
                                        )
                                );

                                pLevel.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, root.getX(), root.getY(), root.getZ() , 0,-0.5,0);

                                if (!pLevel.isEmptyBlock(root)) {break;}

                            }

                            recursivePlace(pLevel,root, pLevel.getBlockState(root), 2);








                    }
                }



            }


            __cooldownCounter = (min(Cooldown, __cooldownCounter + 1));

            if ((IncrementSpell.isDown() || DecrementSpell.isDown()) && __cooldownCounter >= Cooldown) {
                __cooldownCounter = 0;
                if (IncrementSpell.isDown()) {
                    currSpell -= 1;

                }
                if (DecrementSpell.isDown()) {
                    currSpell += 1;

                }


                if (currSpell > spells.length - 1) {
                    currSpell = 0;
                }
                if (currSpell < 0) {
                    currSpell = spells.length - 1;
                }
                pEntity.sendSystemMessage(Component.literal("Spell: " + spells[currSpell]));
                if (ChangedPerspective) {
                    mc.options.setCameraType(CameraType.FIRST_PERSON);
                    ChangedPerspective=false;
                }

            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    public static void explodeBlock(Level pLevel, BlockPos bp, BlockState block, Vec3 vel) {
        pLevel.removeBlock(bp, false);


        FallingBlockEntity fallingBlock = FallingBlockEntity.fall(pLevel, bp.above(), block);
        fallingBlock.hasImpulse = true;
        fallingBlock.hurtMarked=true;

        fallingBlock.addDeltaMovement(vel);

    }
    public static void recursiveExplodeBlock(Level pLevel, BlockPos bp, BlockState block, Vec3 vel, int depth) {
        explodeBlock(pLevel,bp, block, vel);
        if (depth <= 0) {
            return;
        }
        BlockState belowBlock = pLevel.getBlockState(bp.below());
        BlockState westBlock = pLevel.getBlockState(bp.west());
        BlockState eastBlock = pLevel.getBlockState(bp.east());
        BlockState northBlock = pLevel.getBlockState(bp.north());
        BlockState southBlock = pLevel.getBlockState(bp.south());

        if (!(belowBlock.isAir()) && !(belowBlock.is(Blocks.BEDROCK))) {
            recursiveExplodeBlock(pLevel, bp.below(), belowBlock, vel, depth-1);
        }
        if (!(westBlock.isAir()) && !(westBlock.is(Blocks.BEDROCK))) {
            recursiveExplodeBlock(pLevel, bp.west(), westBlock, vel, depth-1);
        }
        if (!(eastBlock.isAir()) && !(eastBlock.is(Blocks.BEDROCK))) {
            recursiveExplodeBlock(pLevel, bp.east(), eastBlock, vel, depth-1);
        }
        if (!(northBlock.isAir()) && !(northBlock.is(Blocks.BEDROCK))) {
            recursiveExplodeBlock(pLevel, bp.north(), northBlock, vel, depth-1);
        }
        if (!(southBlock.isAir()) && !(southBlock.is(Blocks.BEDROCK))) {
            recursiveExplodeBlock(pLevel, bp.south(), southBlock, vel, depth-1);
        }


    }
    public static void recursivePlace(Level pLevel, BlockPos root, BlockState state , int depth) {
        pLevel.setBlock(root, state, 0);
//        pLevel.setBlock(root.west(), state, 0);
//        pLevel.setBlock(root.east(), state, 0);
//        pLevel.setBlock(root.north(), state, 0);
//        pLevel.setBlock(root.south(), state, 0);
        if (depth <= 0) {return;}


        recursivePlace(pLevel,root.above(),state,depth-1);
        recursivePlace(pLevel,root.below(),state,depth-1);
        recursivePlace(pLevel,root.west(),state,depth-1);
        recursivePlace(pLevel,root.east(),state,depth-1);
        recursivePlace(pLevel,root.north(),state,depth-1);
        recursivePlace(pLevel,root.south(),state,depth-1);

    }
}


