package oz.game.base.fontutils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import static oz.game.base.OzUtils.*;
import oz.game.base.OzActor;


public class OzFont extends OzActor{

	private BitmapFont font;
	private Texture backGround;
	
	private String wordsWillBeUse;
	private StringBuilder extraText = null;
	
	private float bgX;
	private float bgY;
	private float bgWidth;
	private float bgHeight;
	
	public OzFont(float x,float y,String wordsWillBeUse,int fontSize,Color color,Texture backGround) {
		this.wordsWillBeUse = wordsWillBeUse;
		setColor(color);
		this.font = newBitmapFont(wordsWillBeUse, fontSize,color);
		this.backGround = backGround;
		setX(x);
		setY(y);
	}
	public OzFont(float x,float y,String wordsWillBeUse,int fontSize,Texture backGround) {
		this(x,y,wordsWillBeUse,fontSize,Color.WHITE,backGround);
	}
	public OzFont(float x,float y,String wordsWillBeUse,int fontSize) {
		this(x,y,wordsWillBeUse,fontSize,Color.WHITE,null);
	}
	public OzFont(String wordsWillBeUse,int fontSize,Color color,Texture backGround) {
		this(0,0,wordsWillBeUse,fontSize,color,backGround);
	}
	public OzFont(String wordsWillBeUse,int fontSize,Texture backGround) {
		this(wordsWillBeUse,fontSize,Color.WHITE,backGround);
	}
	public OzFont(String wordsWillBeUse,int fontSize) {
		this(wordsWillBeUse,fontSize,Color.WHITE,null);
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(backGround!=null) batch.draw(backGround, bgX,bgY, bgWidth,bgHeight);
		if(extraText==null)  font.draw(batch, wordsWillBeUse,getX(),getY());
		else                 font.draw(batch, extraText,getX(),getY());
	}
	/**设置偏移值*/
	public void editBgX(float bgX) {
		this.bgX += bgX;
	}
	/**设置偏移值*/
	public void editBgY(float bgY) {
		this.bgY += bgY;
	}
	/**设置偏移值*/
	public void editBgWidth(float bgWidth) {
		this.bgWidth += bgWidth;
	}
	/**设置偏移值*/
	public void editBgHeight(float bgHeight) {
		this.bgHeight += bgHeight;
	}
	/**设置偏移值*/
	public void edit(float bgX,float bgY,float bgWidth,float bgHeight){
		editBgX(bgX);
		editBgY(bgY);
		editBgWidth(bgWidth);
		editBgHeight(bgHeight);
	}
	
	public float getFontWidth(){
		if(extraText==null){
			return font.getBounds(wordsWillBeUse).width;
		}
		return font.getBounds(extraText).width;
	}
	public float getFontHeight(){
		if(extraText==null){
			return font.getBounds(wordsWillBeUse).height;
		}
		return font.getBounds(extraText).height;
	}
	/**这里输入的字符必须在text里有才能正常显示,设置完后会替换掉先前的extraText.*/
	public void setExtraText(String extraText) {
		if(this.extraText==null){
				this.extraText = new StringBuilder(extraText);
		}
		else{
			this.extraText.replace(0,  this.extraText.length(), extraText);
		}
		refreshBgBounds();
	}
	/**刷新背景*/
	private void refreshBgBounds(){
		if(extraText!=null){
			bgWidth = font.getBounds(extraText).width;
			bgHeight = font.getBounds(extraText).height;
			bgX = getX();
			bgY = getY()-font.getBounds(extraText).height;
		}
		else if(wordsWillBeUse!=null){
			bgWidth = font.getBounds(wordsWillBeUse).width;
			bgHeight = font.getBounds(wordsWillBeUse).height;
			bgX = getX();
			bgY = getY()-font.getBounds(wordsWillBeUse).height;
		}
	}
	
	@Override
	public void setX(float x) {
		super.setX(x);
		refreshBgBounds();
	}
	@Override
	public void setY(float y) {
		super.setY(y);
		refreshBgBounds();
	}
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		refreshBgBounds();
	}
	@Override
	public void setCenterPosition(float x, float y) {
		super.setCenterPosition(x, y);
		refreshBgBounds();
	}
	
	
	
	
	

}
