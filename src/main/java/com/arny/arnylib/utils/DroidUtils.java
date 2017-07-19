package com.arny.arnylib.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.view.View;
import com.arny.arnylib.R;
import com.arny.arnylib.interfaces.AlertDialogListener;
import com.arny.arnylib.interfaces.ConfirmDialogListener;
import com.arny.arnylib.interfaces.ListDialogListener;
import com.arny.arnylib.models.SMS;

import java.util.ArrayList;
public class DroidUtils {

	/**
	 * @param context
	 * @param phoneNumber String || null
	 * @return ArrayList<SMS>
	 */
	public static ArrayList<SMS> getSMSes(Context context,String phoneNumber){
		String where = null;
		if (phoneNumber != null) {
			where = "address='"+ phoneNumber + "'";
		}
		ArrayList<SMS> smsList = new ArrayList<>();
//		Uri uriSMSURI = Uri.parse("content://contacts/people");
		Uri uriSMSURI = Uri.parse("content://sms/inbox");
		Cursor cur = context.getContentResolver().query(uriSMSURI, null, where, null, null);
		if (cur != null) {
			while (cur.moveToNext()) {
				SMS sms = new SMS();
				sms.set_id(cur.getString(cur.getColumnIndex("_id")));
				sms.set_count(cur.getInt(cur.getColumnIndex("_count")));
				sms.setAddress(cur.getString(cur.getColumnIndex("address")));
				sms.setBody(cur.getString(cur.getColumnIndex("body")));
				sms.setCreator(cur.getString(cur.getColumnIndex("creator")));
				sms.setDate(cur.getLong(cur.getColumnIndex("date")));
				sms.setDate_sent(cur.getLong(cur.getColumnIndex("date_sent")));
				sms.setError_code(cur.getInt(cur.getColumnIndex("error_code")));
				sms.setPerson(cur.getInt(cur.getColumnIndex("person")));
				sms.setLocked(Boolean.parseBoolean(cur.getString(cur.getColumnIndex("locked"))));
				sms.setRead(Boolean.parseBoolean(cur.getString(cur.getColumnIndex("read"))));
				sms.setStatus(cur.getInt(cur.getColumnIndex("status")));
				sms.setService_center(cur.getString(cur.getColumnIndex("service_center")));
				sms.setSubject(cur.getString(cur.getColumnIndex("subject")));
				sms.setSeen(Boolean.parseBoolean(cur.getString(cur.getColumnIndex("seen"))));
				smsList.add(sms);
			}
			cur.close();
		}
		return smsList;
	}

	public static void hideProgress(ProgressDialog pDialog) {
		try {
			if (pDialog != null) {
				if (pDialog.isShowing()) {
					pDialog.dismiss();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showProgress(ProgressDialog pDialog, String notif) {
		try {
			if (pDialog != null) {
				pDialog.setMessage(notif);
				if (!pDialog.isShowing()) {
					pDialog.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void confirmDialog(Context context, String title, String content, String btnOkText, String btnCancelText, final ConfirmDialogListener dialogListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)));
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton(btnOkText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogListener.onConfirm();
			}
		});
		builder.setNegativeButton(btnCancelText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialogListener.onCancel();
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	public static void alertConfirmDialog(Context context, String title, final AlertDialogListener alertDialogListener) {
		new AlertDialog.Builder(context).setTitle(title + "?")
				.setNegativeButton(context.getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setPositiveButton(context.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				alertDialogListener.onConfirm();
			}
		}).show();
	}


	public static void alertDialog(Context context, String title, String content, String btnOkText, final AlertDialogListener dialogListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)));
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton(btnOkText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogListener.onConfirm();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	public static String dumpCursor(Cursor cursor){
		return DatabaseUtils.dumpCursorToString(cursor);
	}

	public static void listDialog(Context context, String[] items, final ListDialogListener listDialogListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)));
		builder.setTitle(context.getResources().getString(R.string.list_dialog_title));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				listDialogListener.onClick(item);
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void shareApp(Context context, String subject, String content, String chooseText){
		try {
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT, subject);
			i.putExtra(Intent.EXTRA_TEXT, content);
			context.startActivity(Intent.createChooser(i, chooseText));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set backgroundTint to {@link View} across all targeting platform level.
	 * @param view the {@link View} to tint.
	 * @param color color used to tint.
	 */
	@SuppressLint("RestrictedApi")
	public static void tintView(View view, int color) {
		final Drawable d = view.getBackground();
		final Drawable nd = d.getConstantState().newDrawable();
		nd.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(
				color, PorterDuff.Mode.SRC_IN));
		view.setBackground(nd);
	}

	/**
	 * Повибрировать :)
	 *
	 * @param duration Длительность в ms, например, 500 - полсекунды
	 * @param context
	 */
	public static void vibrate(int duration, Context context) {
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(duration);
	}

	public static String dumpIntent(Intent data){
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				StringBuilder stringBuilder = new StringBuilder();
				for (String key : bundle.keySet()) {
					Object value = bundle.get(key);
					if (value != null) {
						stringBuilder.append(String.format("key:%s  val:%s  classname:(%s)", key, value.toString(), value.getClass().getName()));
					}
				}
				return stringBuilder.toString();
			}
		}
		return null;
	}
}
