package com.github.sondt87.utilities;

import android.graphics.Bitmap;
import android.graphics.Canvas;

class RobertsEdgeDectector {

	private Bitmap sourceImage;
    private Bitmap edgesImage;
	private int height;
	private int width;

	public void setSourceImage(Bitmap image) {
		sourceImage = image;
		width = sourceImage.getWidth();
		height = sourceImage.getHeight();
		edgesImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	}

	public void process() {
		// long startTime = System.currentTimeMillis();
		int[] ori = new int[width * height];
		sourceImage.getPixels(ori, 0, width, 0, 0, width, height);

		int temppixels[] = new int[height * width];
		for (int i = 0; i < width * height; i++) {
			int p = ori[i];
			// extracts the red, green, and blue bits from the pixel
			int r = 0xff & (p >> 16);
			int g = 0xff & (p >> 8);
			int b = 0xff & (p);
			// standard greyscale conversion
			int k = (int) (.56 * g + .33 * r + .11 * b);

			temppixels[i] = k;
		}
		int colormatrix[][] = new int[height][width];
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				colormatrix[r][c] = temppixels[r * width + c];
			}
		}

		int processedpixels[] = new int[width * height];
		for (int r = 1; r < height - 1; r++) {
			for (int c = 1; c < width - 1; c++) {
				int temp1 = Math.abs(colormatrix[r][c] - colormatrix[r + 1][c + 1]);
				int temp2 = Math.abs(colormatrix[r][c + 1] - colormatrix[r + 1][c]);
				colormatrix[r][c] = Math.max(temp1, temp2); // convert back to
															// array
				int k = colormatrix[r][c];
				int newrgb = (0xff000000 | (k << 16) | (k << 8) | k);
				processedpixels[r * width + c] = newrgb;
			}
		}

		edgesImage.setPixels(processedpixels, 0, width, 0, 0, width, height);

		// Log.i("Time login:", )
	}

	public Bitmap getEdgesImage() {
		return edgesImage;
	}

	public static Bitmap crop(Bitmap bitmap, float ratio, int maxSize) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int maxWidth = 0;
		int maxHeight = 0;
		if (width > height) {
			maxWidth = maxSize;
			maxHeight = (int) (maxWidth / ratio);
		} else {
			maxHeight = maxSize;
			maxWidth = (int) (maxHeight * ratio);
		}
		Bitmap bm = Bitmap.createBitmap(maxWidth, maxHeight, Bitmap.Config.ARGB_8888);
		Canvas cv = new Canvas(bm);
		int top = (height - maxHeight) / 2;
		int left = (width - maxWidth) / 2;
		cv.drawBitmap(bitmap, left, top, null);
		return bm;
	}

}