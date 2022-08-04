package org.processmining.plugins.xlogmodifier;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Rename log attributes",
        returnLabels = { },
        returnTypes = { },
        parameterLabels = { "Log", "Parameters" },
        userAccessible = true,
        mostSignificantResult = -1
       )
public class RenamePlugin {

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Rename log attributes using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static void rename(UIPluginContext context, XLog log) {
		RenameParameters params = RenameParameters.create(context, log);
		if (params == null)
			return;
		rename(context, log, params);
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Rename log attributes given selected parameters",
	    requiredParameterLabels = { 0, 1 }
	)
	public static void rename(PluginContext context, XLog log, RenameParameters params) {
		XLogUtils.changeKeys(log, params.LogKeys, params.TraceKeys, params.EventKeys, context.getProgress());
	}
}
