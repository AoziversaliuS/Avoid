package oz.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import oz.game.action.ReplaceColorAction;
import oz.game.base.OzActor;
import oz.game.base.OzUtils;
import oz.game.global.G;

public class LineActor extends OzActor{

	private static final float WIDTH = 200;
	private static final float HEIGHT = 25;
	/**线条隔MAX_COUNT出现一次*/
	private static final int MAX_COUNT = 150;
	/**转换颜色的持续时间*/
	private static float REPLACE_COLOR_DURATION = 0.2f;
	
	private static BallActor ball;
	
	private Rectangle realBodyA;
	private Rectangle realBodyB;
	
	private Sprite lineSpriteA;
	private Sprite lineSpriteB;
	private Sprite overlapSprite;
	
	private int count;
	/**判断是否已经触碰过了,若已经触碰过了则忽视此次触碰*/
	private boolean impacted;
	private ReplaceColorAction replaceColorAction;


	public LineActor() {
		setWidth(WIDTH);
		setHeight(HEIGHT);
		setX(getRandomX());
		setY(G.REFER_SCREEN_HEIGHT);
		count = 0;
		impacted = false;
		lineSpriteA = new Sprite(OzUtils.newTexture(G.REFER_SCREEN_WIDTH,(int)getHeight(),Color.WHITE));
		lineSpriteB =  new Sprite(OzUtils.newTexture(G.REFER_SCREEN_WIDTH,(int)getHeight(),Color.WHITE));
//		setColor(Color.GREEN);
		lineSpriteA.setColor(OzUtils.getRandomColor());
		lineSpriteB.setColor(lineSpriteA.getColor());
		overlapSprite = new Sprite(OzUtils.newTexture((int)getWidth(),(int)getHeight(), G.GAMESCREEN_BACKGROUND_COLOR));
		replaceColorAction = new ReplaceColorAction(REPLACE_COLOR_DURATION);
	}


	@Override
	public void logic(float delta) {
		count++;
		if(count>=MAX_COUNT){
			if(getTop()<0){
				count = 0;
				setY(G.REFER_SCREEN_HEIGHT);
				setX(getRandomX());
			}
			else{
				setY(getY()-G.getSpeed());
			}
		}
		if(impact(ball)){
			if(!impacted){
				G.setUseNextColor(true);
				G.increateSpeed();
				impacted = true;
			}
		}
		else{
			impacted = false;
		}
		
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {

		lineSpriteA.setPosition(0, getY());
		lineSpriteA.draw(batch);
		lineSpriteB.setPosition(0, getY());
		lineSpriteB.draw(batch);
		overlapSprite.setPosition(getX(), getY());
		overlapSprite.draw(batch);
	}
	/**刷新realBodyA和realBodyB.*/
	private void refreshRealBody(){
		if(realBodyA==null){
			realBodyA = new Rectangle();
		}
		if(realBodyB==null){
			realBodyB = new Rectangle();
		}
		realBodyA.x = 0;
		realBodyA.y = getBody().y;
		realBodyA.height = getBody().height;
		realBodyA.width = getBody().x;
		
		realBodyB.x = getBody().x + getBody().width;
		realBodyB.y = getBody().y;
		realBodyB.height = getBody().height;
		realBodyB.width = G.REFER_SCREEN_WIDTH - realBodyB.x;
	}
	@Override
	public boolean impact(OzActor otherActor) {
		refreshRealBody();
		if(realBodyA.overlaps(otherActor.getBody())){
			return true;
		}
		else if(realBodyB.overlaps(otherActor.getBody())){
			return true;
		}
		return false;
	}
	public void replaceColor(Color targetColor){
		replaceColorAction.reset();
		replaceColorAction.set(lineSpriteA, lineSpriteB, targetColor);
		this.addAction(replaceColorAction);
	}
	public boolean replaceColorFinish(){
		return replaceColorAction.isFinish();
	}
	
	/**设置与哪个球进行互动*/
	public static void playWith(BallActor ball){
		LineActor.ball = ball;
	}

	
}
