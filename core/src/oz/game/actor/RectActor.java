package oz.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import oz.game.action.ReplaceColorAction;
import oz.game.base.OzActor;
import oz.game.base.OzUtils;
import oz.game.global.G;
import oz.game.screen.GameScreen;

public class RectActor extends OzActor{
	/**在此方块之上放置另外一个方块的距离*/
	private static int NEXT_RECT_LIMIT_Y = 230;
	/**转换颜色的持续时间*/
	private static float REPLACE_COLOR_DURATION = 0.3f;
	
	private static RectActor lastRectActor;
	private static BallActor ball;
	
	private static int WIDTH = 60;
	private static int HEIGHT = 160;
	/**最上面的那个方块*/
	private Sprite rectSpriteA;
	private Sprite rectSpriteB;
	private ReplaceColorAction replaceColorAction;
	
	
	
	public RectActor(float centerX,float centerY,Color color) {
		this(color);
		setCenterPosition(centerX, centerY);
	}
	public RectActor(float centerY,Color color) {
		this(color);
		setCenterPosition(getRandomCenterX(), centerY);
	}
	public RectActor(RectActor referRectActor,Color color) {
		this(color);
		setX(getRandomX());
		setY(referRectActor.getTop()+NEXT_RECT_LIMIT_Y);
		//设置此方块为最上面的那个方块
		lastRectActor = this;
	}
	

	public RectActor(Color color) {
		setWidth(WIDTH);
		setHeight(HEIGHT);
		setColor(color);
		Texture rectTex = OzUtils.newTexture(WIDTH, HEIGHT, Color.WHITE);
		rectSpriteA = new Sprite(rectTex);
		rectSpriteB = new Sprite(rectTex);
		rectSpriteA.setColor(getColor());
		rectSpriteB.setColor(getColor());
		replaceColorAction = new ReplaceColorAction(REPLACE_COLOR_DURATION);
	}
	@Override
	public void logic(float delta) {
		if((getTop()-GameScreen.getCurrentSpeed())<0/*判断移动后的位置是否小于0*/){
			setX(getRandomX());
			setY(lastRectActor.getTop()+NEXT_RECT_LIMIT_Y);
			lastRectActor = this;//将此方块设置为最上面的那一个方块
		}
		else{
			setY( getY()-GameScreen.getCurrentSpeed());
		}
		//碰撞检测
		if(this.impact(ball)){
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		rectSpriteA.setPosition(getX(),  getY());
		rectSpriteA.draw(batch);
		rectSpriteB.setPosition(getX(),  getY());
		rectSpriteB.draw(batch);
	}
	
	public void replaceColor(Color targetColor){
		replaceColorAction.reset();
		replaceColorAction.set(rectSpriteA, rectSpriteB, targetColor);
		this.addAction(replaceColorAction);
	}
	public boolean replaceColorFinish(){
		return replaceColorAction.isFinish();
	}

	/**设置与哪个球进行碰撞检测的互动*/
	public static void playWith(BallActor ball){
		RectActor.ball = ball;
	}

}
