package oz.game.actor;

import oz.game.base.MoveMode;
import oz.game.base.OzActor;
import oz.game.base.OzScreen;
import oz.game.global.G;
import oz.game.movemode.AccelerateMove;
import oz.game.screen.GameScreen;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


public class BallActor extends OzActor {
	
    private static final float BALL_CENTER_Y = 100;
    private static final float BALL_RADIUS = 30;
    private static final float BALL_SPEED = 5F;
    private static final float SCALE_SIZE = 100;
    private static final float SCALE_SPEED = 1F;
    /**PC版的移动速度*/
    private static final float BALL_PC_SPEED = BALL_SPEED*2-BALL_SPEED/2;
    public static final int DIR_LEFT = 21;
    public static final int DIR_RIGHT = 22;
    /**空闲不动的值*/
    public static final int DIR_FREE = -100;
    
	private Sprite ballSpriteA;
	private Sprite ballSpriteB;
	private float radius;
	private MoveMode moveMode;
	private float speed;
	private int dir;
	private boolean alive;
	private GameScreen screen;
	public BallActor(float centerX,GameScreen screen) {
		this.screen = screen;
		setName("Ball");
		this.radius = BALL_RADIUS;
		this.speed = BALL_SPEED;
		this.dir = DIR_FREE;
		setWidth(radius*2);
		setHeight(radius*2);
		//通过中心点坐标来设置小球位置要在设置宽高之后
		setCenterPosition(centerX, BALL_CENTER_Y);
		
		Texture ballTexA = new Texture("image/gameplay/ballA.png");
		Texture ballTexB = new Texture("image/gameplay/ballB.png");
		ballTexA.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ballTexB.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ballSpriteA = new Sprite(ballTexA);
		ballSpriteA.setSize(radius*2, radius*2);
		ballSpriteA.setOriginCenter();
		ballSpriteB = new Sprite(ballTexB);
		ballSpriteB.setSize(radius*2, radius*2);
		ballSpriteB.setOriginCenter();
		//设置移动方式
		moveMode = new AccelerateMove(speed);
		alive = true;
	}

	@Override
	public void logic(float delta) {
		if(alive){
			//手机版重力感应
			float nextPositionX = getX()+moveMode.getDx();
			//如果小球移动之后的坐标还位于屏幕之内,则让小球移动对应的距离,否则将小球设置在边缘
			float dx = 0;//左正右负
			if(nextPositionX<0){
				dx = getX();//左为正
				setX(0);
			}
			else if((nextPositionX+getWidth())>G.REFER_SCREEN_WIDTH){
				dx =getRight() - G.REFER_SCREEN_WIDTH;//右为负
				setX(G.REFER_SCREEN_WIDTH-getWidth());
			}
			else{
				dx = getX() - nextPositionX;
				setX(nextPositionX);
			}
			//PC版按键移动
			if(dir==DIR_LEFT){
				if(getX()-BALL_PC_SPEED<0){
					dx = getX();//左为正
					setX(0);
				}
				else{
					dx = BALL_PC_SPEED;
					setX(getX()-BALL_PC_SPEED);
				}
			}
			else if(dir==DIR_RIGHT){
				if((getRight()+BALL_PC_SPEED)>G.REFER_SCREEN_WIDTH){
					dx =getRight() - G.REFER_SCREEN_WIDTH;//右为负
					setX(G.REFER_SCREEN_WIDTH-getWidth());
				}
				else{
					dx = -BALL_PC_SPEED;
					setX(getX()+BALL_PC_SPEED);
				}
			}
			setRotation(getRotation()+getDegrees(dx));
		}
		else{
			ballSpriteA.setScale(getScaleX(), getScaleY());
			ballSpriteB.setScale((SCALE_SIZE-getScaleX())/SCALE_SIZE);
			if(getScaleX()>=SCALE_SIZE){
				screen.showGameOverWindow();
			}
		}
		
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		ballSpriteB.setPosition(getX(), getY());
		ballSpriteB.setRotation(getRotation());
		ballSpriteB.draw(batch);
		ballSpriteA.setPosition(getX(), getY());
		ballSpriteA.setRotation(getRotation());
		ballSpriteA.draw(batch);
	}
	
	/**根据弧度(移动的距离)获取角度,小球左移弧度为正,右移弧度为负
	 * @param radian 弧度
	 */
	private float getDegrees(float radian){
		float girth = (float) Math.PI * getWidth();//直径*PI=周长
		float degrees = 0;
		int sign = 1;//暂存radian(弧度)的符号,左为正,右为负
		sign = radian>0?1:-1;
		while( Math.abs(radian)>girth){
			radian = Math.abs(radian)-girth;
		}
		radian = Math.abs(radian);
		degrees = (radian/girth)*360;
		//还原角度的方向(符号)
		degrees = sign*degrees;
		return degrees;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		if(this.alive){
			this.addAction(Actions.scaleTo(SCALE_SIZE, SCALE_SIZE, SCALE_SPEED));
		}
		this.alive = alive;
	}
	

}
