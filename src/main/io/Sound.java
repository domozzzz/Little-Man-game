package main.io;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    public static final Sound hit = new Sound("/res/sounds/roblox/hit.wav");
    public static final Sound death = new Sound("/res/sounds/roblox/death.wav");
    public static final Sound open = new Sound("/res/sounds/roblox/button.wav");
    public static final Sound shoot = new Sound("/res/sounds/roblox/snap.wav");
    public static final Sound pickUp = new Sound("/res/sounds/roblox/bass.wav");
    public static final Sound hurt = new Sound("/res/sounds/roblox/splat.wav");

    private Clip clip;

    private Sound(String name) {
        try {
            URL url = Sound.class.getResource(name);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            new Thread(() -> {
                synchronized (clip) {
                    clip.setFramePosition(0);
                    clip.start();
                }
            }).start();
        }
    }
}