package org.processmining.plugins.xlogmodifier;

import java.text.ParseException;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = RetypePlugin.Name,
        returnLabels = { },
        returnTypes = { },
        parameterLabels = { "XLog", "Parameters" },
        userAccessible = true,
        mostSignificantResult = -1
       )
public class RetypePlugin {
	public static final String Name = "Retype XLog columns";

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = Name + ", inferring default paramters",
	    requiredParameterLabels = { 0 }
	)
	public static void retype(PluginContext context, XLog log) throws NumberFormatException, ParseException {
		retype(context, log, RetypeParameters.create(context, log));
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = Name + ", using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static void retype(UIPluginContext context, XLog log) throws NumberFormatException, ParseException {
		retype(context, log, RetypeParameters.create(context, log));
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = Name + ", using the given paramters",
	    requiredParameterLabels = { 0, 1 }
	)
	public static void retype(PluginContext context, XLog log, RetypeParameters params) throws NumberFormatException, ParseException {
		if (params == null)
			return;
		XLogUtils.changeTypes(log, null, params.TraceTypes, params.EventTypes);
	}
}
