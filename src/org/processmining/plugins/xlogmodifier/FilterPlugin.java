package org.processmining.plugins.xlogmodifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Filter log events",
        returnLabels = { },
        returnTypes = { },
        parameterLabels = { "Log", "Parameters" },
        userAccessible = true,
        mostSignificantResult = -1
       )
public class FilterPlugin {

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Filter log event using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public void filter(UIPluginContext context, XLog log) {
		FilterParameters params = FilterParameters.create(context, log);
		if (params == null)
			return;
		filter(context, log, params);
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Filter log events given selected parameters",
	    requiredParameterLabels = { 0, 1 }
	)
	public void filter(PluginContext context, XLog log, FilterParameters params) {
		HashMap<String, Predicate<String>> pred = new HashMap<String, Predicate<String>>();
		for (Entry<String, Pattern> e : params.Matches.entrySet()) {
			pred.put(e.getKey(), e.getValue().asMatchPredicate());
		}
		filter(log, pred, context.getProgress());
	}

	private void filter(XLog log, Map<String, Predicate<String>> matches, Progress progress) {
		progress.setMinimum(0);
		progress.setMaximum(log.size());
		for (XTrace trace : log) {
			trace.removeIf(e -> {
				XAttributeMap attrs = e.getAttributes();
				for (Entry<String, Predicate<String>> s : matches.entrySet()) {
					String value = attrs.get(s.getKey()).toString();
					if (s.getValue().test(value))
						return true;
				}
				return false;
			});
			progress.inc();
		}
	}
}
