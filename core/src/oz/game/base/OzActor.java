package oz.game.base;

import oz.game.global.G;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class OzActor extends Actor{
	
	private Rectangle body;
	private boolean useLogic = true;
	@Override
	public void act(float delta) {
		if(useLogic){
			this.logic(delta);
		}
		super.act(delta);
	}
	
	/**调用Actor的act方法时都会先调用此方法处理逻辑先.之后再执行未完成的Action*/
	public  void logic(float delta){
		
	}
	/**重置,要使用此方法请在适当场合自行调用.*/
	public void reset(){
		
	}
	/**若碰到另外一个矩形Actor,则返回true,特殊情况可自行重写此方法.*/
	public boolean impact(OzActor otherActor){
		return getBody().overlaps(otherActor.getBody());
	}
	/**获取Actor的body(用于碰撞检测)*/
	public Rectangle getBody(){
		if(body==null){
			body = new Rectangle();
		}
		body.x = getX();
		body.y = getY();
		body.width = getWidth();
		body.height = getHeight();
		return body;
	}
	
	
	
	public void setUseLogic(boolean useLogic) {
		this.useLogic = useLogic;
	}

	/**返回一个在屏幕范围内的左下角X坐标.*/
	public float getRandomX(){
		return MathUtils.random(0, G.REFER_SCREEN_WIDTH-getWidth());
	}
	/**返回一个在屏幕范围内的中心X坐标.*/
	public float getRandomCenterX(){
		return MathUtils.random(getWidth()/2, G.REFER_SCREEN_WIDTH-getWidth()/2);
	}
	
}
