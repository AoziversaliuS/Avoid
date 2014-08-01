package oz.game.movemode;

import com.badlogic.gdx.Gdx;

import oz.game.base.MoveMode;

public class AccelerateMove extends MoveMode{
	
	private static final boolean DEBUG_AX = false;
	
	private static final float RATIO = 0.7f;
	/**重力值超过 +-0.8f才有效*/
	private static final float lIMIT_VALUE = 0.5f;
	public AccelerateMove() {
	}
	
	public AccelerateMove(float speed) {
		setSpeed(speed);
	}
	//重力感应,左倾斜为正,右倾斜为负,但实际上要反过来.
	@Override
	public float getDx() {
		if(DEBUG_AX){
			Gdx.app.log("AccelerometerX", Gdx.input.getAccelerometerX()+"");
		}
		float dx = 0;
		float ax = Gdx.input.getAccelerometerX();
		if(Math.abs(ax)>=lIMIT_VALUE){
			if(ax>0){
				dx = -getSpeed()-(ax*getSpeed()*RATIO);
			}
			else{
				dx = getSpeed()-(ax*getSpeed()*RATIO);
			}
		}
		
		return dx;
	}

}
