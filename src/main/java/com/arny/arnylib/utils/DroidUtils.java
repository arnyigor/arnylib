package com.arny.arnylib.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.AnimRes;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import com.arny.arnylib.R;
import com.arny.arnylib.interfaces.AlertDialogListener;
import com.arny.arnylib.interfaces.ConfirmDialogListener;
import com.arny.arnylib.interfaces.InputDialogListener;
import com.arny.arnylib.interfaces.ListDialogListener;
import com.arny.arnylib.models.SMS;
import com.arny.arnylib.network.Connectivity;
import io.reactivex.Observable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
				hideProgress(pDialog);
				if (!pDialog.isShowing()) {
					pDialog.show();
				}
				pDialog.setMessage(notif);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static void simpleInputDialog(Context context, String title,String content, String btnOkText, String btnCancelText, int inputType, final InputDialogListener inputDialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)));
        builder.setTitle(title);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(context);
        tv.setText(content);
        tv.setLayoutParams(params);
        final EditText etResult = new EditText(context);
        etResult.setLayoutParams(params);
        layout.addView(tv);
        layout.addView(etResult);
        builder.setView(layout);
        etResult.findFocus();
        etResult.setInputType(inputType);
        builder.setPositiveButton(btnOkText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fullText = etResult.getText().toString().trim();
                inputDialogListener.onConfirm(fullText);
                if (fullText.length() > 0) {
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(btnCancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void simpleInputDialog(Context context, String title,String content,String preEdit, String btnOkText, String btnCancelText, int inputType, final InputDialogListener inputDialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)));
        builder.setTitle(title);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(context);
        tv.setText(content);
        tv.setPadding(20, 10, 20, 10); // in pixels (left, top, right, bottom)
        tv.setLayoutParams(params);
        final EditText etResult = new EditText(context);
        etResult.setText(preEdit);
        etResult.setPadding(20, 10, 20, 10); // in pixels (left, top, right, bottom)
        etResult.setLayoutParams(params);
        layout.addView(tv);
        layout.addView(etResult);
        builder.setView(layout);
        etResult.findFocus();
        etResult.setInputType(inputType);
        builder.setPositiveButton(btnOkText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fullText = etResult.getText().toString().trim();
                inputDialogListener.onConfirm(fullText);
                if (fullText.length() > 0) {
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(btnCancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    public static void simpleInputDialog(Context context, String title, String btnOkText, String btnCancelText, int inputType, final InputDialogListener inputDialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        final EditText etResult = new EditText(context);
        builder.setView(etResult);
        etResult.findFocus();
        etResult.setInputType(inputType);
        builder.setPositiveButton(btnOkText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String s = etResult.getText().toString();
                if (Utility.empty(s)) {
                    inputDialogListener.onError("empty");
                }else{
                    inputDialogListener.onConfirm(s.trim());
                }
            }
        });
        builder.setNegativeButton(btnCancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

	public static void confirmDialog(Context context, String title, String content, String btnOkText, String btnCancelText, final ConfirmDialogListener dialogListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    public static void alertDialog(Context context, String title, String btnOkText, final AlertDialogListener dialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)));
        builder.setTitle(title);
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


	public static void alertDialog(Context context, String title, String content, String btnOkText, final AlertDialogListener dialogListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)));
		builder.setTitle(title);
        if (content != null) {
            builder.setMessage(content);
        }
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
        listDialog(context, items, context.getResources().getString(R.string.list_dialog_title), listDialogListener);
    }

    public static void listDialog(Context context, String[] items, String title, final ListDialogListener listDialogListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)));
		builder.setTitle(title);
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

	public static Spanned fromHtml(String html){
		Spanned result;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
		} else {
			result = Html.fromHtml(html);
		}
		return result;
	}

	public static void addTextEllipseToEnd(final TextView tv, final String fileExtension) {
		if (tv.getTag() == null) {
			tv.setTag(tv.getText());
		}
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				try {
					ViewTreeObserver obs = tv.getViewTreeObserver();
					obs.removeGlobalOnLayoutListener(this);
					String text = tv.getLayout().getText().toString();
					if (text.endsWith("…")) {
						int endIndex = text.indexOf("…");
						text = text.substring(0, endIndex);
						text = text.substring(0, endIndex-fileExtension.length()-3)+"…" +fileExtension ;
						tv.setText(text);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

	}

	/**
	 * Конвертирование из dp в px
	 */
	public static float convertDPtoPX(int dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	}

	/**
	 * Конвертирование из px в dp
	 */
	public static float convertPXtoDP(int px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	}

	/**
	 * Воспроизвести звук
	 *
	 * @param resourceId ID звукового ресурса
	 * @param context    Контекст
	 */
	public static void playSound(int resourceId, Context context) {
	    try {
	        MediaPlayer mp = MediaPlayer.create(context, resourceId);
	        mp.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static boolean isConnected(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	    return !(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable());
	}

    /**
     * Converting objects to byte arrays
     */
    public static byte[] object2Bytes(Object o) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converting byte arrays to objects
     */
    public static Object bytes2Object(byte raw[]) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(raw);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void refreshActivity(Context context, Class<?> refreshedClass) {
        Intent intent = new Intent(context, refreshedClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    public static void runLayoutAnimation(final RecyclerView recyclerView, @AnimRes int anim) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, anim);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public static String getNetworkInfo(Context context) {
        boolean connected = Connectivity.isConnected(context);
        boolean connectedWifi = Connectivity.isConnectedWifi(context);
        boolean connectedFast = Connectivity.isConnectedFast(context);
        StringBuilder builder = new StringBuilder();
        builder.append(connected ? "Online" : "Offline");
        if (connected) {
            builder.append(connectedWifi ? ";WIFI" : ";Mobile");
            builder.append(connectedFast ? ";Fast" : ";Slow");
        }
        return builder.toString();
    }

    public static <T> Observable<T> makeObservable(T t){
        return Observable.create(e -> {
            e.onNext(t);
            e.onComplete();
        });
    }

    public static List<String> getAssetsFileLines(Context context, String fileName) {
        BufferedReader reader = null;
        List<String> list = new ArrayList<>();
        try {
            InputStream open = context.getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(open, "UTF-8"));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                list.add(mLine);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return list;
    }
}
