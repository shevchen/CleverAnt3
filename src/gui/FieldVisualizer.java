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
import core.Cell;
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
	private JPanel[][] fieldData;
	private boolean[][] hasIcon;
	private int antRow, antColumn;
	private ImageIcon icon;
	private JButton forward, backward, skip, restart;
	private JLabel eatenLabel, turnsLabel;
	private JFrame frame;

	private void updateField() {
		fieldData[antRow][antColumn].removeAll();
		final int size = Constants.FIELD_SIZE;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				fieldData[i][j].setBackground(Constants.EMPTY_CELL_COLOR);
				if (fcopy[i][j]) {
					if (!hasIcon[i][j]) {
						JLabel label = new JLabel();
						label.setHorizontalAlignment(SwingConstants.CENTER);
						label.setIcon(icon);
						fieldData[i][j].add(label, BorderLayout.CENTER);
						hasIcon[i][j] = true;
					}
				} else {
					if (hasIcon[i][j]) {
						fieldData[i][j].removeAll();
						hasIcon[i][j] = false;
					}
				}
			}
		}
		AntState cur = stack.getLast();
		Cell[] visible = Field.getVisible(cur.row, cur.column, cur.dir);
		int signMask = m.getSignificantMask();
		JLabel ant = new JLabel(cur.dir.getVisualization(),
				SwingConstants.CENTER);
		fieldData[cur.row][cur.column]
				.setBackground(Constants.ANT_POSITION_COLOR);
		antRow = cur.row;
		antColumn = cur.column;
		fieldData[cur.row][cur.column].add(ant, BorderLayout.CENTER);
		for (int i = 0; i < visible.length; ++i) {
			fieldData[visible[i].row][visible[i].column]
					.setBackground(((signMask >> i) & 1) == 1 ? Constants.SIGNIFICANT_CELL_COLOR
							: Constants.INSIGNIFICANT_CELL_COLOR);
		}
	}

	private void updateAuto() {
		int active = stack.getLast().autoState;
		final int states = Constants.STATES_NUMBER;
		for (int i = 0; i < states; ++i) {
			apb.setActive(i, i == active);
		}
	}

	private void updateButtons() {
		restart.setEnabled(!wasFood.isEmpty());
		backward.setEnabled(!wasFood.isEmpty());
		forward.setEnabled(wasFood.size() < Constants.TURNS_NUMBER);
		skip.setEnabled(wasFood.size() < Constants.TURNS_NUMBER);
	}

	private void updateLabels() {
		eatenLabel.setText("Съедено: " + stack.getLast().eaten + "/"
				+ f.getTotalFood());
		turnsLabel.setText("Ходов сделано: " + wasFood.size() + "/"
				+ Constants.TURNS_NUMBER);
	}

	private void updateAll() {
		updateField();
		updateAuto();
		updateButtons();
		updateLabels();
		frame.repaint();
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
		fieldData = new JPanel[size][size];
		hasIcon = new boolean[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				fieldData[i][j] = new JPanel();
				fieldData[i][j].setLayout(new BorderLayout());
			}
		}
		String[] fieldColumnNames = new String[size];
		for (int i = 0; i < size; ++i) {
			fieldColumnNames[i] = "";
		}
		JTable field = new JTable(new FieldModel(fieldData, fieldColumnNames));
		field.setDefaultRenderer(JPanel.class, new JPanelRenderer());
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
		wasFood.addLast(Field.makeStep(m, current, fcopy));
		stack.addLast(current);
	}

	private void stepBackward() {
		AntState last = stack.removeLast();
		if (wasFood.removeLast()) {
			fcopy[last.row][last.column] = true;
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
		eatenLabel = new JLabel("Съедено: 0/" + f.getTotalFood());
		turnsLabel = new JLabel("Ходов сделано: 0/" + Constants.TURNS_NUMBER);
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
				while (wasFood.size() < Constants.TURNS_NUMBER) {
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
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalStrut(strut));
		buttonPanel.add(turnsLabel);
		buttonPanel.add(Box.createHorizontalStrut(2 * strut));
		buttonPanel.add(eatenLabel);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(restart);
		buttonPanel.add(forward);
		buttonPanel.add(backward);
		buttonPanel.add(skip);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(Box.createHorizontalStrut(30 * strut));
		JPanel wholePanel = new JPanel();
		wholePanel.setLayout(new BoxLayout(wholePanel, BoxLayout.Y_AXIS));
		wholePanel.add(mainPanel);
		wholePanel.add(Box.createVerticalStrut(strut));
		wholePanel.add(buttonPanel);
		frame = new JFrame("Визуализатор автомата");
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
