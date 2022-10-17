package org.processmining.plugins.xlogmodifier;

import javax.swing.JComponent;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

public abstract class ModifierDialog<P> {
	private final P Parameters;

	public ModifierDialog(P parameters) {
		this.Parameters = parameters;
	}

	public P show(UIPluginContext context) {
		InteractionResult result = context.showWizard(this.getTitle(), true, true, this.visualize());
		switch (result) {
		case CANCEL:
			return null;
		default :
			this.updateParameters();
			return this.getParameters();
		}
	}

	public P getParameters() {
		return this.Parameters;
	}

	public abstract String getTitle();
	public abstract void updateParameters();
	public abstract JComponent visualize();

	public static ProMPropertiesPanel newProMPropertiesPanel(String title) {
		final ProMPropertiesPanel props = new ProMPropertiesPanel(title);
		props.setPreferredSize(null);
		//props.setMinimumSize(new Dimension(MaxWizardWidth, props.getMaximumSize().height));
		return props;
	}
}
