package gui;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import core.Constants;

public class CenteredButton extends JPanel {
	private JButton button;

	public CenteredButton(JButton button) {
		this.button = button;
		final int str = Constants.CENTERED_BUTTON_STRUT;
		setLayout(new BorderLayout());
		add(button, BorderLayout.CENTER);
		add(Box.createVerticalStrut(str), BorderLayout.NORTH);
		add(Box.createVerticalStrut(str), BorderLayout.SOUTH);
		add(Box.createHorizontalStrut(str), BorderLayout.EAST);
		add(Box.createHorizontalStrut(str), BorderLayout.WEST);
	}

	public JButton getButton() {
		return button;
	}
}
