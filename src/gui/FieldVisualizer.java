package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import main.MooreMachineParser;
import core.Constants;
import core.Field;
import core.MooreMachine;

public class FieldVisualizer {
	private static Field f;
	private static MooreMachine m;

	private static JTable getRandomField() {
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
		return field;
	}

	private static JPanel getAuto() {
		try {
			m = MooreMachineParser.parseBest();
		} catch (IOException e) {
			e.printStackTrace();
			return new JPanel();
		}
		String[] columnNames = { "Состояние", "Действие", "Таблица переходов" };
		final int states = Constants.STATES_NUMBER;
		final int cols = columnNames.length;
		Object[][] data = new Object[states][cols];
		for (int i = 0; i < states; ++i) {
			data[i][0] = new JLabel(Integer.toString(i), SwingConstants.CENTER);
			data[i][1] = new JLabel(m.getMove(i).getRuType(),
					SwingConstants.CENTER);
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			JButton show = new JButton("Показать таблицу");
			panel.add(show, BorderLayout.CENTER);
			panel.add(Box.createVerticalStrut(2), BorderLayout.NORTH);
			panel.add(Box.createVerticalStrut(2), BorderLayout.SOUTH);
			panel.add(Box.createHorizontalStrut(2), BorderLayout.EAST);
			panel.add(Box.createHorizontalStrut(2), BorderLayout.WEST);
			data[i][2] = panel;
		}
		JTable auto = new JTable(new AutomataModel(data, columnNames));
		auto.setDefaultRenderer(JLabel.class, new JLabelRenderer());
		auto.setDefaultRenderer(JPanel.class, new JPanelRenderer());
		for (int i = 0; i < cols; ++i) {
			auto.getColumnModel().getColumn(i).setPreferredWidth(
					Constants.AUTOMATA_TABLE_WIDTH[i]);
		}
		auto.setRowHeight(Constants.AUTOMATA_TABLE_HEIGHT);
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
		return autoPanel;
	}

	private static void createFieldFrame() {
		JTable field = getRandomField();
		JPanel auto = getAuto();
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
