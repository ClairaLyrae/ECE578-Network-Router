package com.uofa.ece578.router.applet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;

import com.uofa.ece578.router.ForwardingTable;
import com.uofa.ece578.router.ForwardingTable.Entry;
import com.uofa.ece578.router.Network;
import com.uofa.ece578.router.NetworkListener;
import com.uofa.ece578.router.Router;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.JSpinner;

/**
 * GUI class for displaying forwarding table of a given router
 */
public class JForwardingTable extends JPanel implements NetworkListener {
	private static final long serialVersionUID = -7145815700584992085L;
	
	private JScrollPane scrollPane;
	private final JTable table = new JTable();
	private ForwardingTableModel jft;
	
	private Network network;
	
	/**
	 * Create the panel.
	 */
	public JForwardingTable(Network network) {
		this.network = network;
		
		scrollPane = new JScrollPane(table);
		jft = new ForwardingTableModel(network);
		
		table.setModel(jft);
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(true);

		setLayout(new BorderLayout(0, 0));
		this.add(scrollPane);
	}

	@SuppressWarnings("serial")
	public class ForwardingTableModel extends AbstractTableModel {	
			private Network nw;
			
			public ForwardingTableModel(Network n) {
				this.nw = n;
			}
			
			private String[] columnNames = {"Dest", "NextHop", "Cost"};
			
	        public int getColumnCount() {
	        	return columnNames.length;
	        }

	        public int getRowCount() {
	        	Router src = nw.getSource();
	        	if(src == null)
	        		return 0;
	        	return src.getForwardingTable().size();
	        }

	        public String getColumnName(int col) {
	        	return columnNames[col];
	        }

	        public Object getValueAt(int row, int col) {
	        	Router src = nw.getSource();
	        	if(src == null)
	        		return null;
	        	Iterator<Integer> iter = src.getForwardingTable().iterator();
	        	Integer isrc = 0;
	        	for(int i = 0; i <= row; i++) {
	        		if(!iter.hasNext())
	        			return null;
	        		isrc = iter.next();
	        	}	        
	        	Entry e = src.getForwardingTable().getEntry(isrc);
	        	if(e == null)
	        		return new Integer(0);
        		switch(col) {
	        		case 0: return isrc;
	        		case 1: return new Integer(e.nextHop);
	        		case 2: return new Float(e.cost);
	        		default: return null;
	        	}
	        }
	        
	        public Class getColumnClass(int c) {
	          return getValueAt(0, c).getClass();
	        }
	}

	@Override
	public void onEditNetwork() {
		jft.fireTableDataChanged();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(100, 1000);
	}
}
