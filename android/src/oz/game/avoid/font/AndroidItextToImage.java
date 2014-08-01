package oz.game.avoid.font;

import oz.game.base.fontutils.ITextToImage;
import oz.game.base.fontutils.MyData;



public class AndroidItextToImage implements ITextToImage {

	@Override
	public MyData GetBitmapFontData(String strings,int fontSize) {
		AndroidBitmapFontData androidBitmapFontData = new AndroidBitmapFontData(strings,fontSize);
//		androidBitmapFontData.create(strings);
		
		MyData data = new MyData();
		data.bitmapFontData = androidBitmapFontData;
		data.textureRegion = androidBitmapFontData.getTextureRegion();
		return data;
	}
	

}
