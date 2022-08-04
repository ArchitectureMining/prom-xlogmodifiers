package org.processmining.plugins.xlogmodifier;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Retype log attributes",
        returnLabels = { },
        returnTypes = { },
        parameterLabels = { "Log" },
        userAccessible = true,
        mostSignificantResult = -1
       )
public class RetypePlugin {
	private static Class<? extends XAttribute> findType(XAttribute attr) {
		if (attr == null)
			return null;
		if (!XAttributeUtils.isLiteral(attr))
			return null;

		try {
			XAttributeUtils.parseContinuous(attr);
			return XAttributeContinuous.class;
		} catch (NumberFormatException e) { }

		try {
			XAttributeUtils.parseDiscrete(attr);
			return XAttributeDiscrete.class;
		} catch (NumberFormatException e) { }

		try {
			XAttributeUtils.parseTimestamp(attr);
			return XAttributeTimestamp.class;
		} catch (ParseException e) { }

		return null;
	}
	private static HashMap<String, Class<? extends XAttribute>> findType(XAttributeMap map) {
		HashMap<String, Class<? extends XAttribute>> types = new HashMap<String, Class<? extends XAttribute>>(map.size());
		for (String k : map.keySet()) {
			Class<? extends XAttribute> t = findType(map.get(k));
			if (t == null)
				continue;
			types.put(k, t);
		}
		return types;
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Retype log attributes",
	    requiredParameterLabels = { 0 }
	)
	public static void retype(PluginContext context, XLog log) throws ParseException {
		Map<String,Class<? extends XAttribute>> log_attrs = findType(log.getAttributes());
		Map<String,Class<? extends XAttribute>> trace_attrs = findType(log.get(0).getAttributes());
		Map<String,Class<? extends XAttribute>> event_attrs = findType(log.get(0).get(0).getAttributes());
		XLogUtils.changeTypes(log, log_attrs, trace_attrs, event_attrs, context.getProgress());
	}
}
