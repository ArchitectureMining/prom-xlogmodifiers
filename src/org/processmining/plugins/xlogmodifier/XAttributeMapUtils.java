package org.processmining.plugins.xlogmodifier;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeBoolean;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;
import org.deckfour.xes.model.impl.XAttributeContinuousImpl;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;

public class XAttributeMapUtils {
	public static XAttribute put(XAttributeMap map, XAttribute a) {
		return map.put(a.getKey(), a);
	}
	public static void putAll(XAttributeMap map, Collection<XAttribute> attrs) {
		for (XAttribute a : attrs) {
			put(map, a);
		}
	}

	public static boolean isLiteral(XAttributeMap map, String key) {
		return XAttributeUtils.isLiteral(map.get(key));
	}

	public static XAttribute putBoolean(XAttributeMap map, String key, boolean value) {
		return put(map, new XAttributeBooleanImpl(key, value, null));
	}
	public static XAttribute putBoolean(XAttributeMap map, String key, boolean value, XExtension extension) {
		return put(map, new XAttributeBooleanImpl(key, value, extension));
	}
	public static boolean getBoolean(XAttributeMap map, String key) {
		return XAttributeUtils.getBoolean(map.get(key));
	}
	public static XAttribute changeTypeBoolean(XAttributeMap map, String key) {
		return put(map, XAttributeUtils.changeTypeBoolean(map.get(key)));
	}

	public static XAttribute putContinuous(XAttributeMap map, String key, double value) {
		return put(map, new XAttributeContinuousImpl(key, value, null));
	}
	public static XAttribute putContinuous(XAttributeMap map, String key, double value, XExtension extension) {
		return put(map, new XAttributeContinuousImpl(key, value, extension));
	}
	public static double getContinuous(XAttributeMap map, String key) {
		return XAttributeUtils.getContinuous(map.get(key));
	}
	public static XAttribute changeTypeContinuous(XAttributeMap map, String key) {
		return put(map, XAttributeUtils.changeTypeContinuous(map.get(key)));
	}

	public static XAttribute putDiscrete(XAttributeMap map, String key, long value) {
		return put(map, new XAttributeDiscreteImpl(key, value, null));
	}
	public static XAttribute putDiscrete(XAttributeMap map, String key, long value, XExtension extension) {
		return put(map, new XAttributeDiscreteImpl(key, value, extension));
	}
	public static long getDiscrete(XAttributeMap map, String key) {
		return XAttributeUtils.getDiscrete(map.get(key));
	}
	public static XAttribute changeTypeDiscrete(XAttributeMap map, String key) {
		return put(map, XAttributeUtils.changeTypeDiscrete(map.get(key)));
	}

	public static XAttribute putLiteral(XAttributeMap map, String key, String value) {
		return put(map, new XAttributeLiteralImpl(key, value, null));
	}
	public static XAttribute putLiteral(XAttributeMap map, String key, String value, XExtension extension) {
		return put(map, new XAttributeLiteralImpl(key, value, extension));
	}
	public static String getLiteral(XAttributeMap map, String key) {
		return XAttributeUtils.getLiteral(map.get(key));
	}
	public static XAttribute changeTypeLiteral(XAttributeMap map, String key) {
		return put(map, XAttributeUtils.changeTypeLiteral(map.get(key)));
	}

	public static XAttribute putTimestamp(XAttributeMap map, String key, Date value) {
		return putTimestamp(map, key, value, null);
	}
	public static XAttribute putTimestamp(XAttributeMap map, String key, Date value, XExtension extension) {
		return put(map, new XAttributeTimestampImpl(key, value, extension));
	}
	public static Date getTimestamp(XAttributeMap map, String key) throws ParseException {
		return XAttributeUtils.getTimestamp(map.get(key));
	}
	public static Date getTimestamp(XAttributeMap map, String key, String[] parsePatterns) throws ParseException {
		return XAttributeUtils.getTimestamp(map.get(key), parsePatterns);
	}
	public static XAttribute changeTypeTimestamp(XAttributeMap map, String key) throws ParseException {
		return put(map, XAttributeUtils.changeTypeTimestamp(map.get(key)));
	}

	public static void changeKeys(XAttributeMap map, Map<String, String> newkeys) {
		for (Entry<String, String> k : newkeys.entrySet())
			changeKey(map, k.getKey(), k.getValue());
	}
	public static void changeKey(XAttributeMap map, String key, String newkey) {
		put(map, XAttributeUtils.changeKey(map.remove(key), newkey));
	}

	public static void changeTypes(XAttributeMap map, Map<String, Class<? extends XAttribute>> types) throws ParseException {
		for (Entry<String, Class<? extends XAttribute>> e : types.entrySet())
			changeType(map, e.getKey(), e.getValue());
	}
	public static XAttribute changeType(XAttributeMap map, String key, Class<? extends XAttribute> type) throws ParseException {
		if (type == null)
			return null;
		if (XAttributeUtils.isBoolean(type))
			return changeTypeBoolean(map, key);
		if (XAttributeUtils.isContinuous(type))
			return changeTypeContinuous(map, key);
		if (XAttributeUtils.isDiscrete(type))
			return changeTypeDiscrete(map, key);
		if (XAttributeUtils.isTimestamp(type))
			return changeTypeTimestamp(map, key);
		if (XAttributeUtils.isLiteral(type))
			return changeTypeLiteral(map, key);
		throw new ClassCastException("Could not cast XAttribute to any subclass");
	}

	public static void drain(XAttributeMap map, Collection<XAttribute> out) {
		out.addAll(map.values());
		map.clear();
	}
	public static boolean retainAll(XAttributeMap map, Collection<String> attrs) {
		return map.keySet().retainAll(attrs);
	}
	public static boolean containsAll(XAttributeMap map, Collection<String> attrs) {
		return map.keySet().containsAll(attrs);
	}
}
