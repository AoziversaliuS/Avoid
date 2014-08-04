package oz.game.base;

import java.nio.ByteBuffer;






import oz.game.base.fontutils.MyData;
import oz.game.base.fontutils.TextControl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

public class OzUtils {
	/**英文大小写,数字和"~`!@#$%^&*():|{}<>?-="*/
	public static String DEFAULT_CHARS = "QWERTYUIOPASDFGHJKLZXCVBNM"
			                           + "qwertyuiopasdfghjklzxcvbnm"
			                           + "1234567890"
			                           +"~`!@#$%^&*():|{}<>?-=";
	
	public static Texture newTexture(int width,int height,Color color){
		Pixmap p = new Pixmap(width, height, Format.RGBA8888);
		p.setColor(color);
		p.fill();
		Texture t = new Texture(p);
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return t;
	}
	public static TextureRegionDrawable newTRDrawable(Texture texture){
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return new TextureRegionDrawable(new TextureRegion(texture));
	}
	public static TextureRegionDrawable newTRDrawable(String internalPath){
		Texture texture = new Texture(internalPath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return new TextureRegionDrawable(new TextureRegion(texture));
	}
	public static Color getRandomColor(){
		return new Color(MathUtils.random(0f, 1f),MathUtils.random(0f, 1f),MathUtils.random(0f, 1f), 1);
	}
	
	/**获取一个支持中文的BitmapFont,默认字体颜色为白色
	 * @param text 之后需要显示的字符(可重复)
	 * @param fontSize 字体大小,(英文字体大小大于76(中文不能大于70)在Android端无法显示)*/
	public static BitmapFont newBitmapFont(String text,int fontSize){
		MyData data = TextControl.GetITextToImage().GetBitmapFontData(text+DEFAULT_CHARS, fontSize);
//		data.textureRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return new BitmapFont(data.bitmapFontData,data.textureRegion,false);
	}
	/**获取一个支持中文的BitmapFont
	 * @param text 之后需要显示的字符(可重复)
	 * @param fontSize 字体大小,(英文字体大小大于76(中文不能大于70)在Android端无法显示)
	 * @param 字体颜色*/
	public static BitmapFont newBitmapFont(String text,int fontSize,Color color){
		MyData data = TextControl.GetITextToImage().GetBitmapFontData(text+DEFAULT_CHARS, fontSize);
		data.textureRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		BitmapFont font = new BitmapFont(data.bitmapFontData,data.textureRegion,false);
		font.setColor(color);
		return font;
	}
	
	
	public static Texture getScreenShot(){
		return getScreenShot(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	public static Texture getScreenShot(int width,int height){
		Pixmap pix = getScreenshot(0, 0,width,height,false);
		return new Texture(pix);
	}
	  public static void saveScreenshot(){
	        try{
	            FileHandle fh;
	            do{
	                fh = new FileHandle("screenshot.png");
	            }while (fh.exists());
	            Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(),
	            		Gdx.graphics.getHeight(), true);
	            PixmapIO.writePNG(fh, pixmap);
	            pixmap.dispose();
	        }catch (Exception e){           
	        }
	    }
	
    private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){

        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

        if (yDown) {
            // Flip the pixmap upside down
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
        }

        return pixmap;
    }
}
