package com.mindtickle.qa.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;

public class JavaUtil {

	/**
	 * This function is used to replace values from the Map in the string.
	 * 
	 * @param replacePreRequisite
	 *            : Map who's values to be replaced in the string.
	 * @param queryParamJson
	 *            : Text in which the replacement needs to be done.
	 * @return
	 */
	public static String replacePreRequisite(Map<String, String> replacePreRequisite, String queryParamJson) {

		if (queryParamJson == null)
			return null;
		String patternString = "__(" + StringUtils.join(replacePreRequisite.keySet(), "|") + ")__";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(queryParamJson);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			// System.out.println("Value inside Matcher ::"
			// +replacePreRequisite.get(matcher.group(1)));
			matcher.appendReplacement(sb, (String) replacePreRequisite.get(matcher.group(1)));
		}
		matcher.appendTail(sb);

		return sb.toString();

	}

	public static String generateRandomString()
	{
		byte[] array = new byte[7];
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
		return generatedString;

	}
}
