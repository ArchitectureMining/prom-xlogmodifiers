package org.processmining.plugins.xlogmodifier;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;

public class XAttributeMapUtils {
	public static XAttribute put(XAttributeMap map, XAttribute a) {
		return map.put(a.getKey(), a);
	}
	public static void putAll(XAttributeMap map, Collection<XAttribute> attrs) {
		for (XAttribute a : attrs) {
			put(map, a);
		}
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
		return put(map, XAttributeUtils.changeType(map.get(key), type));
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
