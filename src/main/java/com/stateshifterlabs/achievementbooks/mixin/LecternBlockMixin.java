package com.stateshifterlabs.achievementbooks.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlock.class)
public class LecternBlockMixin {
    private static final Logger LOGGER = LogManager.getLogger(LecternBlockMixin.class);

    @Inject(cancellable = true, method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER))
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if(player.getStackInHand(hand).getOrCreateNbt().getBoolean("Achievement Book")) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(
            cancellable = true,
            method = "openScreen",
            at = @At(value = "HEAD"))
    public void openScreen(World world, BlockPos pos, PlayerEntity player, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LecternBlockEntity) {
            LecternBlockEntity lectern = (LecternBlockEntity) blockEntity;
            ItemStack book = lectern.getBook();
            player.incrementStat(Stats.INTERACT_WITH_LECTERN);
            if (book.getOrCreateNbt().contains("Achievement Book")) {
                player.sendMessage(Text.of("Feature coming soon"), false);
                LOGGER.info("Would've opened the achievement book");
            } else {
                player.openHandledScreen((LecternBlockEntity)blockEntity);
            }
        }
        ci.cancel();
    }
}
