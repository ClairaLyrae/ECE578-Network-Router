package com.uofa.ece578.router.applet;

import java.awt.EventQueue;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.uofa.ece578.router.Network;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Main class composing the application version of the network router program with a graphical UI. See {@link com.uofa.ece578.NetworkRouter} for the
 * command line version of the program.
 */

public class NetworkRouterApp {

	private static NetworkRouterApp handle;
	private Network network = new Network();
	
	private JFrame frame_EceNetworkRouter;

	private final JMenuBar menu = new JMenuBar();
	private final JMenu menuFile = new JMenu("File");
	private final JMenu menuNetwork = new JMenu("Network");
	private final JSeparator menuSeparator = new JSeparator();
	private final JMenuItem menuBtnOpen = new JMenuItem("Open File...");
	private final JMenuItem menuBtnSave = new JMenuItem("Save");
	private final JMenuItem menuBtnNew = new JMenuItem("New");
	private final JMenuItem menuBtnExit = new JMenuItem("Exit");
	private final JCheckBoxMenuItem menuBtnGrid = new JCheckBoxMenuItem("Show Grid");
	private final JCheckBoxMenuItem menuBtnRange = new JCheckBoxMenuItem("Show Ranges");
	private final JCheckBoxMenuItem menuBtnRouting = new JCheckBoxMenuItem("Show Routing Tree");
	private final JCheckBoxMenuItem menuBtnSpanning = new JCheckBoxMenuItem("Show Spannng Tree");
	private final JCheckBoxMenuItem menuBtnConnect = new JCheckBoxMenuItem("Show Connectivity");
	
	private JNetworkGraph networkGraph;
	private JRouterTable routerTable;
	private JForwardingTable forwardTable;
	private final JTabbedPane tabPane = new JTabbedPane();
	private final JSplitPane splitPane = new JSplitPane();

	public static void main(String[] args) {
		
		try {
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
        
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					handle = new NetworkRouterApp();
					handle.frame_EceNetworkRouter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public NetworkRouterApp() {
		initialize();
	}

	/**
	 * Setup application GUI
	 */
	private void initialize() {
		networkGraph = new JNetworkGraph(network);
		routerTable = new JRouterTable(network);
		forwardTable = new JForwardingTable(network);
		
		frame_EceNetworkRouter = new JFrame();
		frame_EceNetworkRouter.setTitle("ECE578 Network Router");
		frame_EceNetworkRouter.setResizable(false);
		frame_EceNetworkRouter.setBounds(100, 100, 700, 526);
		frame_EceNetworkRouter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame_EceNetworkRouter.setJMenuBar(menu);
		menu.add(menuFile);
		menu.add(menuNetwork);
		menuFile.add(menuBtnNew);
		menuFile.add(menuBtnOpen);
		menuFile.add(menuSeparator);
		menuFile.add(menuBtnSave);
		menuFile.add(menuSeparator);
		menuFile.add(menuBtnExit);
		menuNetwork.add(menuBtnGrid);
		menuNetwork.add(menuBtnRange);
		menuNetwork.add(menuBtnConnect);
		menuNetwork.add(menuBtnRouting);
		menuNetwork.add(menuBtnSpanning);
		
		menuBtnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				network.clear();
			}
		});
		menuBtnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
			    int returnVal = fc.showOpenDialog(handle.frame_EceNetworkRouter);
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            try {
			            	network.clear();
							network.appendFromFile(file.getAbsolutePath());
						} catch (Exception e) {
							e.printStackTrace();
						}
			        } else {
			        }
			}
		});
		menuBtnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
			    int returnVal = fc.showSaveDialog(handle.frame_EceNetworkRouter);
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            try {
							network.saveToFile(file.getAbsolutePath());
						} catch (Exception e) {
							e.printStackTrace();
						}
			        } else {
			        }
			}
		});
		menuBtnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handle.frame_EceNetworkRouter.dispose();
			}
		});
		menuBtnGrid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				networkGraph.displayGrid(menuBtnGrid.isSelected());
			}
		});
		menuBtnRange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				networkGraph.displayRange(menuBtnRange.isSelected());
			}
		});
		menuBtnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				networkGraph.displayConnectivity(menuBtnConnect.isSelected());
			}
		});
		menuBtnRouting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				networkGraph.displayRoutingTree(menuBtnRouting.isSelected());
			}
		});
		menuBtnSpanning.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				networkGraph.displaySpanningTree(menuBtnSpanning.isSelected());
			}
		});
		
		splitPane.setResizeWeight(0.25);
		frame_EceNetworkRouter.getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setLeftComponent(tabPane);
		splitPane.setRightComponent(networkGraph);		
		network.addListener(networkGraph);
		network.addListener(routerTable);
		network.addListener(forwardTable);
		tabPane.add(routerTable);
		tabPane.setTitleAt(0, "Router List");
		tabPane.add(forwardTable);
		tabPane.setTitleAt(1, "Forwarding Table");
		
	}
}
