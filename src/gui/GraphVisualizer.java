package gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import core.Constants;
import core.Mutation;

public class GraphVisualizer {
	private static int maxMeanIndex = 0;
	private static int mutationIndex = 0;

	private static void createMainFrame() {
		JLabel label1 = new JLabel("График зависимости");
		JComboBox combo1 = new JComboBox(new String[] { "максимального",
				"среднего" });
		combo1.setSelectedIndex(maxMeanIndex);
		combo1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				maxMeanIndex = cb.getSelectedIndex();
			}
		});
		JLabel label2 = new JLabel(
				"значения функции приспособленности от вероятности мутации");
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
						final int copy = maxMeanIndex;
						String title = "Зависимость "
								+ (copy == 0 ? "максимального" : "среднего")
								+ " значения функции приспособленности от вероятности мутации "
								+ Mutation.values()[mutationIndex].getRuType();
						JFrame newFrame = new JFrame(title);
						Mutation m = Mutation.values()[mutationIndex];
						JFreeChart chart = GraphPanel.getGraph(m, copy == 0);
						String screenshotDir = Constants.RESULTS_DIR + "/" + m;
						new File(screenshotDir).mkdirs();
						String screenshotFile = screenshotDir + "/"
								+ (copy == 0 ? "max" : "mean")
								+ Constants.SCREENSHOT_FILENAME;
						try {
							ChartUtilities.saveChartAsPNG(new File(
									screenshotFile), chart,
									Constants.SCREENSHOT_WIDTH,
									Constants.SCREENSHOT_HEIGHT);
						} catch (IOException e) {
							e.printStackTrace();
						}
						newFrame.add(new ChartPanel(chart));
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
		final int strut = Constants.DEFAULT_STRUT;
		panel.add(Box.createHorizontalStrut(strut));
		panel.add(label1);
		panel.add(Box.createHorizontalStrut(strut));
		panel.add(combo1);
		panel.add(Box.createHorizontalStrut(strut));
		panel.add(label2);
		panel.add(Box.createHorizontalStrut(strut));
		panel.add(combo2);
		panel.add(Box.createHorizontalStrut(strut));
		panel.add(button);
		panel.add(Box.createHorizontalStrut(strut));
		JFrame frame = new JFrame("Графики зависимостей");
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
		createMainFrame();
	}
}
