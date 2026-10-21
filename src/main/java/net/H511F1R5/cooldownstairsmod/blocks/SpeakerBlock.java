package net.H511F1R5.cooldownstairsmod.blocks;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.blocks.blockLogic.ConnectedTextureBlock;
import net.H511F1R5.cooldownstairsmod.sounds.AmplifiedSoundInstance;
import net.H511F1R5.cooldownstairsmod.sounds.DuplicateSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = coolDownstairsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SpeakerBlock extends ConnectedTextureBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static class SpeakerData {
        public final BlockPos jukeboxPos;
        public final List<BlockPos> speakers;
        public final List<SoundInstance> duplicates;

        public SpeakerData(BlockPos jukeboxPos, List<BlockPos> speakers, List<SoundInstance> duplicates) {
            this.jukeboxPos = jukeboxPos;
            this.speakers = speakers;
            this.duplicates = duplicates;
        }
    }

    // Maps each active amplified sound to its linked speakers and duplicated sound instances
    private static final Map<SoundInstance, SpeakerData> activeSpeakers = new ConcurrentHashMap<>();

    public SpeakerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = super.getStateForPlacement(pContext);
        if (state == null) state = this.defaultBlockState();
        return state.setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    @Override
    protected boolean supports3D() {
        return true;
    }

    @Override
    protected boolean supports2D() {
        return false;
    }

    @Override
    protected boolean matchesMultiblockRequirements(BlockState state, BlockState otherState) {
        if (state.hasProperty(FACING) && otherState.hasProperty(FACING)) {
            return state.getValue(FACING) == otherState.getValue(FACING);
        }
        return super.matchesMultiblockRequirements(state, otherState);
    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        SoundInstance soundSource = event.getSound();
        if (soundSource instanceof AmplifiedSoundInstance || soundSource instanceof DuplicateSoundInstance)
            return;

        if (soundSource != null && soundSource.getSource() == SoundSource.RECORDS) {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level != null) {
                BlockPos pos = BlockPos.containing(soundSource.getX(), soundSource.getY(), soundSource.getZ());

                List<BlockPos> foundSpeakers = new ArrayList<>();
                outer: for (int x = -6; x <= 6; x++) {
                    for (int y = -6; y <= 6; y++) {
                        for (int z = -6; z <= 6; z++) {
                            BlockPos checkPos = pos.offset(x, y, z);
                            BlockState state = level.getBlockState(checkPos);
                            if (state.is(coolDownstairsBlocks.SPEAKER_BLOCK.get())) {
                                foundSpeakers.add(checkPos.immutable());
                                if (foundSpeakers.size() >= 4)
                                    break outer;
                            } // Calculates if theres other speakers in a six block radius of the jukebox
                        }
                    }
                }

                if (!foundSpeakers.isEmpty()) {
                    List<SoundInstance> duplicates = new ArrayList<>();
                    int multiplier = (int) Math.pow(1.25, foundSpeakers.size());

                    for (int i = 0; i < multiplier - 1; i++) {
                        DuplicateSoundInstance extraSound = new DuplicateSoundInstance(soundSource, 1.0f);
                        mc.getSoundManager().play(extraSound);
                        duplicates.add(extraSound);
                    } // Increases music disc volume by like layering multiple tracks over one another

                    float distanceBonus = foundSpeakers.size() * 1.0f;
                    AmplifiedSoundInstance amplified = new AmplifiedSoundInstance(soundSource, distanceBonus);

                    SpeakerData data = new SpeakerData(pos, foundSpeakers, duplicates);
                    activeSpeakers.put(amplified, data);
                    event.setSound(amplified);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null)
            return;

        Iterator<Map.Entry<SoundInstance, SpeakerData>> iter = activeSpeakers.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<SoundInstance, SpeakerData> entry = iter.next();
            SoundInstance sound = entry.getKey();
            SpeakerData data = entry.getValue();

            // When the original playback is stopped (e.g. disk removed), the main amplified
            // instance will register as inactive
            if (!mc.getSoundManager().isActive(sound)) {
                // Instantly force-stop all duplicated track clones so they don't keep playing
                // indefinitely
                for (SoundInstance duplicate : data.duplicates) {
                    mc.getSoundManager().stop(duplicate);
                }
                iter.remove();
                continue;
            }

            // Fallback: If the disc was forcefully popped out, check the Jukebox blockstate
            BlockState jukeboxState = level.getBlockState(data.jukeboxPos);
            if (!jukeboxState.hasProperty(JukeboxBlock.HAS_RECORD) || !jukeboxState.getValue(JukeboxBlock.HAS_RECORD)) {
                mc.getSoundManager().stop(sound);
                for (SoundInstance duplicate : data.duplicates) {
                    mc.getSoundManager().stop(duplicate);
                }
                iter.remove();
                continue;
            }

            for (BlockPos speakerPos : data.speakers) {
                // Average once every 5 seconds (100 ticks)
                if (level.random.nextInt(100) == 0) {
                    double px = speakerPos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 0.4;
                    double py = speakerPos.getY() + 1.05;
                    double pz = speakerPos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 0.4;

                    double color = level.random.nextDouble();
                    level.addParticle(ParticleTypes.NOTE, px, py, pz, color, 0.0, 0.0);
                } // Spawns a note soundeffect when working every 5 secs, with randomized technicolor!
            }
        }
    }
}
