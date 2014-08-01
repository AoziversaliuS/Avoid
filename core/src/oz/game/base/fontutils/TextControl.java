package oz.game.base.fontutils;

public class TextControl {
	private static ITextToImage mITextToImage;
	public static void setITextToImage(ITextToImage iTextToImage){
		mITextToImage = iTextToImage;
	}
	public static ITextToImage GetITextToImage(){
		return mITextToImage;
	}
}
