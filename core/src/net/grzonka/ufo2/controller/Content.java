package net.grzonka.ufo2.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.awt.TextField;
import java.util.HashMap;

public class Content {

  private HashMap<String, Texture> textures;
  private HashMap<String, Sound> sounds;

  public Content() {
    textures = new HashMap<>();
    sounds = new HashMap<>();
  }

  // texture handeling
  public void loadTexture(String path, String key) {
    Texture tempTexture = new Texture(Gdx.files.internal(path));
    textures.put(key, tempTexture);
  }

  public Texture getTexture(String key){
    return textures.get(key);
  }

  public void disposeTexture(String key){
    Texture tempTexture = textures.get(key);
    if (tempTexture != null){
      tempTexture.dispose();
    }
  }


  // sound hadeling
  public void loadSound(String path, String key) {
    Sound tempSound = Gdx.audio.newSound(Gdx.files.internal(path));
    sounds.put(key, tempSound);
  }

  public Sound getSound(String key){
    return sounds.get(key);
  }

  public void disposeSound(String key){
    Sound tempSound = sounds.get(key);
    if (tempSound != null){
      tempSound.dispose();
    }
  }

}
