package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import core.Constants;
import core.Field;
import core.MooreMachine;

public class FieldVisualizer {
	private static Field f;
	private static MooreMachine m;

	private static JPanel getRandomField() {
		f = new Field();
		final int size = Constants.FIELD_SIZE;
		JLabel[][] fieldData = new JLabel[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				if (f.has(i, j)) {
					fieldData[i][j] = new JLabel(new ImageIcon(
							Constants.FOOD_FILENAME));
				} else {
					fieldData[i][j] = new JLabel();
				}
			}
		}
		String[] fieldColumnNames = new String[size];
		for (int i = 0; i < size; ++i) {
			fieldColumnNames[i] = "";
		}
		JTable field = new JTable(new FieldModel(fieldData, fieldColumnNames));
		field.setDefaultRenderer(JLabel.class, new JLabelRenderer());
		for (int i = 0; i < size; ++i) {
			field.getColumnModel().getColumn(i).setPreferredWidth(
					Constants.FIELD_CELL_SIZE);
		}
		field.setRowHeight(Constants.FIELD_CELL_SIZE);
		field.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		field.setEnabled(false);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(field, BorderLayout.SOUTH);
		return panel;
	}

	private static void createFieldFrame() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		final int strut = 10;
		panel.add(Box.createHorizontalStrut(strut));
		panel.add(getRandomField());
		panel.add(Box.createHorizontalStrut(strut));
		panel.add(Box.createHorizontalGlue());
		panel.add(AutomataPanelBuilder.getAuto(m));
		panel.add(Box.createHorizontalStrut(strut));
		JFrame frame = new JFrame("Визуализатор автомата");
		frame
				.setLayout(new BoxLayout(frame.getContentPane(),
						BoxLayout.Y_AXIS));
		frame.add(Box.createVerticalStrut(strut));
		frame.add(panel);
		frame.add(Box.createVerticalStrut(strut));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		createFieldFrame();
	}
}
