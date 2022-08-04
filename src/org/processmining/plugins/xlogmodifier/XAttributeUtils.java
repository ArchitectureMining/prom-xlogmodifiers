package org.processmining.plugins.xlogmodifier;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeBoolean;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;
import org.deckfour.xes.model.impl.XAttributeContinuousImpl;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;

public class XAttributeUtils {
	public static boolean isBoolean(XAttribute a) {
		return a instanceof XAttributeBoolean;
	}
	public static boolean getBoolean(XAttribute a) {
		if (isBoolean(a))
			return ((XAttributeBoolean)a).getValue();
		return parseBoolean(a);
	}
	public static boolean parseBoolean(XAttribute a) {
		return Boolean.parseBoolean(getLiteral(a));
	}
	public static XAttributeBoolean changeTypeBoolean(XAttribute a) {
		return new XAttributeBooleanImpl(a.getKey(), getBoolean(a), a.getExtension());
	}

	public static boolean isContinuous(XAttribute a) {
		return a instanceof XAttributeContinuous;
	}
	public static double getContinuous(XAttribute a) throws NumberFormatException {
		if (isContinuous(a))
			return ((XAttributeContinuous)a).getValue();
		return parseContinuous(a);
	}
	public static double parseContinuous(XAttribute a) throws NumberFormatException {
		return Double.parseDouble(getLiteral(a));
	}
	public static XAttributeContinuous changeTypeContinuous(XAttribute a) {
		return new XAttributeContinuousImpl(a.getKey(), getContinuous(a), a.getExtension());
	}

	public static boolean isDiscrete(XAttribute a) {
		return a instanceof XAttributeDiscrete;
	}
	public static long getDiscrete(XAttribute a) throws NumberFormatException {
		if (isDiscrete(a))
			return ((XAttributeDiscrete)a).getValue();
		return parseDiscrete(a);
	}
	public static long parseDiscrete(XAttribute a) throws NumberFormatException {
		return Long.parseLong(getLiteral(a));
	}
	public static XAttributeDiscrete changeTypeDiscrete(XAttribute a) {
		return new XAttributeDiscreteImpl(a.getKey(), getDiscrete(a), a.getExtension());
	}

	public static boolean isLiteral(XAttribute a) {
		return a instanceof XAttributeLiteral;
	}
	public static String getLiteral(XAttribute a) {
		if (isLiteral(a))
			return ((XAttributeLiteral)a).getValue();
		return a.toString();
	}
	public static XAttributeLiteral changeTypeLiteral(XAttribute a) {
		return new XAttributeLiteralImpl(a.getKey(), getLiteral(a), a.getExtension());
	}

	public static boolean isTimestamp(XAttribute a) {
		return a instanceof XAttributeTimestamp;
	}
	public static Date getTimestamp(XAttribute a) throws ParseException {
		return getTimestamp(a, DEFAULT_TIMESTAMP_FORMATS);
	}
	public static Date getTimestamp(XAttribute a, String[] parsePatterns) throws ParseException {
		if (isTimestamp(a))
			return ((XAttributeTimestamp)a).getValue();
		return parseTimestamp(a, parsePatterns);
	}
	public static Date parseTimestamp(XAttribute a) throws ParseException {
		return parseTimestamp(a, DEFAULT_TIMESTAMP_FORMATS);
	}
	public static Date parseTimestamp(XAttribute a, String[] parsePatterns) throws ParseException {
		return DateUtils.parseDate(getLiteral(a), parsePatterns);
	}
	public static final String[] DEFAULT_TIMESTAMP_FORMATS = new String[] {
	    DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(),
	    DateFormatUtils.ISO_DATETIME_FORMAT.getPattern(),
	    DateFormatUtils.SMTP_DATETIME_FORMAT.getPattern()
	};
	public static XAttributeTimestamp changeTypeTimestamp(XAttribute a) throws ParseException {
		return new XAttributeTimestampImpl(a.getKey(), getTimestamp(a), a.getExtension());
	}

	public static XAttribute changeKey(XAttribute attr, String key) {
		if (isBoolean(attr))
			return changeKey((XAttributeBoolean)attr, key);
		if (isContinuous(attr))
			return changeKey((XAttributeContinuous)attr, key);
		if (isDiscrete(attr))
			return changeKey((XAttributeDiscrete)attr, key);
		if (isTimestamp(attr))
			return changeKey((XAttributeTimestamp)attr, key);
		if (isLiteral(attr))
			return changeKey((XAttributeLiteral)attr, key);
		throw new ClassCastException("Could not cast XAttribute to any subclass");
	}
	public static XAttributeBoolean changeKey(XAttributeBoolean attr, String key) {
		return new XAttributeBooleanImpl(key, attr.getValue(), attr.getExtension());
	}
	public static XAttributeContinuous changeKey(XAttributeContinuous attr, String key) {
		return new XAttributeContinuousImpl(key, attr.getValue(), attr.getExtension());
	}
	public static XAttributeDiscrete changeKey(XAttributeDiscrete attr, String key) {
		return new XAttributeDiscreteImpl(key, attr.getValue(), attr.getExtension());
	}
	public static XAttributeTimestamp changeKey(XAttributeTimestamp attr, String key) {
		return new XAttributeTimestampImpl(key, attr.getValue(), attr.getExtension());
	}
	public static XAttributeLiteral changeKey(XAttributeLiteral attr, String key) {
		return new XAttributeLiteralImpl(key, attr.getValue(), attr.getExtension());
	}
}
