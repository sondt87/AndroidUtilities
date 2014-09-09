package com.github.sondt87.utilities;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;

public class BitmapUtil {
	public static class Coordinate {
		float x;
		float y;

		public Coordinate(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	public static byte[] getByteImageFromURL(String url) {
		Bitmap bmp = loadBitmap(url);
		if (bmp != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			bmp.recycle();
			return stream.toByteArray();
		}
		return null;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static byte[] convertBitmapToByte(Bitmap bmp) {
		if (bmp != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			return stream.toByteArray();
		}
		return null;
	}

	public static byte[] convertBitmapToByte(Bitmap bmp, CompressFormat format, int quality) {
		if (bmp != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(format, quality, stream);
			return stream.toByteArray();
		}
		return null;
	}

	public static byte[] convertBitmapToByte(Bitmap bmp, CompressFormat fomat, int quality, boolean recycle) {
		if (bmp != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(fomat, quality, stream);
			if (recycle)
				bmp.recycle();
			return stream.toByteArray();
		}
		return null;
	}

	public static BitmapDrawable scaleAndRotate(Bitmap bitmap, int newWidth, int newHeight, float degrees) {
		// load the origial BitMap (500 x 500 px)
		Bitmap bitmapOrg = bitmap;

		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// int newWidth = 200;
		// int newHeight = 200;

		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// rotate the Bitmap
		matrix.postRotate(degrees);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);

		// make a Drawable from Bitmap to allow to set the BitMap
		// to the ImageView, ImageButton or what ever
		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
		return bmd;
	}

	public static String convertBitmapToString(Bitmap bm) {
		byte[] bytes = convertBitmapToByte(bm);
		return Base64.encodeToString(bytes, Base64.DEFAULT);

	}

	public static Bitmap convertStringToBitmap(String base64) {
		byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
		Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return bm;
	}

	/**
	 * 
	 * @param b1
	 *            Bitmap 1
	 * @param b2
	 *            Bitmap 2
	 * @param c1
	 *            Toa do ve cua Bitmap 1
	 * @param c2
	 *            Toa do ve cua Bitmap 2
	 * @param width
	 *            width cua Merged bitmap
	 * @param height
	 *            height cua Merged bitmap
	 * @return merged bitmap
	 */
	public static Bitmap mergeBitmap(Bitmap b1, Bitmap b2, Coordinate c1, Coordinate c2, int width, int height) {
		Bitmap bm = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		Canvas cv = new Canvas(bm);
		cv.drawBitmap(b1, c1.x, c1.y, null);
		cv.drawBitmap(b2, c2.x, c2.y, null);
		return bm;
	}

	public static Bitmap getNewResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;

		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation

		Matrix matrix = new Matrix();

		// resize the bit map

		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		return resizedBitmap;

	}

	public static Bitmap myScale(Bitmap bitmap, float scale) {
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
	}

	public static Bitmap cropAndScaleImage(Bitmap bitmap, int width, int height) {
		int mWidth = bitmap.getWidth();
		int mHeight = bitmap.getHeight();
		Bitmap resized = null;
		if (mWidth < width) {
			float scale = width / mWidth;
			resized = myScale(bitmap, scale);
		} else {
			resized = bitmap;
		}
		if (mHeight < height) {
			float scale = height / mHeight;
			resized = myScale(resized, scale);
		} else {
			resized = bitmap;
		}
		int mX = Math.abs((mWidth - width) / 2);
		int mY = Math.abs(mHeight - mWidth / 2);

		return Bitmap.createBitmap(resized, mX, mY, mWidth - mX, mHeight - mY);
	}

