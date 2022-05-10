package de.rafael.ravbite.engine.sound.components;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/07/2022 at 3:11 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
import de.rafael.ravbite.engine.graphics.components.transform.Transform;
import de.rafael.ravbite.engine.sound.utils.Sound;
import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

public class AudioSource extends Component {

    private int sourceId;

    public Sound sound;

    public boolean playOnInitialization = false;
    public boolean looping = false;
    public float gain = 1f;
    public float pitch = 1f;

    @Override
    public void initialize() {
        sourceId = AL10.alGenSources();
        updateSourceProperty();
        if(playOnInitialization) play();
    }

    @Override
    public void valueUpdate(String fieldName) {
        updateSourceProperty();
    }

    @Override
    public void dispose() {
        stop();
        AL10.alDeleteSources(sourceId);
    }

    @Override
    public void update() {
        updateSource();
    }

    /**
     * Sets the sound the source is using
     * @param sound Sound to use
     * @return AudioSource
     */
    public AudioSource use(Sound sound) {
        this.sound = sound;
        return this;
    }

    /**
     * Sets If the sound should be played at initialization the source is using
     * @return AudioSource
     */
    public AudioSource playOnInitialization() {
        this.playOnInitialization = true;
        return this;
    }

    /**
     * Sets if the sound should loop
     * @return AudioSource
     */
    public AudioSource loop() {
        this.looping = true;
        return this;
    }

    /**
     * Plays the stored sound
     * @param gain Gain
     * @param pitch Pitch
     */
    public void play(float gain, float pitch) {
        play(this.sound, gain, pitch);
    }

    /**
     * Plays the stored sound
     */
    public void play() {
        play(this.sound);
    }

    /**
     * Plays a sound
     * @param sound Sound to play
     * @param gain Gain
     * @param pitch Pitch
     */
    public void play(Sound sound, float gain, float pitch) {
        stop();
        AL10.alSourcef(sourceId, AL10.AL_GAIN, gain);
        AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, sound.soundId());
        resume();
    }

    /**
     * Plays a sound
     * @param sound Sound to play
     */
    public void play(Sound sound) {
        stop();
        updateSourceProperty();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, sound.soundId());
        resume();
    }

    /**
     * Updates the source location and velocity
     */
    private void updateSource() {
        Transform transform = getGameObject().getTransform();
        Vector3f position = transform.position;
        Vector3f velocity = transform.velocity;

        AL10.alSource3f(sourceId, AL10.AL_POSITION, position.x, position.y, position.z);
        AL10.alSource3f(sourceId, AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

    /**
     * Updates the sourceProperty
     */
    private void updateSourceProperty() {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, gain);
        AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);

        // TODO: Add distance attenuation property's
    }

    /**
     * Pauses the soundPlayer
     */
    public void pause() {
        AL10.alSourcePause(sourceId);
    }

    /**
     * Resumes the soundPlayer
     */
    public void resume() {
        AL10.alSourcePlay(sourceId);
    }

    /**
     * Stops the currently playing sound
     */
    public void stop() {
        AL10.alSourceStop(sourceId);
    }

    /**
     * @return If true source is playing something
     */
    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    /**
     * Sets and updates if the sound should loop
     * @param looping Boolean value
     */
    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    /**
     * Sets and updates the gain
     * @param gain New gain
     */
    public void setGain(float gain) {
        this.gain = gain;
        updateSourceProperty();
    }

    /**
     * Sets and updates the pitch
     * @param pitch New pitch
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
        updateSourceProperty();
    }

}
