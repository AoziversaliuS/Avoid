package oz.game.global;

import java.util.ArrayList;

import oz.game.base.OzUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ResManager {
	
	private static ArrayList<Texture> textureArray;
	/**初始化*/
	public static void init(){
		textureArray = new ArrayList<Texture>();
	}
	/**new出一个由ResManager管理的texture,长宽均为1*/
	public static Texture newTextureByManager(Color color){
		Texture texture = OzUtils.newTexture(1, 1, color);
		textureArray.add(texture);
		return texture;
	}
	/**new出一个由ResManager管理的texture*/
	public static Texture newTextureByManager(int width,int height,Color color){
		Texture texture = OzUtils.newTexture(width, height, color);
		textureArray.add(texture);
		return texture;
	}
	/**获取一个由ResManager管理的texture*/
	public static Texture newTextureByManager(String internalPath){
		Texture texture = new Texture(internalPath);
		textureArray.add(texture);
		return texture;
	}
	/**获取一个支持中文的BitmapFont
	 * @param text 之后需要显示的字符(可重复)
	 * @param fontSize 字体大小,(英文字体大小大于76(中文不能大于70)在Android端无法显示)
	 * @param color 字体颜色*/
	public static BitmapFont newBitmapFontByManager(String text, int fontSize, Color color){
		BitmapFont font = OzUtils.newBitmapFont(text, fontSize, color);
		textureArray.add(font.getRegion().getTexture());
		return font;
	}
	/**获取一个支持中文的BitmapFont,默认字体颜色为白色
	 * @param text 之后需要显示的字符(可重复)
	 * @param fontSize 字体大小,(英文字体大小大于76(中文不能大于70)在Android端无法显示)*/
	public static BitmapFont newBitmapFontByManager(String text, int fontSize){
		return newBitmapFontByManager(text, fontSize, Color.WHITE);
		
	}
	public static TextureRegionDrawable newTRDrawable(Texture texture){
		textureArray.add(texture);
		return OzUtils.newTRDrawable(texture);
	}
	public static TextureRegionDrawable newTRDrawable(String internalPath){
		TextureRegionDrawable drawable = OzUtils.newTRDrawable(internalPath);
		textureArray.add(drawable.getRegion().getTexture());
		return drawable;
	}
	/**dispose掉textureArray里所有的texture并清空textureArray*/
	public static void dispose(){
		for(Texture tex:textureArray){
			tex.dispose();
		}
		textureArray.clear();
	}

	

}
