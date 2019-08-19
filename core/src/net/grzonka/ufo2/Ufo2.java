package net.grzonka.ufo2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ufo2 extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture megaMan;
	BitmapFont font;
	Sprite sprite;

	/*
	Other functions that can be implemented are:
	resize()
	pause()
	resume()
	 */

	@Override
	public void create() {
		batch = new SpriteBatch();
		megaMan = new Texture(Gdx.files.internal("Mega_man.png"));
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		sprite = new Sprite(megaMan);
	}

	// render() is called each frame. (can be thought of as an event loop)
	@Override
	public void render() {
		// gdx.gl is exposing OpenGL functionality
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
				sprite.translateX(-1f);
			else
				sprite.translateX(-10.0f);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
				sprite.translateX(1f);
			else
				sprite.translateX(10.0f);
		}

		batch.begin();
		//font.draw(batch, "Hello, World", 200, 200);
		//batch.draw(img, 0, 0);
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height){
	}

	@Override
	public void pause(){
	}

	@Override
	public void resume(){
	}

	@Override
	public void dispose() {
		batch.dispose();
		//img.dispose();
		megaMan.dispose();
		font.dispose();
	}
}