package oz.game.base;

import oz.game.avoid.MyGdxGame;
import oz.game.global.G;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
/**不能重写show和render方法,否则底层将不起作用,begin结束后自动设置监听为stage*/
public abstract class OzScreen implements Screen{

	private static final boolean SHOW_FPS = false,DEBUG = false;
	private static final long SLEEP_TIME = 150;
	
	public MyGdxGame game;
	private boolean began = false;
	private String currentScreenName;
	private String toScreenName;
	private SpriteBatch batch;
	private Sprite  darkpic;
	private Sprite  darkpic2;
	/**从自身切换到自身屏幕,自我切换*/
	private boolean refreshScreen = false;
	
	public  Skin skin;
	public Stage stage;
	public float darkAlpha=0;
	public static final float dAlpha = 0.05f;
	
	
	public OzScreen(MyGdxGame game,String currentScreenName) {
		this.game = game;
		this.currentScreenName = currentScreenName;
		this.toScreenName = currentScreenName;
		//skin
		 Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
	     pixmap.setColor(Color.WHITE);
	     pixmap.fill();
	     skin = new Skin();
	     skin.add("color", new Texture(pixmap));
	     //明暗度设置
	     batch = new SpriteBatch();
//	     Texture blackImg = newTexture(G.REFER_SCREEN_WIDTH, G.REFER_SCREEN_HEIGHT, Color.BLACK);
	     Texture blackImg = OzUtils.newTexture(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLACK);
	     darkpic = new Sprite(blackImg);
	     darkpic2 =  new Sprite(blackImg);
	     //默认设置背景颜色为黑色
	}
	
	/**用于添加监听事件,在show方法里会在load之后自动调用 */
	public void addEvent(){
		
	}
	
	/**切换到此screen时,在begin()返回true前一直执行此方法,*/
	public abstract boolean begin(float delta);
	/**当end()返回true之后切换到toScreenName指定的下一个screen,否则一直执行end()方法.*/
	public abstract boolean end(float delta);
	/**在此方法内处理逻辑和画图*/
	public abstract void actAndDraw(float delta);
	/**重设变量值等,在show方法里最后自动调用*/
	public abstract void reset();
	/**加载资源等,在show方法里自动调用(在reset调用之前被调用)*/
	public abstract void load();
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//先执行begin()直到begin()返回true,然后一直执行actAndDraw(),
		//当需要切换屏幕时一直执行end(),end()返回true时切换到新的screen,然后让新的screen执行begin....
		if(!began){
			began = this.begin(delta);
			if(began){
				Gdx.input.setInputProcessor(stage);
			}
		}
		else if(currentScreenName.equals(toScreenName) && !refreshScreen){
			this.actAndDraw(delta);
		}
		else{
			if(this.end(delta)){
				game.setScreen(game.screenMap.get(toScreenName));
			}
		}
		batch.begin();
		darkpic.draw(batch);
		batch.end();
		devMode();
	}
	private void devMode(){
		if(SHOW_FPS){
			Gdx.app.log("FPS", Gdx.graphics.getFramesPerSecond()+"");
		}
		if(DEBUG){
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	@Override
	public void show() {
		//重置值
		Gdx.app.log("OZstatus", " show()");
		began = false;
		refreshScreen = false;
		toScreenName = currentScreenName;
	     darkpic.setAlpha(0);
	     this.load();
	     this.addEvent();
		this.reset();
	}
	
	
	
	@Override
	public void resize(int width, int height) {
		if(stage!=null){
			stage.getViewport().update(width, height);
		}
	}
	/**获取fillX时适应目标屏幕之后超出屏幕之外的部分高度大小,用的是stage的比例.*/
	public float getOutOfScreenSize(){

		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float targetWidth = G.REFER_SCREEN_WIDTH;
		float targetHeight = targetWidth/(screenWidth/screenHeight);
		//超出屏幕的大小,按照stage的比例来计算
		float outOfScreenSize = G.REFER_SCREEN_HEIGHT - targetHeight;
		
		if(outOfScreenSize>0){
			return outOfScreenSize;
		}
		return 0;
	}
	
	public Batch getStageBatch(){
		if(stage!=null){
			return stage.getBatch();
		}
		return null;
	}

	
	/**设置toScreenName值*/
	public void setScreen(String screenName){
		this.toScreenName = screenName;
	}
	/**设置暗度,0-1*/
	public void setDarkness(float darkAlpha){
		darkpic.setAlpha(darkAlpha);
	}
	/**马上把暗幕画出来*/
	public void drawDarkness(float darkAlpha){
		darkpic2.setAlpha(darkAlpha);
		batch.begin();
		darkpic2.draw(batch);
		batch.end();
	}
	/**将Texture加到skin里,并设置抗锯齿*/
	public void addTexture(String name,Texture texture){
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		skin.add(name, texture);
	}
	/**设置默认的stage,使用fillx方式,并且camera移动到最底部*/
	public void setDefaultStage(){
		stage = new Stage(new ScalingViewport(Scaling.fillX, G.REFER_SCREEN_WIDTH,G.REFER_SCREEN_HEIGHT));
		//让camera移动到屏幕的底部的中心位置.
		stage.getCamera().position.y = stage.getCamera().position.y-getOutOfScreenSize()/2;
		stage.getViewport().update();
	}
	
	/**从当前的screen切换到当前的screen*/
	public void setRefreshScreen(boolean refreshScreen) {
		this.refreshScreen = refreshScreen;
	}

	public Stage getStage() {
		return stage;
	}
	/**
	 * 笛卡尔坐标系的屏幕坐标切换成stage的坐标,stage为null则返回null
	 * @param x 屏幕坐标x
	 * @param y 屏幕坐标y*/
	public Vector2 screenToStageCoordinates(float x,float y){
		Vector2 position = null;
		if(stage!=null){
			position = stage.screenToStageCoordinates(new Vector2(x,Gdx.graphics.getHeight()-y));
		}
		return position;
	}
	
	
	
	
	






}
