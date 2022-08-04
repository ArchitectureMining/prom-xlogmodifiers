package org.processmining.plugins.xlogmodifier;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Retain log attributes",
        returnLabels = { },
        returnTypes = { },
        parameterLabels = { "Log", "Parameters" },
        userAccessible = true,
        mostSignificantResult = -1
       )
public class RetainPlugin {
	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Retain log attributes using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static void retain(UIPluginContext context, XLog log) {
		RetainParameters params = RetainParameters.create(context, log);
		if (params == null)
			return;
		retain(context, log, params);
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Retain log attributes given selected parameters",
	    requiredParameterLabels = { 0, 1 }
	)
	public static void retain(PluginContext context, XLog log, RetainParameters params) {
		XLogUtils.retainAll(log, params.LogKeys, params.TraceKeys, params.EventKeys, context.getProgress());
	}
}
