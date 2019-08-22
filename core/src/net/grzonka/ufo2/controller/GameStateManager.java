package net.grzonka.ufo2.controller;

import java.util.Stack;
import net.grzonka.ufo2.Game;
import net.grzonka.ufo2.model.GameState;
import net.grzonka.ufo2.model.Play;

public class GameStateManager {

  private Game game;
  private Stack<GameState> gameStates;

  public static final int PLAY = 65312874;

  public GameStateManager(Game game) {
    this.game = game;
    gameStates = new Stack<GameState>();
    pushState(PLAY);
  }

  public Game getGame(){return game;}

  public void update(float dt) {

  }

  public void render() {
    gameStates.peek().render();
  }

  private GameState getState(int state){
    if (state == PLAY) {
      return new Play(this);
    }
    return null;
  }

  public void setState(int state){
    popState();
    pushState(state);
  }

  public void pushState(int state){
    gameStates.push(getState(state));
  }

  public void popState() {
    GameState g = gameStates.pop();
    g.dispose();
  }


}

