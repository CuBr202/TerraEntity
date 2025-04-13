package org.confluence.terraentity.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.confluence.terraentity.client.util.DefaultBoneBoundIdents;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NPCRenderer<T extends AbstractTerraNPC> extends GeoNormalRenderer<T>{
    private static final String LEFT_HAND = DefaultBoneBoundIdents.LEFT_HAND_BONE_IDENT;
    private static final String RIGHT_HAND = DefaultBoneBoundIdents.RIGHT_HAND_BONE_IDENT;

    private static final String LEFT_BOOT = DefaultBoneBoundIdents.LEFT_FOOT_ARMOR_BONE_IDENT;
    private static final String RIGHT_BOOT = DefaultBoneBoundIdents.RIGHT_FOOT_ARMOR_BONE_IDENT;
    private static final String LEFT_BOOT_2 = DefaultBoneBoundIdents.LEFT_FOOT_ARMOR_BONE_2_IDENT;
    private static final String RIGHT_BOOT_2 = DefaultBoneBoundIdents.RIGHT_FOOT_ARMOR_BONE_2_IDENT;

    private static final String LEFT_ARMOR_LEG = DefaultBoneBoundIdents.LEFT_LEG_ARMOR_BONE_IDENT;
    private static final String RIGHT_ARMOR_LEG = DefaultBoneBoundIdents.RIGHT_LEG_ARMOR_BONE_IDENT;
    private static final String LEFT_ARMOR_LEG_2 = DefaultBoneBoundIdents.LEFT_LEG_ARMOR_BONE_2_IDENT;
    private static final String RIGHT_ARMOR_LEG_2 = DefaultBoneBoundIdents.RIGHT_LEG_ARMOR_BONE_2_IDENT;

    private static final String CHESTPLATE = DefaultBoneBoundIdents.BODY_ARMOR_BONE_IDENT;
    private static final String RIGHT_SLEEVE = DefaultBoneBoundIdents.RIGHT_ARM_ARMOR_BONE_IDENT;
    private static final String LEFT_SLEEVE = DefaultBoneBoundIdents.LEFT_ARM_ARMOR_BONE_IDENT;

    private static final String HELMET = DefaultBoneBoundIdents.HEAD_ARMOR_BONE_IDENT;

    public NPCRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        super(renderManager, path.withPrefix("npc/"));
        this.addRenderLayer(new ItemArmorGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getArmorItemForBone(GeoBone bone, T animatable) {
                // Return the items relevant to the bones being rendered for additional rendering
                return switch (bone.getName()) {
                    case LEFT_BOOT, RIGHT_BOOT, LEFT_BOOT_2, RIGHT_BOOT_2 -> this.bootsStack;
                    case LEFT_ARMOR_LEG, RIGHT_ARMOR_LEG, LEFT_ARMOR_LEG_2, RIGHT_ARMOR_LEG_2 -> this.leggingsStack;
                    case CHESTPLATE, RIGHT_SLEEVE, LEFT_SLEEVE -> this.chestplateStack;
                    case HELMET -> this.helmetStack;
                    case LEFT_HAND -> this.offhandStack;
                    case RIGHT_HAND -> this.mainHandStack;
                    default -> null;
                };
            }

            // Return the equipment slot relevant to the bone we're using
            @Nonnull
            @Override
            protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, T animatable) {
                return switch (bone.getName()) {
                    case LEFT_BOOT, RIGHT_BOOT, LEFT_BOOT_2, RIGHT_BOOT_2 -> EquipmentSlot.FEET;
                    case LEFT_ARMOR_LEG, RIGHT_ARMOR_LEG, LEFT_ARMOR_LEG_2, RIGHT_ARMOR_LEG_2 -> EquipmentSlot.LEGS;
                    case RIGHT_HAND -> !animatable.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    case LEFT_HAND -> animatable.isLeftHanded() ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    case CHESTPLATE -> EquipmentSlot.CHEST;
                    case HELMET -> EquipmentSlot.HEAD;
                    default -> super.getEquipmentSlotForBone(bone, stack, animatable);
                };
            }

            // Return the ModelPart responsible for the armor pieces we want to render
            @Nonnull
            @Override
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, T animatable, HumanoidModel<?> baseModel) {
                return switch (bone.getName()) {
                    case LEFT_BOOT, LEFT_BOOT_2, LEFT_ARMOR_LEG, LEFT_ARMOR_LEG_2 -> baseModel.leftLeg;
                    case RIGHT_BOOT, RIGHT_BOOT_2, RIGHT_ARMOR_LEG, RIGHT_ARMOR_LEG_2 -> baseModel.rightLeg;
                    case RIGHT_HAND -> baseModel.rightArm;
                    case LEFT_HAND -> baseModel.leftArm;
                    case CHESTPLATE -> baseModel.body;
                    case HELMET -> baseModel.head;
                    default -> super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
                };
            }

            protected void prepModelPartForRender(PoseStack poseStack, GeoBone bone, ModelPart sourcePart) {

                switch (bone.getName()) {
                    case LEFT_BOOT, LEFT_BOOT_2, LEFT_ARMOR_LEG, LEFT_ARMOR_LEG_2,
                         RIGHT_BOOT, RIGHT_BOOT_2, RIGHT_ARMOR_LEG, RIGHT_ARMOR_LEG_2:
                        bone.setRotY(0);
                        bone.setRotZ(0);
                        bone.setRotX(0);
                        poseStack.translate(0,-0.1f,0);
                        poseStack.scale(1f,1.2F,1f);
                        poseStack.translate(0,0.2f,0);
                        break;

                    case CHESTPLATE:
                        break;

                    case HELMET: {
                        bone.setRotY(0);
                        bone.setRotZ(0);
                        bone.setRotX(0);
                        break;
                    }
                    default:
                        break;
                }
                super.prepModelPartForRender(poseStack, bone, sourcePart);

            }


        });


        // Add some held item rendering
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, T animatable) {

                // Retrieve the items in the entity's hands for the relevant bone
                return switch (bone.getName()) {
                    case LEFT_HAND -> animatable.isLeftHanded() ?
                            animatable.getMainHandItem() : animatable.getOffhandItem();
                    case RIGHT_HAND -> animatable.isLeftHanded() ?
                            animatable.getOffhandItem() : animatable.getMainHandItem();
                    default -> null;
                };
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
                // Apply the camera transform for the given hand
                return switch (bone.getName()) {
                    case RIGHT_HAND, "torso" -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    case LEFT_HAND -> ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
                    default -> ItemDisplayContext.NONE;
                };
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable,
                                              MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.translate(0, 0, -0.0625);
                poseStack.translate(0, -0.0625, 0);
                boolean offhand = stack == animatable.getOffhandItem();
//                if (stack.getItem() instanceof PotionItem) {
//                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));
//                }
                if (!offhand) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem)
                        poseStack.translate(0, 0.125, -0.25);
                } else {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0, 0.125, 0.25);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    }
                }

                adjustHandItemRendering(poseStack, stack, animatable, partialTick, offhand);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    protected void adjustHandItemRendering(PoseStack poseStack, ItemStack stack, T animatable, float partialTick, boolean offhand) {
        poseStack.translate(0.03F,0,-0.5F);
    }

}
