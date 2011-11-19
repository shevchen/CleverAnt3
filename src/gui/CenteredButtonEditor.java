package gui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class CenteredButtonEditor extends AbstractCellEditor implements
		TableCellEditor {

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		return (CenteredButton) value;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}
}
