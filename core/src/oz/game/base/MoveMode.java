package oz.game.base;

public abstract class MoveMode {
	
	private float speed;
	
	/**获取下一帧要移动的距离大小,负数为向左移动,正数为向右移动*/
	public abstract float getDx();
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
}
