package oz.game.avoid.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DeskBitmapFontData extends BitmapFontData {

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
	
	public DeskBitmapFontData(String strings){
		create(strings,DEFAULT_FONT_SIZE);
	}
	
	
	public DeskBitmapFontData(String strings,int fontSize) {
		create(strings,fontSize);
	}


	private void create(String strings,int fontSize) {
		strings = DeleteRepeateCharacter(strings);
//		Font mFont = new Font("", Font.PLAIN, fontSize);
		Font mFont = new Font("", Font.BOLD, fontSize);
		BufferedImage bi = new BufferedImage(100, 100,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = bi.createGraphics();
		g.setFont(mFont);
		
		FontMetrics fm = g.getFontMetrics ();
		int strWidth = fm.stringWidth (strings);         
		int strHeight = fm.getAscent()+fm.getLeading();
		
		bi = new BufferedImage(strWidth, strHeight,
				BufferedImage.TYPE_4BYTE_ABGR);
		g = bi.createGraphics();
		g.setFont(mFont);
		g.setBackground(Color.BLUE);
		g.drawString(strings, 0, fm.getAscent());
		
		
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
			glyph.width = (int) fm.stringWidth(strings.substring(i, i+1));
			glyph.xadvance = glyph.width;
			glyph.height = strHeight;
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
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		try {
			ImageIO.write(bi,"png",buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] encodedData  = buffer.toByteArray();
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
