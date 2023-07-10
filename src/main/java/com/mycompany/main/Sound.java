package com.mycompany.main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    private Clip clip;
    private AudioInputStream audioStream;
    private String caminho;
    
    public static final Sound musicBackground = new Sound("res/music.wav");
    public static final Sound hurtEffect = new Sound("res/hurt.wav");
    
    private Sound(String caminho){
        try{
            this.caminho = caminho;
            audioStream = AudioSystem.getAudioInputStream(new File(caminho)); 	
		
            // the reference to the clip 
            clip = AudioSystem.getClip();
            
            clip.open(audioStream);
        }catch(Throwable e){
            e.printStackTrace();
        }
    }
    
    public void play(int time){
        try{
            new Thread(){
                public void run(){       
                   if(clip.isOpen()){
                       try { 
                           resetAudioStream();
                       } catch (UnsupportedAudioFileException ex) {
                           Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (IOException ex) {
                           Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                       } catch (LineUnavailableException ex) {
                           Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                       }
                   } 
                   clip.start();
                }
            }.start();
        }catch(Throwable e){
            e.printStackTrace();
        }  
    }
    
    public void loop(){
        try{
            new Thread(){
                public void run(){
                    clip.loop(Clip.LOOP_CONTINUOUSLY); 
                }
            }.start();
        }catch(Throwable e){
            e.printStackTrace();
        }  
    }
    
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        try{
           clip.stop(); 
           clip.close(); 
           audioStream = AudioSystem.getAudioInputStream(new File(caminho)); 
           clip.open(audioStream);   
        }catch(Throwable e){
           e.printStackTrace();
        } 
           
       
    } 
}
