package org.processmining.plugins.xlogmodifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

@Plugin(name = MoveParameters.Name,
        returnLabels = { MoveParameters.Name },
        returnTypes = { MoveParameters.class },
        parameterLabels = { "XLog" },
        userAccessible = true
       )
public class MoveParameters {
	public static final String Name = MovePlugin.Name + " parameters";

	Set<String> Keys;

	public MoveParameters() {
		this(new HashSet<String>());
	}
	public MoveParameters(Set<String> keys) {
		this.Keys = keys;
	}

	@UITopiaVariant(affiliation = "Utrecht University", author = "B.N. Janssen", email = "b.n.janssen@students.uu.nl")
	@PluginVariant(
	    variantLabel = Name + ", using a dialog",
	    requiredParameterLabels = { 0 }
	)
	public static MoveParameters create(UIPluginContext context, XLog log) {
		return new MoveParametersDialog(log).show(context);
	}

	public static class MoveParametersDialog extends ModifierDialog<MoveParameters> {
		protected final HashMap<String, JCheckBox> Columns;
		protected final JComponent MainComponent;

		public MoveParametersDialog(XLog log) {
			super(new MoveParameters());

			this.Columns = new HashMap<String, JCheckBox>();

			ProMPropertiesPanel panel = newProMPropertiesPanel("Trace Attributes");

			for (String key : log.get(0).getAttributes().keySet()) {
				this.Columns.put(key, panel.addCheckBox(key, false));
			}

			this.MainComponent = panel;
		}

		public String getTitle() {
			return Name;
		}

		public void updateParameters() {
			MoveParameters params = this.getParameters();
			params.Keys.clear();
			for (Entry<String, JCheckBox> e : this.Columns.entrySet()) {
				if (e.getValue().isSelected())
					params.Keys.add(e.getKey());
			}
		}

		public JComponent visualize() {
			return this.MainComponent;
		}
	}
}
