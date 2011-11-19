package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

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
		JLabel[][] data = new JLabel[rows][cols];
		for (int i = 0; i < rows; ++i) {
			data[i][0] = new JLabel(MooreMachine.getBitString(i,
					Constants.SIGNIFICANT_INPUTS), SwingConstants.CENTER);
			data[i][1] = new JLabel(Integer.toString(m.getNextState(state, i)),
					SwingConstants.CENTER);
		}
		JTable table = new JTable(new TransitionTableModel(data, columnNames));
		table.setDefaultRenderer(JLabel.class, new JLabelRenderer());
		table.setRowHeight(Constants.TRANSITION_TABLE_HEIGHT);
		for (int i = 0; i < cols; ++i) {
			table.getColumnModel().getColumn(i).setPreferredWidth(
					Constants.TRANSITION_TABLE_WIDTH[i]);
		}
		table.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		table.getTableHeader().setBorder(
				BorderFactory.createLineBorder(Color.BLACK));
		table.setEnabled(false);
		JPanel labelPanel = new JPanel();
		labelPanel.add(new JLabel(getTitle()));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		final int strut = Constants.DEFAULT_STRUT;
		panel.add(labelPanel);
		panel.add(Box.createVerticalStrut(strut));
		panel.add(table.getTableHeader());
		panel.add(table);
		JPanel finalPanel = new JPanel();
		finalPanel.setLayout(new BorderLayout());
		finalPanel.add(Box.createVerticalStrut(strut), BorderLayout.NORTH);
		finalPanel.add(Box.createVerticalStrut(strut), BorderLayout.SOUTH);
		finalPanel.add(Box.createHorizontalStrut(strut), BorderLayout.WEST);
		finalPanel.add(Box.createHorizontalStrut(strut), BorderLayout.EAST);
		finalPanel.add(panel, BorderLayout.CENTER);
		return finalPanel;
	}

	public TransitionTableCreator(MooreMachine m, int state) {
		this.m = m;
		this.state = state;
	}

	@Override
	public void run() {
		JFrame frame = new JFrame(getTitle());
		frame.add(getTransitionTable());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
