package org.processmining.plugins.xlogmodifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

@Plugin(name = "Retain log attributes parameters",
        returnLabels = { "Retain Parameters"},
        returnTypes = { RetainParameters.class },
        parameterLabels = { "Log" },
        userAccessible = true
       )
public class RetainParameters {
	Set<String> LogKeys, TraceKeys, EventKeys;

	public RetainParameters() {
		this(new HashSet<String>(), new HashSet<String>(), new HashSet<String>());
	}
	public RetainParameters(Set<String> log, Set<String> trace, Set<String> event) {
		this.LogKeys = log;
		this.TraceKeys = trace;
		this.EventKeys = event;
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Retain log attributes parameters using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static RetainParameters create(UIPluginContext context, XLog log) {
		return new RetainParametersDialog(log).show(context);
	}

	public static class RetainParametersDialog extends ModifierDialog<RetainParameters> {
		protected final HashMap<String, JCheckBox> LogAttrs, TraceAttrs, EventAttrs;
		protected final JComponent MainComponent;

		public RetainParametersDialog(XLog log) {
			super(new RetainParameters());

			this.LogAttrs = new HashMap<String, JCheckBox>();
			this.TraceAttrs = new HashMap<String, JCheckBox>();
			this.EventAttrs = new HashMap<String, JCheckBox>();

			ProMPropertiesPanel logPanel, tracePanel, eventPanel;
			logPanel = newProMPropertiesPanel("Log Attributes");
			tracePanel = newProMPropertiesPanel("Trace Attributes");
			eventPanel = newProMPropertiesPanel("Event Attributes");

			for (String key : log.getAttributes().keySet()) {
				this.LogAttrs.put(key, logPanel.addCheckBox(key, true));
			}
			for (String key : log.get(0).getAttributes().keySet()) {
				this.TraceAttrs.put(key, tracePanel.addCheckBox(key, true));
			}
			for (String key : log.get(0).get(0).getAttributes().keySet()) {
				this.EventAttrs.put(key, eventPanel.addCheckBox(key, true));
			}

			this.MainComponent = Box.createVerticalBox();
			this.MainComponent.add(logPanel);
			this.MainComponent.add(Box.createVerticalStrut(15));
			this.MainComponent.add(tracePanel);
			this.MainComponent.add(Box.createVerticalStrut(15));
			this.MainComponent.add(eventPanel);
		}

		public String getTitle() {
			return "Retain log attributes parameters";
		}

		public void updateParameters() {
			RetainParameters params = this.getParameters();
			updateParameters(params.LogKeys, this.LogAttrs);
			updateParameters(params.TraceKeys, this.TraceAttrs);
			updateParameters(params.EventKeys, this.EventAttrs);
		}
		private static void updateParameters(Set<String> set, Map<String, JCheckBox> input) {
			set.clear();
			for (Entry<String, JCheckBox> e : input.entrySet()) {
				if (e.getValue().isSelected())
					set.add(e.getKey());
			}
		}

		public JComponent visualize() {
			return this.MainComponent;
		}
	}
}
