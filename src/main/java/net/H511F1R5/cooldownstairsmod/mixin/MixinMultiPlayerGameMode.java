package net.H511F1R5.cooldownstairsmod.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.H511F1R5.cooldownstairsmod.mixin.util.PlacementDelayUtil;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.objectweb.asm.Opcodes;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinMultiPlayerGameMode {

    @Shadow private int destroyDelay;

    // inject AFTER the line: this.destroyDelay = 5;
    @Inject(
        method = "destroyBlock",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;f_105745_:I",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void scaleDestroyDelay(BlockPos pos, CallbackInfo ci) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !mc.player.getAbilities().flying) return;

        double speed = mc.player.getDeltaMovement().horizontalDistance();
        int scaled = PlacementDelayUtil.scaleDelay(5, speed);
        this.destroyDelay = scaled;   // overwrite what vanilla just set
    }
}
