package com.accbdd.aqua_vitae.client.audio;

import com.accbdd.aqua_vitae.registry.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class SaxophoneSound extends AbstractTickableSoundInstance {
    public static SaxophoneSound INSTANCE;
    private float fadeVolume;
    private boolean isStarting;

    public SaxophoneSound() {
        super(ModSounds.SAXOPHONES.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
        this.volume = 0.01f;
        this.relative = true;
        this.fadeVolume = 0f;
        this.isStarting = true;
    }

    @Override
    public void tick() {
        this.volume = this.volume + (this.fadeVolume - this.volume) * 0.15f;

        if (this.fadeVolume > 0.0f) {
            this.isStarting = false;
        }

        if (!this.isStarting && this.volume <= 0.001f && this.fadeVolume == 0.0f) {
            this.stopSound();
        }
    }

    public void setFadeVolume(float fadeVolume) {
        this.fadeVolume = fadeVolume;
    }

    public void stopSound() {
        this.stop();
    }
}
