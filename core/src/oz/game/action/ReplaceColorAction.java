package oz.game.action;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class ReplaceColorAction extends TemporalAction {

	/**spriteA是底层的图片,B是表层的图片*/
	private Sprite spriteA,spriteB;
	private Color targetColor;
	/**在begin→update→end之间为false,其余时间为true*/
	private boolean finish=true;
	
	public ReplaceColorAction(Sprite spriteA,Sprite spriteB,Color targetColor) {
		this.spriteA = spriteA;
		this.spriteB = spriteB;
		this.targetColor = targetColor;
	}
	public ReplaceColorAction(float duration) {
		super(duration);
	}
	public ReplaceColorAction(Sprite spriteA,Sprite spriteB,Color targetColor,float duration) {
		super(duration);
		this.spriteA = spriteA;
		this.spriteB = spriteB;
		this.targetColor = targetColor;
	}

	public ReplaceColorAction(Sprite spriteA,Sprite spriteB,Color targetColor,float duration, Interpolation interpolation) {
		super(duration, interpolation);
		this.spriteA = spriteA;
		this.spriteB = spriteB;
		this.targetColor = targetColor;
	}

	@Override
	protected void update(float percent) {
		spriteB.setSize(spriteA.getWidth(), percent*spriteA.getHeight());
		spriteB.setColor(targetColor);
		
	}
	@Override
	protected void begin() {
		finish = false;
	}
	
	@Override
	protected void end() {
		spriteA.setColor(spriteB.getColor());
		Sprite buff = spriteA;
		spriteA = spriteB;
		spriteB = buff;
		//颜色转换完成后,让A和B的绘画顺序互相调换.
		finish = true;
	}
	
	public void set(Sprite spriteA,Sprite spriteB,Color targetColor){
		this.spriteA = spriteA;
		this.spriteB = spriteB;
		this.targetColor = targetColor;
	}
	@Override
	public void reset() {
		finish = true;
		super.reset();
	}
	
	
	public boolean isFinish() {
		return finish;
	}
	
	
	
	

}
