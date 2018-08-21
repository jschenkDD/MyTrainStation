package com.mytrainstation.util;

import android.support.annotation.Nullable;

/**
 * Util class that provides functions to convert strings to boolean values and backwards.
 *
 * @author JSCHENK
 */
public class BooleanUtils {

	private static final String YES_STRING = "yes";
	private static final String NO_STRING = "no";

	/**
	 * @param value
	 * 		The string that should be parsed to {@code true}.
	 * @return The parsed {@code boolean} if passed string is of {@link #YES_STRING} or
	 * {@link #NO_STRING}, otherwise {@code null}.
	 */
	@Nullable
	public static Boolean tryParseYesNoString(@Nullable String value) {
		if (value == null || value.length() == 0) {
			return null;
		}
		boolean equalsTrue = YES_STRING.equalsIgnoreCase(value);
		boolean equalsNo = NO_STRING.equalsIgnoreCase(value);
		if (!equalsTrue && !equalsNo) {
			return null;
		}
		return equalsTrue;
	}

}