package net.grzonka.ufo2.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.grzonka.ufo2.Game;
import net.grzonka.ufo2.controller.GameStateManager;

public abstract class GameState {

  protected GameStateManager gameStateManager;
  protected Game game;

  protected SpriteBatch spriteBatch;
  protected OrthographicCamera orthographicCamera;

  protected GameState(GameStateManager gsm){
    this.gameStateManager = gsm;
    game = gsm.getGame();
    spriteBatch = game.getSpriteBatch();
    orthographicCamera = game.getCamera();
  }

  public void handleInput(){
  }

  public void update(float dt){
  }

  public void render(){
  }

  public void dispose(){
  }

}
