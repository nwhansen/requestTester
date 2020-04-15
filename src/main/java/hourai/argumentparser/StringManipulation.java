/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides manipulations for strings in the context of a terminal
 */
public final class StringManipulation {

	/**
	 * Returns the length of a string in which null/empty strings return 0
	 *
	 * @param string The string to get the width of
	 * @return The length of the string
	 */
	public static int getLength(String string) {
		if (string == null || string.isEmpty()) {
			return 0;
		}
		return string.length();
	}

	/**
	 * Fixes the width of a string
	 *
	 * @param string The string to have the width fixed
	 * @param width The width to fix the string to
	 * @return The string of correct width
	 */
	public static String fixWidth(String string, int width) {
		int length = getLength(string);
		StringBuilder builder = new StringBuilder();
		if (length > width) {
			//Truncate string
			return string.substring(0, width);
		}
		if (length != 0) {
			builder.append(string);
		}
		for (int i = length; i < width; i++) {
			builder.append(" ");
		}
		return builder.toString();
	}

	/**
	 * Returns an array of strings where each array is width or less in length
	 *
	 * @param string The string to block
	 * @param width The width that each array must not exceed
	 * @return The Array of strings representing the blocked string
	 */
	public static String[] blockString(String string, int width) {
		int length = getLength(string);
		if (length == 0) {
			return new String[0];
		} else if (length < width) {
			//Freebie
			return new String[]{string};
		}
		List<String> parts = new ArrayList<String>();
		int subStart = 0, subEnd = width;
		do {
			//We want to include the last character
			if (' ' == string.charAt(subStart)) {
				subStart += 1;
				subEnd = Math.min(length, subEnd + 1);
			}
			String sub = string.substring(subStart, subEnd);
			//If we did not a perfect width
			if (!sub.endsWith(" ") && !isNextASpaceOrEOS(string, subEnd)) {
				int lastSpace = sub.lastIndexOf(' ');
				//Only adjust if there is even a space to adjust against
				if (lastSpace != -1) {
					sub = sub.substring(0, lastSpace + 1);
					subEnd = subStart + lastSpace + 1;
				}
			}
			subStart += sub.length();
			subEnd = Math.min(length, subEnd + width);
			parts.add(sub);
		} while (subStart < length);
		return parts.toArray(new String[0]);
	}

	private static boolean isNextASpaceOrEOS(String string, int currentIndex) {
		int lastIndex = getLength(string) - 1;
		if (currentIndex > lastIndex) {
			return true;
		}
		return string.charAt(currentIndex) == ' ';
	}
}
