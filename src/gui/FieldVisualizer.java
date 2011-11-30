package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

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
import core.AntState;
import core.Constants;
import core.Field;
import core.MooreMachine;

public class FieldVisualizer {
	private Field f;
	private boolean[][] fcopy;
	private MooreMachine m;
	private AutomataPanelBuilder apb;
	private LinkedList<AntState> stack;
	private LinkedList<Boolean> wasFood;
	private JLabel[][] fieldData;
	private ImageIcon icon;
	private JButton forward, backward, skip, restart;

	private void updateField() {
		final int size = Constants.FIELD_SIZE;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				if (fcopy[i][j]) {
					fieldData[i][j].setIcon(icon);
				} else {
					fieldData[i][j].removeAll();
				}
			}
		}
	}

	private void updateAuto() {
		int active = stack.getLast().currentState;
		final int states = Constants.STATES_NUMBER;
		for (int i = 0; i < states; ++i) {
			apb.setActive(i, i == active);
		}
	}

	private void updateButtons() {
		backward.setEnabled(!wasFood.isEmpty());
		forward.setEnabled(wasFood.size() < Constants.TURNS_NUMBER);
	}

	private void updateAll() {
		updateField();
		updateAuto();
		updateButtons();
	}

	private JPanel getRandomField() {
		f = new Field();
		final int size = Constants.FIELD_SIZE;
		fcopy = new boolean[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				fcopy[i][j] = f.hasFood(i, j);
			}
		}
		icon = new ImageIcon(Constants.FOOD_FILENAME);
		fieldData = new JLabel[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				fieldData[i][j] = new JLabel();
				fieldData[i][j].setHorizontalAlignment(SwingConstants.CENTER);
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

	private void initAuto() {
		try {
			m = MooreMachineParser.parseBest();
		} catch (IOException e) {
			e.printStackTrace();
			m = null;
			return;
		}
		stack = new LinkedList<AntState>();
		stack.add(new AntState(m.getStartState(), Constants.START_ROW,
				Constants.START_COLUMN, Constants.START_DIRECTION, 0));
		wasFood = new LinkedList<Boolean>();
	}

	private void stepForward() {
		AntState current = stack.getLast().clone();
		wasFood.addLast(fcopy[current.currentRow][current.currentColumn]);
		Field.makeStep(m, current, fcopy);
		stack.addLast(current);
	}

	private void stepBackward() {
		stack.removeLast();
		AntState current = stack.getLast();
		if (wasFood.removeLast()) {
			fcopy[current.currentRow][current.currentColumn] = true;
		}
	}

	private void createFieldFrame() {
		initAuto();
		if (m == null) {
			return;
		}
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		final int strut = 10;
		mainPanel.add(Box.createHorizontalStrut(strut));
		mainPanel.add(getRandomField());
		mainPanel.add(Box.createHorizontalStrut(strut));
		mainPanel.add(Box.createHorizontalGlue());
		apb = new AutomataPanelBuilder();
		mainPanel.add(apb.getAutoPanel(m));
		mainPanel.add(Box.createHorizontalStrut(strut));
		forward = new JButton("Шаг вперёд");
		forward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stepForward();
				updateAll();
			}
		});
		backward = new JButton("Шаг назад");
		backward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stepBackward();
				updateAll();
			}
		});
		skip = new JButton("В конец");
		skip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while (wasFood.size() < 200) {
					stepForward();
				}
				updateAll();
			}
		});
		restart = new JButton("Рестарт");
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while (!wasFood.isEmpty()) {
					stepBackward();
				}
				updateAll();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(restart);
		buttonPanel.add(forward);
		buttonPanel.add(backward);
		buttonPanel.add(skip);
		JPanel wholePanel = new JPanel();
		wholePanel.setLayout(new BoxLayout(wholePanel, BoxLayout.Y_AXIS));
		wholePanel.add(mainPanel);
		wholePanel.add(buttonPanel);
		JFrame frame = new JFrame("Визуализатор автомата");
		frame
				.setLayout(new BoxLayout(frame.getContentPane(),
						BoxLayout.Y_AXIS));
		frame.add(Box.createVerticalStrut(strut));
		frame.add(wholePanel);
		frame.add(Box.createVerticalStrut(strut));
		updateField();
		updateAuto();
		updateButtons();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new FieldVisualizer().createFieldFrame();
	}
}
