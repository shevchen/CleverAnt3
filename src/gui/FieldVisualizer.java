package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import main.MooreMachineParser;
import core.AntState;
import core.Constants;
import core.Direction;
import core.Field;
import core.MooreMachine;

public class FieldVisualizer {
	private Field f;
	private MooreMachine m;
	private int currentState;
	private int currentRow, currentColumn;
	private Direction currentDir;
	private int stepsDone;
	private LinkedList<AntState> stack;

	private JPanel getRandomField() {
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

	private void initAuto() {
		try {
			m = MooreMachineParser.parseBest();
		} catch (IOException e) {
			e.printStackTrace();
			m = null;
			return;
		}
		currentState = m.getStartState();
		currentRow = Constants.START_ROW;
		currentColumn = Constants.START_COLUMN;
		currentDir = Constants.START_DIRECTION;
		stepsDone = 0;
		stack = new LinkedList<AntState>();
	}

	private void createFieldFrame() {
		initAuto();
		if (m == null) {
			return;
		}
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		final int strut = 10;
		panel.add(Box.createHorizontalStrut(strut));
		panel.add(getRandomField());
		panel.add(Box.createHorizontalStrut(strut));
		panel.add(Box.createHorizontalGlue());
		panel.add(AutomataPanelBuilder.getAutoPanel(m));
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
		new FieldVisualizer().createFieldFrame();
	}
}
