package com.uofa.ece578.router.applet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;

import com.uofa.ece578.router.Network;
import com.uofa.ece578.router.NetworkListener;
import com.uofa.ece578.router.Router;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JRouterTable extends JPanel implements NetworkListener {

	private JScrollPane scrollPane;
	private final JTable table = new JTable();
	private RouterTable routerTable;
	private Network network;
	private final JButton button_AddRouter_1 = new JButton("Add Router");
	
	/**
	 * Create the panel.
	 */
	public JRouterTable(Network n) {
		this.network = n;
		setLayout(new BorderLayout(0, 0));
		scrollPane = new JScrollPane(table);
		routerTable = new RouterTable(network);
		table.setModel(routerTable);
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(true);
		this.add(scrollPane);
		button_AddRouter_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				network.addRouter(network.getNextValidID(), 0f, 0f, 20f);
				((AbstractTableModel)table.getModel()).fireTableDataChanged();
			}
		});
		add(button_AddRouter_1, BorderLayout.SOUTH);
	}

	@SuppressWarnings("serial")
	public class RouterTable extends AbstractTableModel {
			private Network network;
			
			public RouterTable(Network n) {
				network = n;
			}
			
			private String[] columnNames = {"ID", "X Pos", "Y Pos", "Range"};
			
	        public int getColumnCount() {
	          return columnNames.length;
	        }

	        public int getRowCount() {
	          return network.size();
	        }

	        public String getColumnName(int col) {
	          return columnNames[col];
	        }

	        public Object getValueAt(int row, int col) {
	        	Router r = network.getRouterByIndex(row);
	        	if(r == null)
	        		return null;
	        	switch(col) {
	        	case 0: return new Integer(r.getID());
	        	case 1: return new Float(r.getXPos());
	        	case 2: return new Float(r.getYPos());
	        	case 3: return new Float(r.getRange());
	        	default: return null;
	        	}
	        }
	        
	        public Class getColumnClass(int c) {
	          return getValueAt(0, c).getClass();
	        }

	        public boolean isCellEditable(int row, int col) {
	        	return col > 0;
	        }

	        public void setValueAt(Object value, int row, int col) {
	        	Router r = network.getRouterByIndex(row);
	        	if(r == null)
	        		return;
	        	switch(col) {
	        	case 0: break;
	        	case 1: 
	        		if(value instanceof Float)
	        			r.setPos((Float)value, r.getYPos());
	        		break;
	        	case 2:
	        		if(value instanceof Float)
	        			r.setPos(r.getXPos(), (Float)value);
	        		break;
	        	case 3: 
	        		if(value instanceof Float)
	        			r.setRange((Float)value);
	        		break;
	        	default:
	        		break;
	        	}
				fireTableCellUpdated(row, col);
				network.callEvent();
	        }
	}

	@Override
	public void onEditNetwork() {
		routerTable.fireTableDataChanged();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(100, 1000);
	}
}
