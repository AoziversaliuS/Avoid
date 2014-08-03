package oz.game.screen;

import java.util.ArrayList;

import oz.game.actor.BallActor;
import oz.game.actor.LineActor;
import oz.game.actor.RectActor;
import oz.game.avoid.MyGdxGame;
import oz.game.base.OzScreen;
import oz.game.base.fontutils.OzFont;
import static oz.game.base.OzUtils.*;
import oz.game.global.G;
import oz.game.global.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class GameScreen extends OzScreen {

	private static final int STATUS_PLAY=-50,STATUS_PLAYTOPAUSE=-200,
			                  STATUS_PAUSE=-100,STATUS_PAUSETOPLAY=-300; 
	private static final float DEFAULT_SPEED = 7F;
	/**默认的计分间隔*/private static final float DEFAULT_SCORE_TIME = 60;
	/**默认的计分时间递减值*/private static final float DEFAULT_SCORE_TIME_DECREMENT = 10;
	/**计分时间的递减值的递减值*/private static final float SCORE_TIME_DECREMENT_DECREMENT = 0.9F;
	/**默认的每一次计分被累加的分值*/private static final int DEFAULT_SCORE_VALUE = 1;
	/**计分累加值的递增值*/private static final int DEFAULT_SCORE_VALUE_INCREMENT = 2;
	/**速度的增量*/private static final float INCREMENT_SPEED = 1F;
	/**下拉框出现和消失时的耗时*/private static final float PAUSE_SHOW_TIME = 0.1f;
    private Image backGround;
	private BallActor ball;
	private RectActor rectA;
	private RectActor rectB;
	private RectActor rectC;
	private RectActor rectD;
	private static float currentSpeed;
	/**计分板*/
	private OzFont scoreFont;
	private static int currentScore;
	/** currentScore +=scoreValue */
	private static int scoreValue;
	private static float currentScoreTime;
	private static float maxScoreTime;
	private static float scoreTimeDecrement;
	private static boolean useNextColor = false;
	private static boolean replaceColorFinish = false;
	/**里面有ABCD*/
	private ArrayList<RectActor> rects;
	private LineActor line;

	/**游戏状态,暂停和游戏状态*/
	private int status;
	
	private ImageButton pauseBtn;
	private Image pauseWindowBg;
	private ImageButton resumeBtn;
	private ImageButton restartBtn;
	private ImageButton mainBtn;
	int i=0;

	public GameScreen(MyGdxGame game, String currentScreenName) {
		super(game, currentScreenName);
		setDefaultStage();

		backGround = new Image(newTexture(G.REFER_SCREEN_WIDTH,G.REFER_SCREEN_HEIGHT,G.GAMESCREEN_BACKGROUND_COLOR));

		pauseBtn = new ImageButton(newTRDrawable("image/pause/pauseUp.png"),
				newTRDrawable("image/pause/pauseDown.png"));
		pauseWindowBg = new Image(newTRDrawable("image/pause/pauseBg.png"));
		resumeBtn= new ImageButton(newTRDrawable("image/pause/resumeUp.png"), newTRDrawable("image/pause/resumeDown.png"));
		restartBtn = new ImageButton(newTRDrawable("image/pause/restartUp.png"), newTRDrawable("image/pause/restartDown.png"));
		mainBtn =  new ImageButton(newTRDrawable("image/pause/mainUp.png"), newTRDrawable("image/pause/mainDown.png"));
		addEvent();
	}
	@Override
	public void reset() {
		scoreValue = DEFAULT_SCORE_VALUE;
		currentScore = 0;
		currentScoreTime = 0;
		maxScoreTime = DEFAULT_SCORE_TIME;
		currentSpeed = DEFAULT_SPEED;
		scoreTimeDecrement = DEFAULT_SCORE_TIME_DECREMENT;
		scoreFont = new OzFont("得分",50, Color.BLACK, newTexture(1, 1, Color.WHITE ));
		Vector2 fontPosition = screenToStageCoordinates(0, Gdx.graphics.getHeight());
		fontPosition.y -= scoreFont.getFontHeight()*2;
		scoreFont.setX(fontPosition.x);
		scoreFont.setY(fontPosition.y);
		scoreFont.setExtraText("得分:");
		
		status = STATUS_PLAY;
		darkAlpha = 1;
		stage.getActors().clear();
		Color rectColor = getRandomColor();
		rectA = new RectActor(G.REFER_SCREEN_HEIGHT,rectColor);
		rectB = new RectActor(rectA,rectColor);
		rectC = new RectActor(rectB,rectColor);
		rectD = new RectActor(rectC,rectColor);
		ball = new BallActor(G.REFER_SCREEN_WIDTH/2);
		if(rects==null){
			rects = new ArrayList<RectActor>();
		}
		rects.clear();
		rects.add(rectA);rects.add(rectB);rects.add(rectC);rects.add(rectD);
		line = new LineActor();
		LineActor.playWith(ball);
		RectActor.playWith(ball);
		stage.addActor(backGround);
		stage.addActor(line);
		stage.addActor(rectA);
		stage.addActor(rectB);
		stage.addActor(rectC);
		stage.addActor(rectD);
		stage.addActor(ball);
		Vector2 pauseBtnPosition = screenToStageCoordinates(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		pauseBtnPosition.x -= pauseBtn.getWidth();
		pauseBtnPosition.y -= pauseBtn.getHeight();
		pauseBtn.setPosition(pauseBtnPosition.x, pauseBtnPosition.y);
		stage.addActor(pauseBtn);
		pauseWindowBg.setPosition(0,-pauseWindowBg.getHeight());
		this.refreshPauseBtnsPosition();
		stage.addActor(scoreFont);
		stage.addActor(pauseWindowBg);
		stage.addActor(resumeBtn);
		stage.addActor(restartBtn);
		stage.addActor(mainBtn);
	}
	@Override
	public void addEvent() {
		stage.addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode==BallActor.DIR_LEFT){
					ball.setDir(BallActor.DIR_LEFT);
				}
				else if(keycode==BallActor.DIR_RIGHT){
					ball.setDir(BallActor.DIR_RIGHT);
				}
				return true;
			}
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if(keycode==ball.getDir()){
					ball.setDir(BallActor.DIR_FREE);
				}
				return true;
			}
			
		});
		pauseBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				status = STATUS_PLAYTOPAUSE;

				pauseWindowBg.addAction(Actions.moveTo(0,0,PAUSE_SHOW_TIME));
				for(RectActor rect:rects){
					rect.setUseLogic(false);
					rect.addAction(Actions.moveTo(rect.getX(),rect.getY()+pauseWindowBg.getHeight(), PAUSE_SHOW_TIME));
				}
				ball.setUseLogic(false);
				ball.addAction(Actions.moveTo(ball.getX(),ball.getY()+pauseWindowBg.getHeight(), PAUSE_SHOW_TIME));
				line.setUseLogic(false);
				line.addAction(Actions.moveTo(line.getX(),line.getY()+pauseWindowBg.getHeight(), PAUSE_SHOW_TIME));
				pauseBtn.addAction(Actions.moveTo(pauseBtn.getX(),pauseBtn.getY()+pauseWindowBg.getHeight(),PAUSE_SHOW_TIME));
				return true;
			}
		});
		resumeBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if(x>0&&x<resumeBtn.getWidth()&&y>0&&y<resumeBtn.getHeight()){
					status = STATUS_PAUSETOPLAY;

					pauseWindowBg.addAction(Actions.moveTo(0,-pauseWindowBg.getHeight(),PAUSE_SHOW_TIME));
					for(RectActor rect:rects){
						rect.addAction(Actions.moveTo(rect.getX(),rect.getY()-pauseWindowBg.getHeight(), PAUSE_SHOW_TIME));
					}
					ball.addAction(Actions.moveTo(ball.getX(),ball.getY()-pauseWindowBg.getHeight(), PAUSE_SHOW_TIME));
					line.addAction(Actions.moveTo(line.getX(),line.getY()-pauseWindowBg.getHeight(), PAUSE_SHOW_TIME));
					pauseBtn.addAction(Actions.moveTo(pauseBtn.getX(),pauseBtn.getY()-pauseWindowBg.getHeight(),PAUSE_SHOW_TIME));
				}
			}
		});
		restartBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if(x>0&&x<restartBtn.getWidth()&&y>0&&y<restartBtn.getHeight()){
					setRefreshScreen(true);//重新进入此screen
				}
			}
		});
		mainBtn.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {return true;}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if(x>0&&x<restartBtn.getWidth()&&y>0&&y<restartBtn.getHeight()){
					setScreen(Screens.MAIN);
				}
			}
		});
	}
	
	@Override
	public boolean begin(float delta) {
		stage.act();
		stage.draw();
		
		darkAlpha-=0.05f;
		if(darkAlpha<=0){
			return true;	
		}
		else{
			setDarkness(darkAlpha);
		}
		return false;
	}
	@Override
	public void actAndDraw(float delta) {
		if(status==STATUS_PLAY){
			this.play();
		}
		else if(status==STATUS_PLAYTOPAUSE){
			line.act(delta);
			for(RectActor rect:rects){
				rect.act(delta);
			}
			ball.act(delta);
			pauseBtn.act(delta);
			pauseWindowBg.act(delta);
			refreshPauseBtnsPosition();
			stage.draw();
			if(pauseWindowBg.getActions().size==0){
				status = STATUS_PAUSE;
			}
		}
		else if(status == STATUS_PAUSE){
			stage.draw();
		}
		else if(status == STATUS_PAUSETOPLAY){
			line.act(delta);
			for(RectActor rect:rects){
				rect.act(delta);
			}
			ball.act(delta);
			pauseBtn.act(delta);
			pauseWindowBg.act(delta);
			refreshPauseBtnsPosition();
			stage.draw();
			if(pauseWindowBg.getActions().size==0){
				for(RectActor rect:rects){
					rect.setUseLogic(true);
				}
				ball.setUseLogic(true);
				line.setUseLogic(true);
				status = STATUS_PLAY;
			}
		}
		
	}
	
	private void play(){
		stage.act();
		if(line.replaceColorFinish() 
				&& rectA.replaceColorFinish()
				&& rectB.replaceColorFinish()
				&& rectC.replaceColorFinish()
				&& rectD.replaceColorFinish()
				){
			setReplaceColorFinish(true);
		}
		if(isUseNextColor()){
			if(isReplaceColorFinish()){
				line.replaceColor(getRandomColor());
				Color rectColor = getRandomColor();
				rectA.replaceColor(rectColor);
				rectB.replaceColor(rectColor);
				rectC.replaceColor(rectColor);
				rectD.replaceColor(rectColor);
				setReplaceColorFinish(false);
			}
			setUseNextColor(false);
		}
		if(currentScoreTime>=maxScoreTime){
			currentScoreTime = 0;
			currentScore += scoreValue;
		}
		else{
			currentScoreTime++;
		}
		scoreFont.setExtraText("得分: "+currentScore);
		stage.draw();
	}

	@Override
	public boolean end(float delta) {
		stage.draw();
		setDarkness(darkAlpha);
		darkAlpha += dAlpha;
		if(darkAlpha<1){
			
			return false;
		}
		darkAlpha = 1;
		return true;
	}


	/**刷新暂停菜单里那三个按钮所处的位置*/
	private void refreshPauseBtnsPosition(){
		resumeBtn.setPosition(pauseWindowBg.getX()+14,pauseWindowBg.getY()+10);
		restartBtn.setPosition(resumeBtn.getRight()+14, resumeBtn.getY());
		mainBtn.setPosition(restartBtn.getRight()+14, resumeBtn.getY());
	}
	


	@Override
	public void hide() {
		drawDarkness(1f);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		System.out.println("GameScreen.resume()");
		
	}
	


	@Override
	public void dispose() {
		
	}
	/**加速并刷新积分增加规则*/
	public static void increateSpeed(){
		currentSpeed += INCREMENT_SPEED;
		if(maxScoreTime>=(scoreTimeDecrement+1)){	
			maxScoreTime -= scoreTimeDecrement;
			if(scoreTimeDecrement>1){
				 scoreTimeDecrement = scoreTimeDecrement*SCORE_TIME_DECREMENT_DECREMENT;
				 scoreTimeDecrement = scoreTimeDecrement>1?scoreTimeDecrement:1;
				 System.out.println("scoreTimeDecrement = "+scoreTimeDecrement);
			}
			else {
				scoreTimeDecrement = 1;
				System.out.println("scoreTimeDecrementelse");
			}
			
		}else{
			scoreValue +=DEFAULT_SCORE_VALUE_INCREMENT;
		}
	}
	public static float getCurrentSpeed(){
		return currentSpeed;
	}

	/**当转变颜色完成之后才允许进行下一次颜色的更换*/
	public static boolean isReplaceColorFinish() {return replaceColorFinish;}
	public static void setReplaceColorFinish(boolean replaceColorFinish) {GameScreen.replaceColorFinish = replaceColorFinish;}
	/**设置是否要更换颜色*/
	public static boolean isUseNextColor() {return useNextColor;}
	public static void setUseNextColor(boolean useNextColor) {GameScreen.useNextColor = useNextColor;}



}
