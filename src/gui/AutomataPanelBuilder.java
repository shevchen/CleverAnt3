package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import main.MooreMachineParser;
import core.Constants;
import core.Direction;
import core.MooreMachine;

public class AutomataPanelBuilder {
	private static JPanel getSignTextPanel(MooreMachine m) {
		String significant = "Номера видимых полей, образующих предикат:";
		boolean found = false;
		for (int i = 0; i < Constants.VISIBLE_CELLS; ++i) {
			if (((m.getSignificantMask() >> i) & 1) == 1) {
				if (found) {
					significant += ",";
				}
				found = true;
				significant += " " + i;
			}
		}
		significant += ".";
		JPanel panel = new JPanel();
		panel.add(new JLabel(significant));
		return panel;
	}

	private static JPanel getSignVisualPanel(MooreMachine m) {
		String[][] values = {
				{ "7", "5", Direction.DOWN.getVisualization(), "1", "0" },
				{ "", "6", "3", "2", "" }, { "", "", "4", "", "" } };
		final int rows = values.length;
		final int cols = values[0].length;
		String[] columnNames = new String[cols];
		for (int i = 0; i < cols; ++i) {
			columnNames[i] = "";
		}
		JLabel[][] data = new JLabel[rows][cols];
		final int signMask = m.getSignificantMask();
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				data[i][j] = new JLabel(values[i][j], SwingConstants.CENTER);
				data[i][j].setOpaque(true);
				if (values[i][j].length() == 0
						|| !Character.isDigit(values[i][j].charAt(0))) {
					continue;
				}
				int cell = Integer.parseInt(values[i][j]);
				if (((signMask >> cell) & 1) == 1) {
					data[i][j].setBackground(Constants.SIGNIFICANT_CELL_COLOR);
				} else {
					data[i][j]
							.setBackground(Constants.UNSIGNIFICANT_CELL_COLOR);
				}
			}
		}
		data[0][2].setBackground(Constants.ANT_POSITION_COLOR);
		JTable fieldPart = new JTable(new FieldModel(data, columnNames));
		fieldPart.setDefaultRenderer(JLabel.class, new JLabelRenderer());
		for (int i = 0; i < cols; ++i) {
			fieldPart.getColumnModel().getColumn(i).setPreferredWidth(
					2 * Constants.FIELD_CELL_SIZE);
		}
		fieldPart.setRowHeight(2 * Constants.FIELD_CELL_SIZE);
		fieldPart.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		fieldPart.setEnabled(false);
		JPanel panel = new JPanel();
		panel.add(fieldPart);
		return panel;
	}

	private static JTable getAutoTable(MooreMachine m) {
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
			final TransitionTableCreator ttc = new TransitionTableCreator(m, i);
			show.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Thread th = new Thread(ttc);
					th.start();
				}
			});
			panel.add(show, BorderLayout.CENTER);
			final int strut = 2;
			panel.add(Box.createVerticalStrut(strut), BorderLayout.NORTH);
			panel.add(Box.createVerticalStrut(strut), BorderLayout.SOUTH);
			panel.add(Box.createHorizontalStrut(strut), BorderLayout.EAST);
			panel.add(Box.createHorizontalStrut(strut), BorderLayout.WEST);
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
		return auto;
	}

	public static JPanel getAuto(MooreMachine m) {
		try {
			m = MooreMachineParser.parseBest();
		} catch (IOException e) {
			e.printStackTrace();
			return new JPanel();
		}
		JTable autoTable = getAutoTable(m);
		JPanel autoPanel = new JPanel();
		autoPanel.setLayout(new BoxLayout(autoPanel, BoxLayout.Y_AXIS));
		final int strut = 20;
		autoPanel.add(Box.createVerticalStrut(strut));
		autoPanel.add(getSignTextPanel(m));
		autoPanel.add(Box.createVerticalGlue());
		autoPanel.add(getSignVisualPanel(m));
		autoPanel.add(Box.createVerticalGlue());
		autoPanel.add(autoTable.getTableHeader());
		autoPanel.add(autoTable);
		return autoPanel;
	}
}
