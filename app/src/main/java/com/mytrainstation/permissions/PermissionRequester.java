package com.mytrainstation.permissions;

import android.app.Fragment;
import android.content.Context;

/**
 * All {@link Context} and {@link Fragment} classes that want to request
 * permissions have to implement this interface. It forces to implement
 * methods to perform code after the user permission decision, depending on
 * the decision (positive or negative).
 *
 * @author JSCHENK
 */
@SuppressWarnings("JavaDoc")
public interface PermissionRequester {

    /**
     * Method to handle the 'permission was/is granted' case after
     * requesting the permission. To check all possible request codes make
     * sure to call {@link #performPositiveUserDecision(int)} of your super
     * class. But be aware that you don't assign identical codes in super
     * and subclasses when calling the super method.
     *
     * @param requestCode
     * 		callback code to identify the initiator of the request
     */
    void performPositiveUserDecision(int requestCode);

    /**
     * Method to handle the 'permission was/is denied' case after requesting
     * the permission. To check all possible request codes make sure to call
     * {@link #performNegativeUserDecision(int)} of your super class. But be
     * aware that you don't assign identical codes in super and subclasses
     * when calling the super method.
     *
     * @param requestCode
     * 		callback code to identify the initiator of the request
     */
    void performNegativeUserDecision(int requestCode);
}
