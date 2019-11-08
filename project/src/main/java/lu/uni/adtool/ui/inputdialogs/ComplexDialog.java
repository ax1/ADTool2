package lu.uni.adtool.ui.inputdialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatter;

public class ComplexDialog extends InputDialog {

	public ComplexDialog(Frame frame, String title) {
		super(frame, title);
	}

	public ComplexDialog(Frame frame) {
		super(frame, "Complex");
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void createLayout(boolean showDefault) {
//		final NumberFormat f = NumberFormat.getInstance();
//		f.setParseIntegerOnly(true);
//		valueField = new JFormattedTextField(f);
		DefaultFormatter format = new DefaultFormatter();
		format.setOverwriteMode(false);

		valueField = new JFormattedTextField(format);
		valueField.addKeyListener(this);
		if (showDefault) {
			valueField.setValue(value);
		}
		valueField.setColumns(15);
		valueField.addPropertyChangeListener("value", this);
		final JPanel inputPane = new JPanel();
		inputPane.setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
//		c.insets = new Insets(2, 10, 0, 0);
//		c.gridy = 0;
//		c.gridx = 1;
//		c.gridwidth = 8;
		c.ipadx = 10;
		c.ipady = 10;
		inputPane.add(valueField, c);
		contentPane.add(inputPane, BorderLayout.CENTER);
		pack();
	}

	@Override
	protected boolean isValid(double d) {
		return true;
	}

	@Override
	protected void setValue(double d) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean sync() {
		value.updateFromString(valueField.getValue().toString());
		return true;
	}
}
