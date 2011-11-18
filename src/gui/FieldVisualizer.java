package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import core.Constants;

public class FieldVisualizer {
	private static void createFieldFrame() {
		final int size = Constants.FIELD_SIZE;
		JLabel[][] fieldData = new JLabel[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				fieldData[i][j] = new JLabel(new ImageIcon(
						Constants.FOOD_FILENAME));
			}
		}
		String[] fieldColumnNames = new String[size];
		for (int i = 0; i < size; ++i) {
			fieldColumnNames[i] = "";
		}
		TableModel model = new FieldModel(fieldData, fieldColumnNames);
		JTable field = new JTable(model);
		field.setDefaultRenderer(JLabel.class, new FieldRenderer());
		for (int i = 0; i < size; ++i) {
			field.getColumnModel().getColumn(i).setPreferredWidth(20);
		}
		field.setRowHeight(20);
		field.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		field.setEnabled(false);
		String[] columnNames = { "Состояние", "Действие", "Таблица переходов" };
		String[][] data = new String[Constants.STATES_NUMBER][columnNames.length];
		int[] width = { 100, 90, 160 };
		JTable auto = new JTable(data, columnNames);
		for (int i = 0; i < columnNames.length; ++i) {
			auto.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
		}
		auto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		auto.getTableHeader().setBorder(
				BorderFactory.createLineBorder(Color.BLACK));
		auto.getTableHeader().setResizingAllowed(false);
		auto.getTableHeader().setReorderingAllowed(false);
		auto.setEnabled(false);
		JPanel autoPanel = new JPanel();
		autoPanel.setLayout(new BoxLayout(autoPanel, BoxLayout.Y_AXIS));
		autoPanel.add(auto.getTableHeader());
		autoPanel.add(auto);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(10));
		panel.add(field);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(Box.createHorizontalGlue());
		panel.add(autoPanel);
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