	public static Bitmap cropToFrame(final Bitmap src, final int frameWidth, final int frameHeight) {
		final int imageWidth = src.getWidth();
		final int imageHeight = src.getHeight();
		/*
		 * Kiem tra voi gia thiet ca khung va anh co cung do rong se xay ra 2
		 * truong hop - Chieu cao cua khung lon hon chieu cao cua anh: TH1 -
		 * chieu cao cua khung nho hon chieu cao cua anh: TH2
		 */
		/*
		 * Cac buoc tinh toan: - scale anh ve dung chieu rong cua khung - Kiem
		 * tra xem no thuoc truong hop nao - TH1: + scale anh co chieu cao bang
		 * voi chieu cao cua khung + crop anh voi width, height cua khung - TH2:
		 * + crop anh voi width, height cua khung
		 */
		// chieu cao cua khung trong truong hop cung do rong
		float tempHeight = (float) imageWidth / frameWidth * frameHeight;
		Bitmap frameBitmap = null;
		// TH1
		if (tempHeight > imageHeight) {
			float scale = ((float) frameHeight) / imageHeight;
			Bitmap scaledBitmap = scaleAndRotate(src, scale, 0).getBitmap();
			frameBitmap = crop(scaledBitmap, frameWidth, frameHeight);
			scaledBitmap.recycle();
		} else if (tempHeight < imageHeight) {
			float scale = ((float) frameWidth) / imageWidth;
			Bitmap scaledBitmap = scaleAndRotate(src, scale, 0).getBitmap();
			frameBitmap = crop(scaledBitmap, frameWidth, frameHeight);
			scaledBitmap.recycle();
		} else {
			float scale = ((float) frameWidth) / imageWidth;
			frameBitmap = scaleAndRotate(src, scale, 0).getBitmap();
		}
		return frameBitmap;
	}

