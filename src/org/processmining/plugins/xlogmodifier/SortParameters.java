package org.processmining.plugins.xlogmodifier;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

@Plugin(name = "Sort log events parameters",
        returnLabels = { "Sort Parameters"},
        returnTypes = { SortParameters.class },
        parameterLabels = { "Log" },
        userAccessible = true
       )
public class SortParameters {
	public List<String> Keys;

	public SortParameters() {
		this(new ArrayList<String>());
	}
	public SortParameters(List<String> keys) {
		this.Keys = keys;
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = "Sort log events parameters using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static SortParameters create(UIPluginContext context, XLog log) {
		return new SortParametersDialog(log).show(context);
	}

	public static class SortParametersDialog extends XLogModifierDialog<SortParameters> {
		protected final ProMPropertiesPanel Panel;
		protected final List<ProMComboBox<String>> Selections;

		public SortParametersDialog(XLog log) {
			super(new SortParameters());
			this.Panel = newProMPropertiesPanel("Sorting Attribute Order");
			this.Selections = new ArrayList<ProMComboBox<String>>();

			List<String> attrs = new ArrayList<String>();
			attrs.add("");
			for (String key : log.get(0).get(0).getAttributes().keySet()) {
				attrs.add(key);
			}
			for (int i = 0; i < attrs.size() - 1; i++) {
				this.Selections.add(this.Panel.addComboBox(String.format("Attribute %d", i + 1), attrs));
			}
		}

		public String getTitle() {
			return "Sort log events parameters";
		}

		public void updateParameters() {
			SortParameters params = this.getParameters();
			params.Keys.clear();
			for (ProMComboBox<String> select : this.Selections) {
				String item = (String)select.getSelectedItem();
				if (item == null || item.isEmpty())
					continue;
				params.Keys.add(item);
			}
		}

		public JComponent visualize() {
			return this.Panel;
		}
	}
}
