package oz.game.avoid.android;

import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import oz.game.avoid.MyGdxGame;
import oz.game.avoid.font.AndroidItextToImage;
import oz.game.base.fontutils.TextControl;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {

		//设置屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//接口实现
		TextControl.setITextToImage(new AndroidItextToImage());
		super.onCreate(savedInstanceState);

		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGdxGame(), config);
	}
}
