package com.mytrainstation.util;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Run tests against {@link BooleanUtils}.
 *
 * @author JSCHENK
 */
public class BooleanUtilsTest {

	/**
	 * Tests that {@link BooleanUtils#tryParseYesNoString(String)} will return {@code null} by
	 * passing several invalid parameters.
	 */
	@Test
	public void BooleanUtils_tryParseYesNoString_returnNullIfPassedStringsAreNotOfYesOrNo() {
		Assert.assertThat(BooleanUtils.tryParseYesNoString(null), is(nullValue()));
		Assert.assertThat(BooleanUtils.tryParseYesNoString(""), is(nullValue()));
		Assert.assertThat(BooleanUtils.tryParseYesNoString("NoBooleanValue"), is(nullValue()));
		Assert.assertThat(BooleanUtils.tryParseYesNoString("Not"), is(nullValue()));
	}

	/**
	 * Tests that {@link BooleanUtils#tryParseYesNoString(String)} will return {@code true} for
	 * passed string {@code yes} and {@code false} for passed string {@code no}.
	 */
	@Test
	public void BooleanUtils_tryParseYesNoString_returnBooleanValuesIfPassedStringsAreOfYesOrNo() {
		Assert.assertThat(BooleanUtils.tryParseYesNoString("yes"), is(true));
		Assert.assertThat(BooleanUtils.tryParseYesNoString("no"), is(false));
	}

}