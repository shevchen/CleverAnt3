package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import core.Constants;
import core.MooreMachine;

public class TransitionTableCreator implements Runnable {
	private MooreMachine m;
	private int state;

	private String getTitle() {
		return "Таблица переходов для " + state + " состояния";
	}

	private JPanel getTransitionTable() {
		String[] columnNames = { "Предикат", "Номер следующего состояния" };
		final int rows = 1 << Constants.SIGNIFICANT_INPUTS;
		final int cols = columnNames.length;
		String[][] data = new String[rows][cols];
		for (int i = 0; i < rows; ++i) {
			data[i][0] = String.format("%04d", i);
			data[i][1] = Integer.toString(m.getNextState(state, i));
		}
		JTable table = new JTable(data, columnNames);
		table.setRowHeight(Constants.TRANSITION_TABLE_HEIGHT);
		for (int i = 0; i < cols; ++i) {
			table.getColumnModel().getColumn(i).setPreferredWidth(
					Constants.TRANSITION_TABLE_WIDTH[i]);
		}
		table.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		table.setEnabled(false);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		final int strut = 10;
		panel.add(new JLabel(getTitle()));
		panel.add(Box.createVerticalStrut(strut));
		panel.add(table);
		return panel;
	}

	public TransitionTableCreator(MooreMachine m, int state) {
		this.m = m;
		this.state = state;
	}

	@Override
	public void run() {
		JFrame frame = new JFrame(getTitle());
		frame.add(getTransitionTable());
		frame.setVisible(true);
	}
}
