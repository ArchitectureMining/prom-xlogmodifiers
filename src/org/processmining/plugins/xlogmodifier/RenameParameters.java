package org.processmining.plugins.xlogmodifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;

@Plugin(name = "Rename log attributes parameters",
        returnLabels = { "Rename Parameters"},
        returnTypes = { RenameParameters.class },
        parameterLabels = { "Log" },
        userAccessible = true
       )
public class RenameParameters {
	Map<String, String> LogKeys, TraceKeys, EventKeys;

	public RenameParameters() {
		this(new HashMap<String, String>(), new HashMap<String, String>(), new HashMap<String, String>());
	}
	public RenameParameters(Map<String, String> log, Map<String, String> trace, Map<String, String> event) {
		this.LogKeys = log;
		this.TraceKeys = trace;
		this.EventKeys = event;
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Rename log attributes parameters using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static RenameParameters create(UIPluginContext context, XLog log) {
		return new RenameParametersDialog(log).show(context);
	}

	public static class RenameParametersDialog extends XLogModifierDialog<RenameParameters> {
		protected final Map<String, ProMTextField> LogInput, TraceInput, EventInput;
		protected final JComponent MainComponent;

		public RenameParametersDialog(XLog log) {
			super(new RenameParameters());

			this.LogInput = new HashMap<String, ProMTextField>();
			this.TraceInput = new HashMap<String, ProMTextField>();
			this.EventInput = new HashMap<String, ProMTextField>();

			ProMPropertiesPanel logPanel, tracePanel, eventPanel;
			logPanel = newProMPropertiesPanel("Log Attributes");
			tracePanel = newProMPropertiesPanel("Trace Attributes");
			eventPanel = newProMPropertiesPanel("Event Attributes");

			for (String key : log.getAttributes().keySet()) {
				this.LogInput.put(key, logPanel.addTextField(key));
			}
			for (String key : log.get(0).getAttributes().keySet()) {
				this.TraceInput.put(key, tracePanel.addTextField(key));
			}
			for (String key : log.get(0).get(0).getAttributes().keySet()) {
				this.EventInput.put(key, eventPanel.addTextField(key));
			}

			this.MainComponent = Box.createVerticalBox();
			this.MainComponent.add(logPanel);
			this.MainComponent.add(Box.createVerticalStrut(15));
			this.MainComponent.add(tracePanel);
			this.MainComponent.add(Box.createVerticalStrut(15));
			this.MainComponent.add(eventPanel);
		}

		public String getTitle() {
			return "Rename log attributes parameters";
		}

		public void updateParameters() {
			RenameParameters params = this.getParameters();
			updateParameters(params.LogKeys, this.LogInput);
			updateParameters(params.TraceKeys, this.TraceInput);
			updateParameters(params.EventKeys, this.EventInput);
		}
		private static void updateParameters(Map<String, String> map, Map<String, ProMTextField> input) {
			map.clear();
			for (Entry<String, ProMTextField> e : input.entrySet()) {
				String s = e.getValue().getText();
				if (s == null || s.isEmpty())
					continue;
				map.put(e.getKey(), s);
			}
		}

		public JComponent visualize() {
			return this.MainComponent;
		}
	}
}
