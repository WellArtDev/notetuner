package com.wellartdev.notetuner;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Shows the tuned pitch of the note block under your crosshair.
 *
 * <p>Note blocks have 25 pitches (blockstate {@code note} 0-24) following
 * the chromatic scale starting at F#: 0 = F#1, 1 = G1, ..., 24 = F#3.
 * NoteTuner samples the crosshair target every 5 client ticks and draws
 * a single line above the hotbar like {@code "Note: F#2 (harp)"} while
 * you are looking at a note block. The line is cleared as soon as you
 * look away, so it never lingers.
 *
 * <p>Client-side only. No mixins, no networking, nothing persisted.
 */
public class NoteTuner implements ClientModInitializer {
    public static final String MOD_ID = "notetuner";

    /** Chromatic scale names starting at F# (note 0 = F#1). */
    private static final String[] NAMES = {
            "F#", "G", "G#", "A", "A#", "B",
            "C", "C#", "D", "D#", "E", "F"
    };

    // Read by the HUD element every frame; rebuilt on tick below.
    private String hudLine = null;
    private int tick = 0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(MOD_ID, "note_tuner"),
                (extractor, deltaTracker) -> {
                    if (hudLine == null) {
                        return;
                    }
                    Minecraft mc = Minecraft.getInstance();
                    int y = mc.getWindow().getGuiScaledHeight() - 70;
                    int x = (mc.getWindow().getGuiScaledWidth() - mc.font.width(hudLine)) / 2;
                    extractor.text(mc.font, hudLine, x, y, 0xFFFF55);
                });
    }

    private void onTick(Minecraft mc) {
        tick++;
        if (mc.player == null || tick % 5 != 0) {
            return;
        }

        hudLine = null;
        HitResult hit = mc.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) {
            return;
        }
        BlockPos pos = ((BlockHitResult) hit).getBlockPos();
        BlockState state = mc.player.level().getBlockState(pos);
        if (!state.is(Blocks.NOTE_BLOCK)) {
            return;
        }

        int note = state.getValue(NoteBlock.NOTE);
        String pitch = pitchName(note);
        String instrument = state.getValue(NoteBlock.INSTRUMENT).getSerializedName();
        hudLine = "Note: " + pitch + " (" + instrument + ")";
    }

    /**
     * Converts a note-block pitch (0-24) to a human-readable name.
     * Pitch 0 = F#1, the chromatic scale ascends from there, and pitch
     * 24 = F#3. Out-of-range values are clamped defensively.
     */
    private static String pitchName(int note) {
        int clamped = Math.min(Math.max(note, 0), 24);
        return NAMES[clamped % 12] + (1 + clamped / 12);
    }
}
