package com.mytrainstation.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Util class that handles permission requests.
 *
 * @author JSCHENK
 */
public class PermissionUtils {

	/**
	 * Request code for permission requests to retrieve gps location.
	 **/
	public static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 1;

	/**
	 * Method to check, if a permission already was granted or not.
	 *
	 * @param context
	 * 		The current context where permission is needed.
	 * @param permission
	 * 		Permission to be checked (use Manifest.permission to find the
	 * 		correct string).
	 * @return true, if the context has the permission
	 */
	public static boolean isPermissionAlreadyGranted(Context context, String permission) {
		if (context == null || permission == null) {
			return false;
		}
		int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
		return permissionCheck == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * Use this method to request a certain permission from an {@link Activity}
	 * which implements {@link PermissionRequester}. If the permission is not
	 * yet granted, a permission request dialog will be shown to the user to
	 * make a decision. Your activity should implement
	 * {@link Activity#onRequestPermissionsResult(int, String[], int[])} and
	 * provide a callback code to handle the dialog callback. There you should
	 * just forward the callback to
	 * {@link #forwardingToPermissionRequesterAfterPermissionDialog(PermissionRequester, int, String[], int[])}
	 * .
	 *
	 * @param activityRequester
	 * 		{@link Activity} object the implements
	 * 		{@link PermissionRequester}
	 * @param permission
	 * 		Permission to be checked (use Manifest.permission to find the
	 * 		correct string).
	 * @param requestCode
	 * 		Use the request codes which are provided in {@link PermissionUtils}.
	 * 		They're needed to handle the callback in
	 * 		{@link Activity#onRequestPermissionsResult(int, String[], int[])}
	 * 		after the request dialog.
	 */
	public static <T extends Activity & PermissionRequester> void requestPermissionWithActivity(
			T activityRequester, String permission, int requestCode) {
		if (activityRequester == null || permission == null) {
			return;
		}

		int permissionCheck = ContextCompat.checkSelfPermission(activityRequester, permission);
		if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
			activityRequester.performPositiveUserDecision(requestCode);
		} else {
			ActivityCompat.requestPermissions(activityRequester, new String[]{permission},
					requestCode);
		}
	}

	/**
	 * Method that should be called in
	 * {@link Activity#onRequestPermissionsResult(int, String[], int[])} to
	 * handle the tasks after the user decision. Depending on the decision
	 * (positive or negative), the matching {@link PermissionRequester} methods
	 * will be called.
	 *
	 * @param requester
	 * 		Your class that implements the {@link PermissionRequester}
	 * 		interface.
	 * @param requestCode
	 * 		Request code of {@link Activity#onRequestPermissionsResult(int, String[], int[])}
	 * @param permissions
	 * 		Permissions of {@link Activity#onRequestPermissionsResult(int, String[], int[])}
	 * @param grantResults
	 * 		Grant results of {@link Activity#onRequestPermissionsResult(int, String[], int[])}
	 */
	public static void forwardingToPermissionRequesterAfterPermissionDialog(
			PermissionRequester requester, int requestCode, String permissions[],
			int[] grantResults) {
		if (requester == null || grantResults == null) {
			return;
		}
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			// permission granted
			requester.performPositiveUserDecision(requestCode);
		} else {
			// permission denied
			requester.performNegativeUserDecision(requestCode);
		}
	}
}
