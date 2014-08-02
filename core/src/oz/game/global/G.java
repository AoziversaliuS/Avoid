package oz.game.global;

import oz.game.base.OzUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class G {
	
	public static final int REFER_SCREEN_WIDTH = 720,REFER_SCREEN_HEIGHT = 1280;
	
//	/**LineActor和RectActor共用的speed.*/
//	private static float publicSpeed = 7;
//	private static float dSpeed = 1f;
//	public static float getSpeed(){return publicSpeed;}
//	/**增加公共速度publicSpeed*/
//	public static void increateSpeed(){publicSpeed += dSpeed;}
//	public static void resetSpeed(){ publicSpeed = 7;}

	private static boolean useNextColor = false;
	private static boolean replaceColorFinish = false;
	/**当转变颜色完成之后才允许进行下一次颜色的更换*/
	public static boolean isReplaceColorFinish() {return replaceColorFinish;}
	public static void setReplaceColorFinish(boolean replaceColorFinish) {G.replaceColorFinish = replaceColorFinish;}
	/**设置是否要更换颜色*/
	public static boolean isUseNextColor() {return useNextColor;}
	public static void setUseNextColor(boolean useNextColor) {G.useNextColor = useNextColor;}
	
	public static final Color GAMESCREEN_BACKGROUND_COLOR = Color.BLACK;
	public static final Color MAINSCREEN_BACKGROUND_COLOR = Color.GRAY;
	
	private static Sprite screenShot;
	public static Sprite getScreenShotSprite(){
		return screenShot;
	}
	/**将当前屏幕作为截屏图片*/
	public static void updateScreenShot(Stage stage){
		float start = System.currentTimeMillis();
		stage.getViewport().update();
		Texture tex = OzUtils.getScreenShot(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		screenShot = new Sprite(tex);
		screenShot.flip(false, true);
		Vector2 size = stage.screenToStageCoordinates(new Vector2(Gdx.graphics.getWidth(),0));
		System.out.println("size.x = "+size.x+"  size.y = "+ size.y);
		screenShot.setSize(size.x, size.y+2);
		float cost = System.currentTimeMillis() - start;
		Gdx.app.log("TIMECOST", cost+"毫秒");
	}
	
}
