package game;

import javax.sound.sampled.*;

public class SoundManager {

    static Clip loopClip;

    // 🔁 âm thanh lặp
    public static void loop(String file){

        try{

            if(loopClip != null && loopClip.isRunning()) return;

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(new java.io.File("src" + file));

            loopClip = AudioSystem.getClip();
            loopClip.open(audio);

            loopClip.loop(Clip.LOOP_CONTINUOUSLY);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // ⛔ dừng âm thanh lặp
    public static void stopLoop(){

        if(loopClip != null){
            loopClip.stop();
            loopClip.close();
            loopClip = null;
        }
    }

    // 🔊 âm thanh phát 1 lần
    public static void play(String file){

        try{

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(new java.io.File("src" + file));

            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            clip.start();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}