	public static BitmapDrawable scaleAndRotate(Bitmap bitmap, float scale, float degrees) {
		// load the origial BitMap (500 x 500 px)
		Bitmap bitmapOrg = bitmap;
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// int newWidth = 200;
		// int newHeight = 200;
		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) scale) * width;
		float scaleHeight = ((float) scale) * height;
		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scale, scale);
		// rotate the Bitmap
		matrix.postRotate(degrees);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
		// bitmapOrg.recycle();
		// make a Drawable from Bitmap to allow to set the BitMap
		// to the ImageView, ImageButton or what ever
		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
		return bmd;
	}

	public static Bitmap crop(Bitmap source, int left, int top, int width, int height) {
		Bitmap cropBitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		Canvas canvas = new Canvas(cropBitmap);
		canvas.drawBitmap(source, -left, -top, new Paint());
		return cropBitmap;
	}
	/**
	 * 
	 * @param src
	 * @param rate ty le width/height
	 * @return
	 */
	public static Bitmap cropCenter(Bitmap src, float rate){
		int width = src.getWidth();
		int height = src.getHeight();
		int newWidth = (int) (height*rate);
		if(newWidth > width){
			height = (int) (width/rate);
		}else if(newWidth < width){
			width= newWidth;
		}
		return crop(src, width, height);
				
		//truong hop width 
	}
	public static Bitmap crop(Bitmap src, int width, int height){
		int left = (int) ((src.getWidth() - width)/2f);
		int top = (int) ((src.getHeight() - height)/2f);
		return crop(src, left, top, width, height);
	}

	public static BitmapDrawable scale(Bitmap bitmap, int newWidth, int newHeight) {
		// load the origial BitMap (500 x 500 px)
		Bitmap bitmapOrg = bitmap;

		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// int newWidth = 200;
		// int newHeight = 200;

		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
		// bitmapOrg.recycle();
		// make a Drawable from Bitmap to allow to set the BitMap
		// to the ImageView, ImageButton or what ever
		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
		return bmd;
	}

	public static BitmapDrawable convertByteToDrawable(byte[] img) {
		if (img != null) {
			Bitmap temp = BitmapFactory.decodeByteArray(img, 0, img.length);
			BitmapDrawable drawable = new BitmapDrawable(temp);
			return drawable;
		}
		return null;
	}

	public static Bitmap blend(Bitmap src, Bitmap dst, int width, int height, Xfermode mode) {
		Log.i("BitmapUtil.blend()", "son.dt----> " + src + "/" + dst);
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
		bitmaps.add(src);
		bitmaps.add(dst);
		return blend(bitmaps, width, height, mode);
	}

	public static Bitmap blendMiddle(Bitmap src, Bitmap dst, int width, int height, Xfermode mode) {
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
		bitmaps.add(src);
		bitmaps.add(dst);
		return blendMiddle(bitmaps, width, height, mode);
	}

	public static Bitmap blend(ArrayList<Bitmap> bitmaps, int width, int height, Xfermode mode) {
		Bitmap blendedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas myCanvas = new Canvas(blendedBitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		// paint.setXfermode(mode);
		final int length = bitmaps.size();
		for (int i = 0; i < length; i++) {
			if (i > 0) {
				paint.setXfermode(mode);
			}
			myCanvas.drawBitmap(bitmaps.get(i), 0, 0, paint);
		}
		return blendedBitmap;
	}

	public static Bitmap blendWithEdgeImageEffect1(Bitmap src, Bitmap dst, int width, int height) {
		Log.i("TEST", "blend 1");
		Bitmap grayBitmap = toGrayscale(src);
		Bitmap gaussianBitmap = applyGaussianBlur(grayBitmap);

		Bitmap invertBitmap = doInvert(gaussianBitmap);
		gaussianBitmap.recycle();
		Bitmap resultBitmap = grayBitmap;

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				int R, G, B, C, BR, BG, BB, GRAY, IGRAY;
				C = src.getPixel(x, y);
				R = Color.red(C);
				G = Color.green(C);
				B = Color.blue(C);

				C = dst.getPixel(x, y);
				BR = Color.red(C);
				BG = Color.green(C);
				BB = Color.blue(C);

				GRAY = grayBitmap.getPixel(x, y);
				IGRAY = invertBitmap.getPixel(x, y);

				// make edge detection
				int alphablendPixel = blendChanelAlpha(GRAY, IGRAY, 0.5);
				int edgedPixel = blendChanelColorDodge(alphablendPixel, alphablendPixel);

				R = blendChanelScreen(R, BR);
				G = blendChanelScreen(G, BG);
				B = blendChanelScreen(B, BB);

				BR = blendChanelMultiply(edgedPixel, R);
				BG = blendChanelMultiply(edgedPixel, G);
				BB = blendChanelMultiply(edgedPixel, B);
				resultBitmap.setPixel(x, y, Color.rgb(BR, BG, BB));
			}
		}

		invertBitmap.recycle();

		return resultBitmap;
	}

	public static Bitmap blendWithEdgeImageEffect2(Bitmap src, Bitmap dst, int width, int height) {
		Log.i("TEST", "blend 1");
		Bitmap grayBitmap = toGrayscale(src);
		Bitmap gaussianBitmap = applyGaussianBlur(grayBitmap);

		Bitmap invertBitmap = doInvert(gaussianBitmap);
		gaussianBitmap.recycle();
		Bitmap resultBitmap = grayBitmap;

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				int A, R, G, B, C, BA, BR, BG, BB, GRAY, IGRAY;
				C = src.getPixel(x, y);
				R = Color.red(C);
				G = Color.green(C);
				B = Color.blue(C);
				A = Color.alpha(C);

				C = dst.getPixel(x, y);
				BR = Color.red(C);
				BG = Color.green(C);
				BB = Color.blue(C);
				BA = Color.alpha(C);

				GRAY = grayBitmap.getPixel(x, y);
				IGRAY = invertBitmap.getPixel(x, y);

				int AR = blendChanelAlpha(Color.red(GRAY), Color.red(IGRAY), 0.5);
				int AB = blendChanelAlpha(Color.blue(GRAY), Color.blue(IGRAY), 0.5);
				int AG = blendChanelAlpha(Color.green(GRAY), Color.green(IGRAY), 0.5);
				int AA = blendChanelAlpha(Color.alpha(GRAY), Color.alpha(IGRAY), 0.5);
				// ------------B
				// make edge detection
				AR = blendChanelColorDodge(AR, AR);
				AB = blendChanelColorDodge(AB, AB);
				AG = blendChanelColorDodge(AG, AG);
				AA = blendChanelColorDodge(AA, AA);

				// int edgedPixel = Color.rgb(AR, AG, AB);
				// int edgedPixel = Color.argb(AA,AR, AG, AB);
				// Log.i("BitmapUtil.blendWithEdgeImageEffect2()",
				// "----> Edged: " + edgedPixel);
				// --------------A
				R = blendChanelScreen(R, BR);
				G = blendChanelScreen(G, BG);
				B = blendChanelScreen(B, BB);
				A = blendChanelScreen(A, BA);
				// //---------------C
				BR = blendChanelOverlay(AR, R);
				BG = blendChanelOverlay(AG, G);
				BB = blendChanelOverlay(AB, B);
				BA = blendChanelOverlay(AA, A);
				// resultBitmap.setPixel(x, y, Color.rgb(BR, BG, BB));
				// //--------------D
				BR = blendChanelMultiply(AR, BR);
				BG = blendChanelMultiply(AG, BG);
				BB = blendChanelMultiply(AB, BB);

				// //--------------E
				BR = blendChanelMultiply(AR, BR);
				BG = blendChanelMultiply(AG, BG);
				BB = blendChanelMultiply(AB, BB);
				resultBitmap.setPixel(x, y, Color.rgb(BR, BG, BB));
				// resultBitmap.setPixel(x, y, edgedPixel);

			}
		}

		invertBitmap.recycle();

		return resultBitmap;
	}

	private static void toTransparent(Bitmap src) {
		// int width = src.getWidth();
		// int height = src.getHeight();
		// Bitmap bitmap = Bitmap.createBitmap((width, height,
		// Config.ARGB_8888);
		//
		// Bitmap transparent = blend(src, bitmap, width, height, (new
		// PorterDuffXfermode(Mode.MULTIPLY)));
		// src.recycle();
		// bitmap.recycle();
		// src = transparent;
		int[] pixels = new int[src.getHeight() * src.getWidth()];
		src.getPixels(pixels, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
		final int length = pixels.length;
		int transparent = Color.parseColor("#00000000");
		for (int i = 0; i < length; i++) {
			if (pixels[i] == Color.WHITE)
				pixels[i] = transparent;
		}
		src.setPixels(pixels, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
	}

	private static Bitmap overlay(Bitmap src, Bitmap dst) {
		final int width = src.getWidth();
		final int height = src.getHeight();
		final int size = width * height;

		int[] pixSrc = new int[size];
		src.getPixels(pixSrc, 0, width, 0, 0, width, height);

		int[] pixDst = new int[size];
		dst.getPixels(pixDst, 0, width, 0, 0, width, height);

		int[] pixOverlay = new int[size];

		for (int y = 0; y < height; y++) {
			// from left to right
			for (int x = 0; x < width; x++) {
				int index = y * width + x;
				// red
				int rSrc = (pixSrc[index] >> 16) & 0xff;
				int rDst = (pixDst[index] >> 16) & 0xff;

				int rBlended = blendChanelOverlay(rSrc, rDst);
				// green
				int gSrc = (pixSrc[index] >> 8) & 0xff;
				int gDst = (pixDst[index] >> 8) & 0xff;

				int gBlended = blendChanelOverlay(gSrc, gDst);
				// blue
				int bSrc = (pixSrc[index]) & 0xff;
				int bDst = (pixDst[index]) & 0xff;

				int bBlended = blendChanelOverlay(bSrc, bDst);

				pixOverlay[index] = Color.rgb(rBlended, gBlended, bBlended);

			}
		}
		Bitmap overlay = Bitmap.createBitmap(pixOverlay, width, height, Config.ARGB_4444);
		return overlay;
	}

	public static Bitmap createTransparentBitmapFromBitmap(Bitmap bitmap, int replaceThisColor) {
		if (bitmap != null) {
			int picw = bitmap.getWidth();
			int pich = bitmap.getHeight();
			int[] pix = new int[picw * pich];
			bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);
			final int gray = Color.GRAY;
			for (int y = 0; y < pich; y++) {
				// from left to right
				for (int x = 0; x < picw; x++) {
					int index = y * picw + x;
					int r = (pix[index] >> 16) & 0xff;
					int g = (pix[index] >> 8) & 0xff;
					int b = pix[index] & 0xff;

					if (pix[index] == replaceThisColor) {
						pix[index] = Color.TRANSPARENT;
					} else {
						pix[index] = gray;
					}
				}

				// from right to left
				/*
				 * for (int x = picw - 1; x >= 0; x--) { int index = y * picw +
				 * x; int r = (pix[index] >> 16) & 0xff; int g = (pix[index] >>
				 * 8) & 0xff; int b = pix[index] & 0xff;
				 * 
				 * if (pix[index] == replaceThisColor) { pix[index] =
				 * Color.TRANSPARENT; } else { // break; } }
				 */}

			Bitmap bm = Bitmap.createBitmap(pix, picw, pich, Bitmap.Config.ARGB_8888);

			return bm;
		}
		return null;
	}

	public static Bitmap createTransparentBitmapFromBitmap(Bitmap bitmap, int betweenColor1, int betweenColor2) {
		if (bitmap != null) {
			int picw = bitmap.getWidth();
			int pich = bitmap.getHeight();
			int[] pix = new int[picw * pich];
			final int gray = Color.GRAY;
			bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);

			Log.i("BitmapUtil.createTransparentBitmapFromBitmap()", "----> " + betweenColor1 + "," + betweenColor2 + ",");
			for (int y = 0; y < pich; y++) {
				// from left to right
				for (int x = 0; x < picw; x++) {
					int index = y * picw + x;
					int r = (pix[index] >> 16) & 0xff;
					int g = (pix[index] >> 8) & 0xff;
					int b = pix[index] & 0xff;
					Log.i("BitmapUtil.createTransparentBitmapFromBitmap()", "----> R:" + r + ", G:" + g + ",B:" + b + ", " + ((pix[index] >> 32) & 0xff));

					if (pix[index] >= betweenColor1 && pix[index] <= betweenColor2) {
						pix[index] = Color.TRANSPARENT;
					} else {
						pix[index] = gray + pix[index];
					}
				}

				// from right to left
				// for (int x = picw - 1; x >= 0; x--) {
				// int index = y * picw + x;
				// int r = (pix[index] >> 16) & 0xff;
				// int g = (pix[index] >> 8) & 0xff;
				// int b = pix[index] & 0xff;
				//
				// if (pix[index] >= betweenColor1 && pix[index] <=
				// betweenColor2) {
				// pix[index] = Color.TRANSPARENT;
				// }else{
				// pix[index] = gray;
				// }
				// }
			}

			Bitmap bm = Bitmap.createBitmap(pix, picw, pich, Bitmap.Config.ARGB_8888);

			return bm;
		}
		return null;
	}

	public static Bitmap blendWithEdgeImageEffect(Bitmap src, Bitmap dst, int width, int height) {
		Log.i("TEST", "blend 2");
		// Step 1: blend as A
		Bitmap blendedBitmap = blend(src, dst, width, height, new PorterDuffXfermode(PorterDuff.Mode.SCREEN));

		// Step 2: get edge as B
		RobertsEdgeDectector edgeDectector = new RobertsEdgeDectector();
		edgeDectector.setSourceImage(src);
		edgeDectector.process();
		Bitmap edgeBitmap = edgeDectector.getEdgesImage();
		Bitmap recycleBitmap = edgeBitmap;
		edgeBitmap = doInvert(edgeBitmap);
		recycleBitmap.recycle();
		// create transparent
		// toTransparent(edgeBitmap);
		// recycleBitmap = edgeBitmap;
		// edgeBitmap =
		// createTransparentBitmapFromBitmap(edgeBitmap,Color.parseColor("#eeeeee")/3*2,
		// Color.WHITE);
		// // edgeBitmap = createTransparentBitmapFromBitmap(edgeBitmap,
		// Color.WHITE);
		// recycleBitmap.recycle();

		// edge overlay blend to blendEdge (B overlay A as C )
		recycleBitmap = blendedBitmap;

		blendedBitmap = overlay(edgeBitmap, blendedBitmap);
		// blendedBitmap = blend(edgeBitmap, blendedBitmap, width, height,
		// new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
		recycleBitmap.recycle();
		// edge multiple blendEdge (b multiple c)
		recycleBitmap = blendedBitmap;
		blendedBitmap = blend(blendedBitmap, edgeBitmap, width, height, new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

		// ---dont use
		// blendedBitmap = blend(edgeBitmap, dst, width, height,
		// new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
		// ---
		recycleBitmap.recycle();
		edgeBitmap.recycle();
		return blendedBitmap;
	}

	static int blendChanelColorDodge(int A, int B) {
		return ((B == 255) ? B : Math.min(255, ((A << 8) / (255 - B))));
	}

	static int blendChanelAlpha(int A, int B, int O) {
		return (int) ((O * A) + (1 - O) * B);
		// if(O <=255 && O>=0){
		// }else{
		// return A;
		// }
	}

	static int blendChanelAlpha(int A, int B, double O) {
		return (int) ((O * A) + (1 - O) * B);
	}

	static int blendChanelScreen(int A, int B) {
		return (255 - (((255 - A) * (255 - B)) >> 8));
	}

	static int blendChanelMultiply(int A, int B) {
		return (A * B) / 255;
	}

	static int blendChanelInvert(int A) {
		return (255 - A);
	}

	static int blendChanelOverlay(int A, int B) {
		return (((B < 128) ? (2 * A * B / 255) : (255 - 2 * (255 - A) * (255 - B) / 255)));
	}

	public static Bitmap blendMiddle(ArrayList<Bitmap> bitmaps, int width, int height, Xfermode mode) {
		Bitmap blendedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas myCanvas = new Canvas(blendedBitmap);
		Paint paint = new Paint();
		paint.setXfermode(mode);
		final int length = bitmaps.size();
		for (int i = 0; i < length; i++) {
			Bitmap bm = bitmaps.get(i);
			float left = ((float) (width - bm.getWidth())) / 2;
			float top = ((float) (height - bm.getHeight())) / 2;
			myCanvas.drawBitmap(bm, left, top, paint);
		}
		return blendedBitmap;
	}

	public static Bitmap drawAlteredImage(Bitmap bmp, float contr, float bright, float satur) {
		Bitmap alteredBitmap = drawAlteredImage(bmp, changeColorFilter(contr, bright, satur));
		return alteredBitmap;
	}

	public static Bitmap drawAlteredImage(Bitmap bmp, float contr, float bright, float satur, Config config) {
		Bitmap alteredBitmap = drawAlteredImage(bmp, changeColorFilter(contr, bright, satur), config);
		return alteredBitmap;
	}

	public static Bitmap drawAlteredImage(Bitmap bmp, ColorFilter filter) {
		// Bitmap alteredBitmap = Bitmap.createBitmap(bmp.getWidth(),
		// bmp.getHeight(), bmp.getConfig());
		// Canvas canvas = new Canvas(alteredBitmap);
		// Paint paint = new Paint();
		// paint.setColorFilter(filter);
		// Matrix matrix = new Matrix();
		// canvas.drawBitmap(bmp, matrix, paint);
		return drawAlteredImage(bmp, filter, bmp.getConfig());
	}

	public static Bitmap drawAlteredImage(Bitmap bmp, ColorFilter filter, Config config) {
		Bitmap alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), config);
		Canvas canvas = new Canvas(alteredBitmap);
		Paint paint = new Paint();
		paint.setColorFilter(filter);
		Matrix matrix = new Matrix();
		canvas.drawBitmap(bmp, matrix, paint);
		return alteredBitmap;
	}

	public static Bitmap decodeImageFile(String filePath, int requireSize) throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		Bitmap temp = BitmapFactory.decodeStream(new FileInputStream(filePath), null, o);
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < requireSize || height_tmp / 2 < requireSize)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(new FileInputStream(filePath), null, o2);
	}

	public static ColorMatrixColorFilter changeColorFilter(float contr, float bright, float satur) {
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(satur);
		final float m[] = cm.getArray();
		final float c = contr;
		cm.set(new float[] { m[0] * c, m[1] * c, m[2] * c, m[3] * c, m[4] * c + bright, m[5] * c, m[6] * c, m[7] * c, m[8] * c, m[9] * c + bright, m[10] * c, m[11] * c, m[12] * c, m[13] * c, m[14] * c + bright, m[15], m[16], m[17], m[18], m[19] });
		// cm.set(new float[] { contr, 0, 0, 0, bright, 0,
		// contr, 0, 0, bright, 0, 0, 1, 0,
		// bright, 0, 0, 0, 1, 0 });
		return new ColorMatrixColorFilter(cm);
	}

	public static Bitmap makeTransparent(Bitmap alphaGray, Bitmap src) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] alphaPix = new int[width * height];
		alphaGray.getPixels(alphaPix, 0, width, 0, 0, width, height);
		int count = width * height;
		for (int i = 0; i < count; ++i) {
			alphaPix[i] = alphaPix[i] << 8;
		}
		src.setPixels(alphaPix, 0, width, 0, 0, width, height);

		Paint alphaP = new Paint();
		alphaP.setAntiAlias(true);
		alphaP.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		result.eraseColor(Color.BLACK);
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(src, 0, 0, alphaP);
		src.recycle();
		return result;
	}

	public static Bitmap loadBitmap(String url) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is, 8192);
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}

	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	public static Bitmap applyGaussianBlur(Bitmap src) {
		double[][] GaussianBlurConfig = new double[][] { { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } };
		ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
		convMatrix.applyConfig(GaussianBlurConfig);
		convMatrix.Factor = 16;
		convMatrix.Offset = 0;
		return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}

	public static Bitmap doInvert(Bitmap src) {
		// create new bitmap with the same settings as source bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		// color info
		int A, R, G, B;
		int pixelColor;
		// image size
		int height = src.getHeight();
		int width = src.getWidth();

		// scan through every pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// get one pixel
				pixelColor = src.getPixel(x, y);
				// saving alpha channel
				A = Color.alpha(pixelColor);
				// inverting byte for each R/G/B channel
				R = 255 - Color.red(pixelColor);
				G = 255 - Color.green(pixelColor);
				B = 255 - Color.blue(pixelColor);
				// set newly-inverted pixel to output image
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final bitmap
		return bmOut;
	}
}

