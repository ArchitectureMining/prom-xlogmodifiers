package org.processmining.plugins.xlogmodifier;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Sort log events",
        returnLabels = { },
        returnTypes = { },
        parameterLabels = { "Log", "Parameters" },
        userAccessible = true,
        mostSignificantResult = -1
       )
public class SortPlugin {

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Retain log attributes using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public void sort(UIPluginContext context, XLog log) {
		SortParameters params = SortParameters.create(context, log);
		if (params == null)
			return;
		sort(context, log, params);
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Retain log attributes given selected parameters",
	    requiredParameterLabels = { 0, 1 }
	)
	public void sort(PluginContext context, XLog log, SortParameters params) {
		XLogUtils.sort(log, params.Keys, context.getProgress());
	}
}
