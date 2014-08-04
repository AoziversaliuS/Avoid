package oz.game.screen;


import oz.game.avoid.MyGdxGame;
import oz.game.base.OzScreen;
import oz.game.base.OzUtils;
import oz.game.global.G;
import oz.game.global.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class MainScreen extends OzScreen{
	
	
	ImageButton startGameBtn;
	Image backGround;

	public MainScreen(MyGdxGame game, String currentScreenName) {
		super(game, currentScreenName);
		ScalingViewport scalingView = new ScalingViewport(Scaling.fillX,G.REFER_SCREEN_WIDTH
				,G.REFER_SCREEN_HEIGHT);
		
		stage = new Stage(scalingView);
		System.out.println("cameraX : "+scalingView.getCamera().position.x+" cameraY : "+scalingView.getCamera().position.y);
		Vector2 fullscreen = stage.screenToStageCoordinates(new Vector2(Gdx.graphics.getWidth(),0));
		System.out.println("x = "+fullscreen.x+" y = "+fullscreen.y);
		backGround = new Image(OzUtils.newTexture((int)fullscreen.x,(int)fullscreen.y,G.MAINSCREEN_BACKGROUND_COLOR));
		addTexture("startGameUp", new Texture("image/startGameUp.png"));
		addTexture("startGameDown",new Texture("image/startGameDown.png"));
		startGameBtn = new ImageButton(skin.newDrawable("startGameUp"),
					skin.newDrawable("startGameDown"));
		
		stage.addActor(backGround);
		stage.addActor(startGameBtn);
		addEvent();
	}
	@Override
	public void reset() {
		startGameBtn.setPosition(G.REFER_SCREEN_WIDTH, 500);
//		startGameBtn.setPosition(0,1100);
		startGameBtn.addAction(Actions.moveTo(0, 500, 0.5f));
		darkAlpha = 1;
	}
	@Override
	public void addEvent() {
		startGameBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				//touchUp时触屏点还在按钮内才触发事件
				if(x>0 && x<startGameBtn.getWidth() && y>0 && y<startGameBtn.getHeight()){
					startGameBtn.addAction(Actions.moveTo(-startGameBtn.getWidth(),
					startGameBtn.getY(), 0.25f));
					setScreen(Screens.GAME);
				}
			}
		});
	}
	
	@Override
	public boolean begin(float delta) {
		if(darkAlpha<=0){
			stage.act();
			stage.draw();
			if( startGameBtn.getX()<=0){
				return true;
			}
		}
		else{
			stage.draw();
			setDarkness(darkAlpha-=dAlpha);
		}
		return false;
	}

	@Override
	public boolean end(float delta) {
		stage.act();
		stage.draw();
		if(startGameBtn.getX()<=-startGameBtn.getWidth()){
			darkAlpha+=0.05f;
			if(darkAlpha<1){
				setDarkness(darkAlpha);
			}
			else{
				return true;
			}
		}
		return false;
	}

	@Override
	public void actAndDraw(float delta) {
		stage.act();
		stage.draw();
	}
	@Override
	public void hide() {
	}
	@Override
	public void pause() {
		
	}
	@Override
	public void resume() {
		
	}
	@Override
	public void dispose() {
	}

}
