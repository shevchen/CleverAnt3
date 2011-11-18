package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import core.Constants;

public class FieldVisualizer {
	private static void createFieldFrame() {
		final int size = Constants.FIELD_SIZE;
		JTable field = new JTable(size, size);
		for (int i = 0; i < size; ++i) {
			field.getColumnModel().getColumn(i).setPreferredWidth(20);
		}
		field.setRowHeight(20);
		field.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		field.setEnabled(false);
		JTable auto = new JTable(10, 5);
		auto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		auto.setEnabled(false);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(10));
		panel.add(field);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(Box.createHorizontalGlue());
		panel.add(auto);
		panel.add(Box.createHorizontalStrut(10));
		JFrame frame = new JFrame();
		frame
				.setLayout(new BoxLayout(frame.getContentPane(),
						BoxLayout.Y_AXIS));
		frame.add(Box.createVerticalStrut(10));
		frame.add(panel);
		frame.add(Box.createVerticalStrut(10));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		createFieldFrame();
	}
}
