package oz.game.avoid.font;

import oz.game.base.fontutils.ITextToImage;
import oz.game.base.fontutils.MyData;



public class DeskItextToImage implements ITextToImage {

	@Override
	public MyData GetBitmapFontData(String strings,int fontSize) {
		DeskBitmapFontData deskBitmapFontData = new DeskBitmapFontData(strings,fontSize);
//		androidBitmapFontData.create(strings);
		
		MyData data = new MyData();
		data.bitmapFontData = deskBitmapFontData;
		data.textureRegion = deskBitmapFontData.getTextureRegion();
		return data;
	}
	

}
