package gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class FieldRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof JLabel) {
			return (JLabel) value;
		}
		return super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
	}
}
