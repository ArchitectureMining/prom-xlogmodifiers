package org.processmining.plugins.xlogmodifier;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JComponent;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

@Plugin(name = RetypeParameters.Name,
        returnLabels = { RetypeParameters.Name },
        returnTypes = { RetypeParameters.class },
        parameterLabels = { "XLog" },
        userAccessible = true
       )
public class RetypeParameters {
	public static final String Name = RetypePlugin.Name + " parameters";

	Map<String, Class<? extends XAttribute>> TraceTypes, EventTypes;

	public RetypeParameters() {
		this(new HashMap<String, Class<? extends XAttribute>>(), new HashMap<String, Class<? extends XAttribute>>());
	}
	public RetypeParameters(Map<String, Class<? extends XAttribute>> trace, Map<String, Class<? extends XAttribute>> event) {
		this.TraceTypes = trace;
		this.EventTypes = event;
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = Name + ", using default options",
	    requiredParameterLabels = { 0 }
	)
	public static RetypeParameters create(PluginContext context, XLog log) {
		return new RetypeParameters(findType(log.get(0).getAttributes()), findType(log.get(0).get(0).getAttributes()));
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = Name + ", using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static RetypeParameters create(UIPluginContext context, XLog log) {
		return new RetypeParametersDialog(log).show(context);
	}

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
		for (Entry<String, XAttribute> e : map.entrySet()) {
			types.put(e.getKey(), findType(e.getValue()));
		}
		return types;
	}
	private static HashSet<String> toNames(HashSet<Class<? extends XAttribute>> attrs) {
		HashSet<String> names = new HashSet<String>();
		for (Class<? extends XAttribute> a : attrs) {
			names.add(XAttributeUtils.getName(a));
		}
		return names;
	}

	public static class RetypeParametersDialog extends ModifierDialog<RetypeParameters> {
		protected final HashMap<String, ProMComboBox<String>> TraceColumns, EventColumns;
		protected final JComponent MainComponent;

		public RetypeParametersDialog(XLog log) {
			super(new RetypeParameters());

			this.TraceColumns = new HashMap<String, ProMComboBox<String>>();
			this.EventColumns = new HashMap<String, ProMComboBox<String>>();

			ProMPropertiesPanel tracePanel, eventPanel;
			tracePanel = newProMPropertiesPanel("Trace Columns");
			eventPanel = newProMPropertiesPanel("Event Columns");

			for (Entry<String, XAttribute> e : log.get(0).getAttributes().entrySet()) {
				String desc = String.format("%s (%s)", e.getKey(), XAttributeUtils.getName(e.getValue()));
				HashSet<String> alts = toNames(XAttributeUtils.getParsableTypes(e.getValue()));
				ProMComboBox<String> select = tracePanel.addComboBox(desc, alts);
				select.setSelectedItem(XAttributeUtils.getName(e.getValue()));
				this.TraceColumns.put(e.getKey(), select);
			}
			for (Entry<String, XAttribute> e : log.get(0).get(0).getAttributes().entrySet()) {
				String desc = String.format("%s (%s)", e.getKey(), XAttributeUtils.getName(e.getValue()));
				HashSet<String> alts = toNames(XAttributeUtils.getParsableTypes(e.getValue()));
				ProMComboBox<String> select = eventPanel.addComboBox(desc, alts);
				select.setSelectedItem(XAttributeUtils.getName(e.getValue()));
				this.EventColumns.put(e.getKey(), select);
			}

			this.MainComponent = Box.createVerticalBox();
			this.MainComponent.add(tracePanel);
			this.MainComponent.add(Box.createVerticalStrut(15));
			this.MainComponent.add(eventPanel);
		}

		public String getTitle() {
			return Name;
		}

		public void updateParameters() {
			RetypeParameters params = this.getParameters();
			updateParameters(params.TraceTypes, this.TraceColumns);
			updateParameters(params.EventTypes, this.EventColumns);
		}
		private static void updateParameters(Map<String, Class<? extends XAttribute>> map, HashMap<String,ProMComboBox<String>> traceColumns) {
			for (Entry<String, ProMComboBox<String>> e : traceColumns.entrySet()) {
				map.put(e.getKey(), XAttributeUtils.fromName((String)e.getValue().getSelectedItem()));
			}
		}

		public JComponent visualize() {
			return this.MainComponent;
		}
	}
}
