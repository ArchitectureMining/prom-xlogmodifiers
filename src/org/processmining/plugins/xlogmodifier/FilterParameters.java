package org.processmining.plugins.xlogmodifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.JComponent;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;

@Plugin(name = "Sort log events parameters",
        returnLabels = { "Sort Parameters"},
        returnTypes = { FilterParameters.class },
        parameterLabels = { "Log" },
        userAccessible = true
       )
public class FilterParameters {
	public Map<String, Pattern> Matches;

	public FilterParameters() {
		this(new HashMap<String, Pattern>());
	}
	public FilterParameters(HashMap<String,Pattern> matches) {
		this.Matches = matches;
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Filter log events parameters using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static FilterParameters create(UIPluginContext context, XLog log) {
		return new FilterParametersDialog(log).show(context);
	}

	public static class FilterParametersDialog extends ModifierDialog<FilterParameters> {
		protected final ProMPropertiesPanel Panel;
		protected final Map<String, ProMTextField> Selections;

		public FilterParametersDialog(XLog log) {
			super(new FilterParameters());
			this.Panel = newProMPropertiesPanel("Filtering Attribute Regexes");
			this.Selections = new HashMap<String, ProMTextField>();

			ArrayList<String> keys = new ArrayList<String>(log.get(0).get(0).getAttributes().keySet());
			keys.sort((o1, o2) -> o1.compareTo(o2));

			XAttributeMap attrs = log.get(0).get(0).getAttributes();
			for (String key : keys) {
				System.err.println(attrs.get(key).toString());
				this.Selections.put(key, this.Panel.addTextField(key));
			}
		}

		public String getTitle() {
			return "Filter log events parameters";
		}

		public void updateParameters() {
			FilterParameters params = this.getParameters();
			params.Matches.clear();
			for (Entry<String, ProMTextField> e : this.Selections.entrySet()) {
				String text = e.getValue().getText();
				if (text == null || text.isEmpty())
					continue;
				Pattern pattern = Pattern.compile(text);
				params.Matches.put(e.getKey(), pattern);
			}
		}

		public JComponent visualize() {
			return this.Panel;
		}
	}
}
