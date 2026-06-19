package apitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.ib.client.Contract;

public class A_Table extends JPanel{
	DefaultTableModel tableModel = new DefaultTableModel();
	JTable table = new JTable(tableModel);
	JScrollPane scrollPane = new JScrollPane(table);
	
	A_Table(String [] colums){

		for(int i=0; i< colums.length; i++) {
			tableModel.addColumn(colums[i]);
		}
	
	}
	public void update(DefaultTableModel model) {
		resetTable();
		System.out.println("row count = "+tableModel.getRowCount());
		
		for(int i=0;i<model.getRowCount();i++) {
			ArrayList<String> row = new ArrayList<String>();
			for(int j=0;j<model.getColumnCount();j++) {
				String out = model.getValueAt(i, j).toString();
				row.add(out);
				}
			tableModel.insertRow(tableModel.getRowCount(), row.toArray());
		
		}
	}
	
	public void updateDetail(Object t_model, String msg) {
		DefaultTableModel model = (DefaultTableModel) t_model;
		if(msg.equals("pos")) {
			
			update(model);
		}
		else if(msg.equals("hist")) {
			
			double pos = Double.parseDouble(tableModel.getValueAt(0, 1).toString());
			double avgCost = Double.parseDouble( tableModel.getValueAt(0, 2).toString());
			double price = Double.parseDouble( model.getValueAt(model.getRowCount()-1, 5).toString());
			System.out.println(" value "+price);
			tableModel.setValueAt(price,0, 3);
			tableModel.setValueAt(avgCost-price,0, 4);
		}
		
		
	}

	public void resetTable() {
		if (tableModel.getRowCount() > 0) {
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		    	tableModel.removeRow(i);
		    	
		    }
		}
	}
}
