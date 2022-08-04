package org.processmining.plugins.xlogmodifier;

import java.util.ArrayList;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Create a transition log from a normal event log",
        returnLabels = { },
        returnTypes = { },
        parameterLabels = { "Log" },
        userAccessible = true,
        mostSignificantResult = -1
       )
public class TransitionPlugin {

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Create a transition log given the normal event log",
	    requiredParameterLabels = { 0 }
	)
	public static void transition(PluginContext context, XLog log) {
		context.getProgress().setValue(0);
		context.getProgress().setMaximum(log.size());
		context.getProgress().setCaption("Creating Transitions");

		ArrayList<XAttribute> attrs = new ArrayList<XAttribute>();
		for (XTrace trace : log) {
			if (trace.size() < 2)
				continue;

			XAttributeMap previous = trace.get(0).getAttributes();
			drain(attrs, previous);
			place(attrs, previous, "source:");

			for (int i = 1; i < trace.size(); i++) {
				XAttributeMap current = trace.get(i).getAttributes();
				drain(attrs, current);
				place(attrs, current, "source:");
				place(attrs, previous, "target:");
				previous = current;
			}
			// We did not add a target to the last event, and it cannot have one, hence we remove it.
			trace.remove(trace.size() - 1);
			context.getProgress().inc();
		}
	}

	private static void drain(ArrayList<XAttribute> data, XAttributeMap map) {
		data.clear();
		XAttributeMapUtils.drain(map, data);
	}
	private static void place(ArrayList<XAttribute> data, XAttributeMap map, String prefix) {
		for (XAttribute attr : data) {
			XAttributeMapUtils.put(map, prefix(attr, prefix));
		}
	}
	private static XAttribute prefix(XAttribute attr, String prefix) {
		return XAttributeUtils.changeKey(attr, prefix + attr.getKey());
	}
}
