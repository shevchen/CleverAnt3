package gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.Mutation;

public class GraphVisualizer {
	private static int maxMeanIndex = 0;
	private static int mutationIndex = 0;

	private static void createMainFrame() {
		JLabel label = new JLabel("График зависимости");
		JComboBox combo = new JComboBox(new String[] { "максимального",
				"среднего" });
		combo.setSelectedIndex(maxMeanIndex);
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				maxMeanIndex = cb.getSelectedIndex();
			}
		});
		JLabel label2 = new JLabel(
				"значения fitness-функции от вероятности мутации");
		Mutation[] values = Mutation.values();
		String[] names = new String[values.length];
		for (int i = 0; i < values.length; ++i) {
			names[i] = values[i].getRuType();
		}
		JComboBox combo2 = new JComboBox(names);
		combo2.setSelectedIndex(mutationIndex);
		combo2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				mutationIndex = cb.getSelectedIndex();
			}
		});
		JButton button = new JButton("Показать график");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th = new Thread(new Runnable() {
					@Override
					public void run() {
						JFrame newFrame = new JFrame();
						newFrame.add(GraphPanel.getGraph(
								Mutation.values()[mutationIndex],
								maxMeanIndex == 0));
						newFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
						newFrame
								.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						newFrame.setVisible(true);
					}
				});
				th.start();
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(5));
		panel.add(label);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(combo);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(label2);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(combo2);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(button);
		panel.add(Box.createHorizontalStrut(5));
		JFrame frame = new JFrame();
		frame
				.setLayout(new BoxLayout(frame.getContentPane(),
						BoxLayout.Y_AXIS));
		frame.add(Box.createVerticalStrut(5));
		frame.add(panel);
		frame.add(Box.createVerticalStrut(5));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		createMainFrame();
	}
}
