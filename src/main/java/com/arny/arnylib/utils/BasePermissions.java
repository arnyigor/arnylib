package com.arny.arnylib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
public class BasePermissions {
	private static final String PERMISSION_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
	private static final String PERMISSION_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
	private static final String PERMISSION_READ_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE;
	private static final String PERMISSION_WRITE_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
	private static final String PERMISSION_READ_PHONE_STATE = android.Manifest.permission.READ_PHONE_STATE;
	private static final String PERMISSION_CALL_PHONE = android.Manifest.permission.CALL_PHONE;
	private static final String PERMISSION_CALL_PRIVILEGED = android.Manifest.permission.CALL_PRIVILEGED;
	private static final String[] STORAGE_PERMS = {
			PERMISSION_READ_STORAGE,
			PERMISSION_WRITE_STORAGE
	};
	private static final String[] CALL_PERMS = {
			PERMISSION_CALL_PHONE,
			PERMISSION_CALL_PRIVILEGED
	};
	private static final String[] LOCATION_PERMS={
			PERMISSION_COARSE_LOCATION,
			PERMISSION_FINE_LOCATION
	};
	public static boolean isLocationPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isReadPhoneStatePermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isReceiveSMSPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
	}
//	public static boolean isBroadcastSMSPermissonGranted(Context context) {
//		return ActivityCompat.checkSelfPermission(context, Manifest.permission.BROADCAST_SMS) == PackageManager.PERMISSION_GRANTED;
//	}
	public static boolean isSendSMSPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isNotificationPolicyPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isReadSMSPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isReceiveBootCompletedPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isStoragePermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isPhoneCallPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isCameraPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isAudioPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean isContactsPermissonGranted(Context context) {
		return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED ;
	}
	public static boolean canAccessLocation(Activity activity,int requestCode) {
		return(hasPermission(activity, LOCATION_PERMS, PERMISSION_FINE_LOCATION,requestCode));
	}
	public static boolean canAccessStorage(Activity activity,int requestCode) {
		return(hasPermission(activity, STORAGE_PERMS, PERMISSION_READ_STORAGE,requestCode));
	}
	public static boolean canPhoneCall(Activity activity,int requestCode) {
		return(hasPermission(activity, CALL_PERMS, PERMISSION_CALL_PHONE,requestCode));
	}
	public static boolean canReadPhoneState(Activity activity,int requestCode) {
		return(hasPermission(activity, new String[]{PERMISSION_READ_PHONE_STATE}, PERMISSION_READ_PHONE_STATE,requestCode));
	}
	public static boolean permissionGranted(int[] grantResults){
		boolean granted = true;
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				granted = false;
				break;
			}
		}
		return granted;
	}
	private static boolean hasPermission(Activity activity, String[] permissions,String permision,int requestCode) {
		int permission = ActivityCompat.checkSelfPermission(activity, permision);
		if (permission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(
					activity,
					permissions,
					requestCode
			);
		}
		return permission == PackageManager.PERMISSION_GRANTED;
	}
}