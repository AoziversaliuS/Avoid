package oz.game.avoid.font;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AndroidBitmapFontData extends BitmapFontData {

	static private final int LOG2_PAGE_SIZE = 9;
	static private final int PAGE_SIZE = 1 << LOG2_PAGE_SIZE;
	static private final int PAGES = 0x10000 / PAGE_SIZE;
	/**默认字体大小为30*/
	private static  final int DEFAULT_FONT_SIZE = 30;
	
	public static final char[] xChars = { 'x', 'e', 'a', 'o', 'n', 's', 'r',
			'c', 'u', 'm', 'v', 'w', 'z' };
	public static final char[] capChars = { 'M', 'N', 'B', 'D', 'C', 'E', 'F',
			'K', 'A', 'G', 'H', 'I', 'J', 'L', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z' };

	TextureRegion textRegion;
	/**默认字体大小为30*/
	public AndroidBitmapFontData(String strings){
		create(strings,DEFAULT_FONT_SIZE);
	}
	
	
	public AndroidBitmapFontData(String strings,int fontSize) {
		create(strings,fontSize);
	}



	public AndroidBitmapFontData(FileHandle fontFile, boolean flip) {
		super(fontFile, flip);
	}



	private void create(String strings,int fontSize) {
		strings = DeleteRepeateCharacter(strings);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(fontSize);
		paint.setTextAlign(Align.LEFT);
		paint.setColor(Color.WHITE);
		FontMetrics fm = paint.getFontMetrics();
		
		int w = (int) paint.measureText(strings)+1;
		int h = (int) (fm.descent - fm.ascent);
		
		int x = 0;
		for (int i = 0; i < strings.length(); i++) {
			Glyph glyph = new Glyph();
			int ch = strings.charAt(i);
			if (ch <= Character.MAX_VALUE)
				setGlyph(ch, glyph);
			else
				continue;
			glyph.srcX = x;
			glyph.srcY = 0;
			glyph.width = (int) paint.measureText(strings.substring(i, i+1));
			glyph.xadvance = glyph.width;
			glyph.height = h;
			glyph.xoffset = 0;
			x+=glyph.width;
		}
		
		Glyph spaceGlyph = getGlyph(' ');
		if (spaceGlyph == null) {
			spaceGlyph = new Glyph();
			Glyph xadvanceGlyph = getGlyph('l');
			if (xadvanceGlyph == null) xadvanceGlyph = getFirstGlyph();
			spaceGlyph.xadvance = xadvanceGlyph.xadvance;
			setGlyph(' ', spaceGlyph);
		}
		spaceWidth = spaceGlyph != null ? spaceGlyph.xadvance + spaceGlyph.width : 1;

		Glyph xGlyph = null;
		for (int i = 0; i < xChars.length; i++) {
			xGlyph = getGlyph(xChars[i]);
			if (xGlyph != null) break;
		}
		if (xGlyph == null) xGlyph = getFirstGlyph();
		xHeight = xGlyph.height;

		Glyph capGlyph = null;
		for (int i = 0; i < capChars.length; i++) {
			capGlyph = getGlyph(capChars[i]);
			if (capGlyph != null) break;
		}
		if (capGlyph == null) {
			for (Glyph[] page : this.glyphs) {
				if (page == null) continue;
				for (Glyph glyph : page) {
					if (glyph == null || glyph.height == 0 || glyph.width == 0) continue;
					capHeight = Math.max(capHeight, glyph.height);
				}
			}
		} else
			capHeight = capGlyph.height;

		ascent = 0 - capHeight;
		down = -lineHeight;
		boolean flip = false;
		if (flip) {
			ascent = -ascent;
			down = -down;
		}
		
		Bitmap strBitmap = Bitmap.createBitmap(w, (int) (h+1), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(strBitmap);
		
		c.drawText(strings, 0, -fm.ascent, paint);
		c.save();

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		strBitmap.compress(CompressFormat.PNG, 100, buffer);
		byte[] encodedData = buffer.toByteArray();
		Pixmap pixmap = new Pixmap(encodedData, 0, encodedData.length);
		
		textRegion = new TextureRegion(new Texture(pixmap));
	}
	
	public TextureRegion getTextureRegion(){
		return textRegion;
	}
	
	public static String DeleteRepeateCharacter(String...params){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			buffer.append(params[i]);
		}
		StringBuffer resultBuffer = new StringBuffer();
		for (int i = 0; i < buffer.length(); i++){
			String s = buffer.substring(i, i+1);
			if(resultBuffer.indexOf(s)==-1){
				resultBuffer.append(s);
			}
		}
		return resultBuffer.toString();
	}

}
