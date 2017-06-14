package com.arny.arnylib.files;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import com.arny.arnylib.utils.Logcat;
import com.arny.arnylib.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
public class BitmapUtils {
	public static Bitmap getBitmap(Uri mediaURI, Context context) throws IOException {
		return MediaStore.Images.Media.getBitmap(context.getContentResolver(), mediaURI);
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = null;
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}
		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap getBitmap(String path) {
		try {
			File image = new File(path);
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			return BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap getBitmap(Context context, int idRes) {
		return BitmapFactory.decodeResource(context.getResources(), idRes);
	}

	public static Bitmap getResizedBitmap(String path, int maxWith) throws IOException {
		Bitmap image = BitmapFactory.decodeFile(path);//loading the large bitmap is fine.
		int w = image.getWidth();//get width
		int h = image.getHeight();//get height
		Logcat.d(FileUtils.class.getSimpleName(), "getResizedBitmap:w = " + w + " h = " + h);
		double aspRat = (double) w / (double) h;//get aspect ratio
		Logcat.d(FileUtils.class.getSimpleName(), "getResizedBitmap: aspRat = " + aspRat);
		int H = (int) (w * aspRat);//set the height based on width and aspect ratio
		Logcat.d(FileUtils.class.getSimpleName(), "getResizedBitmap: H = " + H + " maxWith = " + maxWith);
		Bitmap result = Bitmap.createScaledBitmap(image, maxWith, H, false);//scale the bitmap
		image = null;
		return result;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Bitmap resizeBitmap(String path, int width, int height) throws IOException {
		byte[] imageBytes = FileUtils.readFile(new File(path));
		return resizeBitmap(imageBytes, width, height);
	}

	public static Bitmap resizeBitmap(String path, int required_size) throws IOException {
		byte[] imageBytes = FileUtils.readFile(new File(path));
		Bitmap bm; // prepare object to return
		// clear system and runtime of rubbish
		System.gc();
		Runtime.getRuntime().gc();
		//Decode image size only
		BitmapFactory.Options oo = new BitmapFactory.Options();
		// only decodes size, not the whole image
		// See Android documentation for more info.
		oo.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, oo);
		// Important function to resize proportionally.
		//Find the correct scale value. It should be the power of 2.
		int scale = 1;
		while (oo.outWidth / scale / 2 >= required_size
				&& oo.outHeight / scale / 2 >= required_size)
			scale *= 2; // Actual scaler
		//Decode Options: byte array image with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale; // set scaler
		o2.inPurgeable = true; // for effeciency
		o2.inInputShareable = true;
		// Do actual decoding, this takes up resources and could crash
		// your app if you do not do it properly
		bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, o2);
		// Just to be safe, clear system and runtime of rubbish again!
		System.gc();
		Runtime.getRuntime().gc();
		return bm; // return Bitmap to the method that called it
	}

	public static Bitmap stringToBitmap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, 0);
			return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	public static String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return Base64.encodeToString(baos.toByteArray(), 0);
	}

	public static byte[] bitmapToBytes(Bitmap photo) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 90, stream);
		return stream.toByteArray();
	}

	public static Bitmap bytesToBitmap(byte[] bytes) {
		try {
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getThumnailStringFromFile(String path) {
		Bitmap b = null;
		try {
			Logcat.d(FileUtils.class.getSimpleName(), "getThumbnailStringFromFile");
			b = ThumbnailUtils.createVideoThumbnail(path, 3);
		} catch (Exception e) {
			Logcat.d(FileUtils.class.getSimpleName(), "getThumbnailStringFromFile Exception: " + e.toString());
		}
		return bitmapToString(b);
	}

	public static Bitmap rotateBitmap(float angle, Bitmap arrowBitmap, Context context) {
		Bitmap canvasBitmap = arrowBitmap.copy(Bitmap.Config.ARGB_8888, true);
		canvasBitmap.eraseColor(0x00000000);
		Canvas canvas = new Canvas(canvasBitmap);
		Matrix rotateMatrix = new Matrix();
		rotateMatrix.setRotate(angle, canvas.getWidth() / 2, canvas.getHeight() / 2);
		canvas.drawBitmap(arrowBitmap, rotateMatrix, null);
		return new BitmapDrawable(context.getResources(), canvasBitmap).getBitmap();
	}

	public static Bitmap textOnBitmap(Bitmap bitmap, String text, int size) {
		try {
			Bitmap.Config bitmapConfig = bitmap.getConfig();
			// set default bitmap config if none
			if (bitmapConfig == null) {
				bitmapConfig = Bitmap.Config.ARGB_8888;
			}
			// resource bitmaps are imutable,
			// so we need to convert it to mutable one
			bitmap = bitmap.copy(bitmapConfig, true);
			Canvas canvas = new Canvas(bitmap);
			// new antialised Paint
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.rgb(0, 0, 0));
			// text size in pixels
			paint.setTextSize(size);
			// text shadow
			paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
			// draw text to the Canvas center
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			int x = (bitmap.getWidth() - bounds.width()) / 2;
			int y = (bitmap.getHeight() + bounds.height()) / 2;
			canvas.drawText(text, x, y, paint);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap getThumb(String finalFilePath, Context context, int widthDP, int heightDP) {
		return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(finalFilePath), (int) Utility.convertDPtoPX(widthDP, context), (int) Utility.convertDPtoPX(heightDP, context));
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight, Context context) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) (int) Utility.convertDPtoPX(newWidth, context)) / width;
		float scaleHeight = ((float) (int) Utility.convertDPtoPX(newHeight, context)) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);
		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		bm.recycle();
		return resizedBitmap;
	}

	public static Bitmap getThumb(Uri uri, Context context) {
		File image = new File(FileUtils.getUriFilePath(uri, context));
		BitmapFactory.Options bounds = new BitmapFactory.Options();
		bounds.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(image.getPath(), bounds);
		if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
			return null;
		int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight : bounds.outWidth;
		Logcat.d(FileUtils.class.getSimpleName(), "getPreview: originalSize = " + originalSize);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = originalSize / 2;
		return BitmapFactory.decodeFile(image.getPath(), opts);
	}

	public static Bitmap getVideoThumbnail(String filePath) {
		Bitmap thumb = null;
		Bitmap thumbCompressed = null;
		try {
			thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
			thumbCompressed = FileUtils.compressImage(thumb, 50);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thumbCompressed;
	}

	public static String getVideoThumbnail(Context context, int videoID) {
		try {
			String[] projection = {
					MediaStore.Video.Thumbnails.DATA,
			};
			ContentResolver cr = context.getContentResolver();
			Cursor cursor = cr.query(
					MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
					projection,
					MediaStore.Video.Thumbnails.VIDEO_ID + "=?",
					new String[] { String.valueOf(videoID) },
					null);
			if (cursor != null && cursor.moveToFirst()) {
				String path = cursor.getString(0);
				cursor.close();
				return path;
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getMediaThumb(String filePath, int width, int heigh) throws IOException {
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(filePath);
		byte[] rawArt;
		Bitmap thumb = null;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		rawArt = retriever.getEmbeddedPicture();
		if (rawArt != null) {
			thumb = resizeBitmap(rawArt, width, heigh);
		}
		return thumb;
	}

	public static void removeImageThumbnails(Context context, long photoId) {
		Cursor thumbnails = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Thumbnails.IMAGE_ID + "=?", new String[]{String.valueOf(photoId)}, null);
		for (thumbnails.moveToFirst(); !thumbnails.isAfterLast(); thumbnails.moveToNext()) {
			long thumbnailId = thumbnails.getLong(thumbnails.getColumnIndex(MediaStore.Images.Thumbnails._ID));
			String path = thumbnails.getString(thumbnails.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
			File file = new File(path);
			if (file.delete()) {
				context.getContentResolver().delete(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, MediaStore.Images.Thumbnails._ID + "=?", new String[]{String.valueOf(thumbnailId)});
			}
		}
	}

	public static void removeVideoThumbnails(Context context, long photoId) {
		Logcat.d(FileUtils.class.getSimpleName(), "removeVideoThumbnails: photoId = " + photoId);
		Cursor thumbnails = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, null, MediaStore.Video.Thumbnails.VIDEO_ID + "=?", new String[]{String.valueOf(photoId)}, null);
		Logcat.d(FileUtils.class.getSimpleName(), "removeVideoThumbnails: thumbnails = " + thumbnails);
		if (thumbnails != null) {
			for (thumbnails.moveToFirst(); !thumbnails.isAfterLast(); thumbnails.moveToNext()) {
				long thumbnailId = thumbnails.getLong(thumbnails.getColumnIndex(MediaStore.Video.Thumbnails._ID));
				Logcat.d(FileUtils.class.getSimpleName(), "removeVideoThumbnails: thumbnailId = " + thumbnailId);
				String path = thumbnails.getString(thumbnails.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
				File file = new File(path);
				if (file.delete()) {
					context.getContentResolver().delete(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, MediaStore.Video.Thumbnails._ID + "=?", new String[]{String.valueOf(thumbnailId)});
				}
			}
		}

	}

	public static Bitmap resizeBitmap(byte[] imageBytes, int width, int height) throws IOException {
		Bitmap bm; // prepare object to return
		// clear system and runtime of rubbish
		System.gc();
		Runtime.getRuntime().gc();
		//Decode image size only
		BitmapFactory.Options oo = new BitmapFactory.Options();
		// only decodes size, not the whole image
		// See Android documentation for more info.
		oo.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, oo);
		// Important function to resize proportionally.
		//Find the correct scale value. It should be the power of 2.
		//Decode Options: byte array image with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = calculateInSampleSize(o2, width, height);
		o2.inPurgeable = true; // for effeciency
		o2.inInputShareable = true;
		// Do actual decoding, this takes up resources and could crash
		// your app if you do not do it properly
		bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, o2);
		// Just to be safe, clear system and runtime of rubbish again!
		System.gc();
		Runtime.getRuntime().gc();
		return bm; // return Bitmap to the method that called it
	}

	public static Bitmap roundBitmap(Bitmap source) {
		try {
			long start = System.currentTimeMillis();
			Logcat.d(BitmapUtils.class.getSimpleName(), "roundBitmap: time = " + Utility.getTimeDiff(start));
			int size = Math.min(source.getWidth(), source.getHeight());
			int x = (source.getWidth() - size) / 2;
			int y = (source.getHeight() - size) / 2;
			Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
			if (squaredBitmap != source) {
				source.recycle();
			}
			Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
			paint.setShader(shader);
			paint.setAntiAlias(true);
			float r = size / 8f;
			canvas.drawRoundRect(new RectF(0, 0, source.getWidth(), source.getHeight()), r, r, paint);
			squaredBitmap.recycle();
			Logcat.d(BitmapUtils.class.getSimpleName(), "roundBitmap: time = " + Utility.getTimeDiff(start));
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String bitmapInfo(Bitmap bitmap){
		if (bitmap != null) {
			return "bitmap = " + FileUtils.formatFileSize(bitmap.getByteCount(),3)
					+ " w/h:" + bitmap.getWidth()  + "x" + bitmap.getHeight() +" size:"+ bitmap.getByteCount();
		}else{
			return null;
		}
	}

	public static Uri getImageContentUri(Context context, File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
			cursor.close();
			return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	public static Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Utility.getDateTime("yyyyMMdd_HHmmss"), null);
		return Uri.parse(path);
	}
}
