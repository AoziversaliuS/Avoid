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
	
	private String text;
	private String extraText = null;
	
	private float bgX;
	private float bgY;
	private float bgWidth;
	private float bgHeight;
	
	public OzFont(float x,float y,String text,int fontSize,Color color,Texture backGround) {
		this.text = text;
		setColor(color);
		this.font = newBitmapFont(text, fontSize,color);
		this.backGround = backGround;
		setX(x);
		setY(y);
	}
	public OzFont(float x,float y,String text,int fontSize,Texture backGround) {
		this(x,y,text,fontSize,Color.WHITE,backGround);
	}
	public OzFont(float x,float y,String text,int fontSize) {
		this(x,y,text,fontSize,Color.WHITE,null);
	}
	public OzFont(String text,int fontSize,Color color,Texture backGround) {
		this(0,0,text,fontSize,color,backGround);
	}
	public OzFont(String text,int fontSize,Texture backGround) {
		this(text,fontSize,Color.WHITE,backGround);
	}
	public OzFont(String text,int fontSize) {
		this(text,fontSize,Color.WHITE,null);
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(backGround!=null) batch.draw(backGround, bgX,bgY, bgWidth,bgHeight);
		if(extraText==null)  font.draw(batch, text,getX(),getY());
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
		return font.getBounds(text).width;
	}
	public float getFontHeight(){
		return font.getBounds(text).height;
	}
	/**这里输入的字符必须在text里有才能正常显示*/
	public void setExtraText(String extraText) {
		this.extraText = extraText;
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
		else if(text!=null){
			bgWidth = font.getBounds(text).width;
			bgHeight = font.getBounds(text).height;
			bgX = getX();
			bgY = getY()-font.getBounds(text).height;
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
	
	
	
	
	

}
