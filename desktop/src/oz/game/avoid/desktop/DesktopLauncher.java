package oz.game.avoid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import oz.game.avoid.MyGdxGame;
import oz.game.avoid.font.DeskItextToImage;
import oz.game.base.fontutils.TextControl;
import oz.game.global.G;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//接口实现
		TextControl.setITextToImage(new DeskItextToImage());
//		config.width = 320;
//		config.height = 640;
		
//		config.width = 720/2;
//		config.height = 1500/2;
		
		config.width = 720/2;
		config.height = 1280/2;
		
//		config.width = G.REFER_SCREEN_WIDTH/2;
//		config.height = G.REFER_SCREEN_HEIGHT/2;
		
//		config.width = 640/2;
//		config.height = 960/2;
//		
		new LwjglApplication(new MyGdxGame(), config);
	}
}
