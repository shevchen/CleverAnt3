package gui;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class JPanelRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		return (JPanel) value;
	}
}
