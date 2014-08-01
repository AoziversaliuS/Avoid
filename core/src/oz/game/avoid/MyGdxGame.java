package oz.game.avoid;

import java.util.HashMap;
import java.util.Map;

import oz.game.base.OzScreen;
import oz.game.global.Screens;
import oz.game.screen.GameScreen;
import oz.game.screen.MainScreen;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	
	public Map<String, OzScreen> screenMap;
	@Override
	public void create () {
		screenMap = new HashMap<String, OzScreen>();

		screenMap.put(Screens.MAIN, new MainScreen(this,Screens.MAIN));
		screenMap.put(Screens.GAME, new GameScreen(this,Screens.GAME));
		
		this.setScreen(screenMap.get(Screens.MAIN));
	}

}
