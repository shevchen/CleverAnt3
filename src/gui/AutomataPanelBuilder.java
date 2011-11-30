package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import core.Constants;
import core.Direction;
import core.MooreMachine;

public class AutomataPanelBuilder {
	private JPanel[][] data;

	private JPanel getSignTextPanel(MooreMachine m) {
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
		JLabel label = new JLabel(significant);
		JPanel panel = new JPanel();
		panel.add(label);
		panel.setMaximumSize(new Dimension(label.getPreferredSize().width,
				label.getPreferredSize().height));
		return panel;
	}

	private JPanel getSignVisualPanel(MooreMachine m) {
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
		panel.setMaximumSize(new Dimension(12 * Constants.FIELD_CELL_SIZE,
				7 * Constants.FIELD_CELL_SIZE));
		return panel;
	}

	private JTable getAutoTable(MooreMachine m) {
		String[] columnNames = { "Состояние", "Действие", "Таблица переходов" };
		final int states = Constants.STATES_NUMBER;
		final int cols = columnNames.length;
		data = new JPanel[states][cols];
		for (int i = 0; i < states; ++i) {
			data[i][0] = new JPanel();
			data[i][0].add(new JLabel(Integer.toString(i),
					SwingConstants.CENTER));
			data[i][1] = new JPanel();
			data[i][1].add(new JLabel(m.getMove(i).getRuType(),
					SwingConstants.CENTER));
			JButton button = new JButton("Показать таблицу");
			final TransitionTableCreator ttc = new TransitionTableCreator(m, i);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Thread th = new Thread(ttc);
					th.run();
				}
			});
			data[i][2] = new CenteredButton(button);
		}
		JTable auto = new JTable(new AutomataTableModel(data, columnNames));
		auto.setDefaultRenderer(JPanel.class, new JPanelRenderer());
		auto.setDefaultRenderer(CenteredButton.class,
				new CenteredButtonRenderer());
		auto.setDefaultEditor(CenteredButton.class, new CenteredButtonEditor());
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
		return auto;
	}

	public JPanel getAutoPanel(MooreMachine m) {
		JTable autoTable = getAutoTable(m);
		JPanel autoPanel = new JPanel();
		autoPanel.setLayout(new BoxLayout(autoPanel, BoxLayout.Y_AXIS));
		autoPanel.add(Box.createVerticalGlue());
		autoPanel.add(getSignTextPanel(m));
		autoPanel.add(Box.createVerticalStrut(Constants.DEFAULT_STRUT));
		autoPanel.add(getSignVisualPanel(m));
		autoPanel.add(Box.createVerticalStrut(5 * Constants.DEFAULT_STRUT));
		autoPanel.add(autoTable.getTableHeader());
		autoPanel.add(autoTable);
		return autoPanel;
	}

	public void setActive(int state, boolean active) {
		JPanel panel = data[state][0];
		panel.setBackground(active ? Constants.ACTIVE_STATE_COLOR
				: Constants.INACTIVE_STATE_COLOR);
	}
}
