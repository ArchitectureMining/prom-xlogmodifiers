package org.processmining.plugins.xlogmodifier;

import java.util.HashMap;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = MovePlugin.Name,
        returnLabels = { },
        returnTypes = { },
        parameterLabels = { "XLog", "Parameters" },
        userAccessible = true,
        mostSignificantResult = -1
       )
public class MovePlugin {
	public static final String Name = "Move XLog trace attributes to event attributes";

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = Name + ", using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static void move(UIPluginContext context, XLog log) {
		MoveParameters params = MoveParameters.create(context, log);
		if (params == null)
			return;
		move(context, log, params);
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = Name + ", using the given parameters",
	    requiredParameterLabels = { 0, 1 }
	)
	public static void move(PluginContext context, XLog log, MoveParameters params) {
		HashMap<String, XAttribute> moved = new HashMap<String, XAttribute>();
		for (XTrace trace : log) {
			XAttributeMap map = trace.getAttributes();
			moved.clear();

			for (String key : params.Keys) {
				moved.put(key, map.remove(key));
			}

			for (XEvent event : trace) {
				event.getAttributes().putAll(moved);
			}
		}
	}
}
