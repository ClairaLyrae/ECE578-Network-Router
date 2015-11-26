package com.uofa.ece578.router.applet;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

/**
 * Main class composing the application version of the network router program with a graphical UI. See {@link com.uofa.ece578.NetworkRouter} for the
 * command line version of the program.
 */

public class NetworkRouterApp implements ActionListener{

	private static NetworkRouterApp handle;
	private Network network = new Network();
	private File currentFile = null;
	
	private JFrame frame_EceNetworkRouter;

	private final JMenuBar menu = new JMenuBar();
	private final JMenu menuFile = new JMenu("File");
	private final JMenu menuNetwork = new JMenu("Network");
	private final JSeparator menuSeparator = new JSeparator();
	private final JSeparator menuSeparator2 = new JSeparator();
	private final JMenuItem menuBtnOpen = new JMenuItem("Open File...");
	private final JMenuItem menuBtnSave = new JMenuItem("Save");
	private final JMenuItem menuBtnSaveAs = new JMenuItem("Save As");
	private final JMenuItem menuBtnNew = new JMenuItem("New");
	private final JMenuItem menuBtnExit = new JMenuItem("Exit");
	private final JCheckBoxMenuItem menuBtnGrid = new JCheckBoxMenuItem("Show Grid");
	private final JCheckBoxMenuItem menuBtnRange = new JCheckBoxMenuItem("Show Ranges");
	private final JCheckBoxMenuItem menuBtnRouting = new JCheckBoxMenuItem("Show Routing Tree");
	private final JCheckBoxMenuItem menuBtnBroadcast = new JCheckBoxMenuItem("Show Broadcast Tree");
	private final JCheckBoxMenuItem menuBtnConnect = new JCheckBoxMenuItem("Show Connectivity");
	
	private JNetworkGraph networkGraph;
	private JRouterTable routerTable;
	private JForwardingTable forwardTable;
	private final JTabbedPane tabPane = new JTabbedPane();
	private final JSplitPane splitPane = new JSplitPane();
	private final JRadioButtonMenuItem menuRadioGrid1 = new JRadioButtonMenuItem("Grid Spacing (1)");
	private final JRadioButtonMenuItem menuRadioGrid5 = new JRadioButtonMenuItem("Grid Spacing (5)");
	private final JRadioButtonMenuItem menuRadioGrid10 = new JRadioButtonMenuItem("Grid Spacing (10)");
	private final ButtonGroup menuRadioGridGroup = new ButtonGroup();

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
		frame_EceNetworkRouter.setIconImage(Toolkit.getDefaultToolkit().getImage(NetworkRouterApp.class.getResource("/rss.png")));
		frame_EceNetworkRouter.setTitle("ECE578 Network Router");
		frame_EceNetworkRouter.setResizable(false);
		frame_EceNetworkRouter.setBounds(100, 100, 700, 526);
		frame_EceNetworkRouter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame_EceNetworkRouter.setJMenuBar(menu);
		menu.add(menuFile);
		menu.add(menuNetwork);
		menuBtnNew.setIcon(new ImageIcon(NetworkRouterApp.class.getResource("/file.png")));
		menuFile.add(menuBtnNew);
		menuBtnOpen.setIcon(new ImageIcon(NetworkRouterApp.class.getResource("/folder_files.png")));
		menuFile.add(menuBtnOpen);
		menuFile.add(menuSeparator);
		menuBtnSave.setIcon(new ImageIcon(NetworkRouterApp.class.getResource("/save.png")));
		menuFile.add(menuBtnSave);
		menuBtnSaveAs.setIcon(new ImageIcon(NetworkRouterApp.class.getResource("/save.png")));
		menuFile.add(menuBtnSaveAs);
		menuFile.add(menuSeparator);
		menuFile.add(menuBtnExit);
		menuNetwork.add(menuBtnGrid);
		menuNetwork.add(menuBtnRange);
		menuNetwork.add(menuBtnConnect);
		menuNetwork.add(menuBtnRouting);
		menuNetwork.add(menuBtnBroadcast);
		menuNetwork.add(menuSeparator2);
		menuNetwork.add(menuRadioGrid1);
		menuNetwork.add(menuRadioGrid5);
		menuNetwork.add(menuRadioGrid10);
		
		menuRadioGridGroup.add(menuRadioGrid1);
		menuRadioGridGroup.add(menuRadioGrid5);
		menuRadioGridGroup.add(menuRadioGrid10);
		menuRadioGrid5.setSelected(true);
		
		menuBtnGrid.setSelected(true);
		
		menuRadioGrid1.addActionListener(this);
		menuRadioGrid5.addActionListener(this);
		menuRadioGrid10.addActionListener(this);
		menuBtnNew.addActionListener(this);
		menuBtnOpen.addActionListener(this);
		menuBtnSave.addActionListener(this);
		menuBtnSaveAs.addActionListener(this);
		menuBtnExit.addActionListener(this);
		menuBtnGrid.addActionListener(this);
		menuBtnRange.addActionListener(this);
		menuBtnConnect.addActionListener(this);
		menuBtnRouting.addActionListener(this);
		menuBtnBroadcast.addActionListener(this);
		
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
	
	private void saveAs() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
	    int returnVal = fc.showSaveDialog(handle.frame_EceNetworkRouter);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
				network.saveToFile(file.getAbsolutePath());
				currentFile = file;
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	private void save(File f) {
		if(f == null) {
			saveAs();
		} else {
	        try {
	        	network.saveToFile(currentFile.getAbsolutePath());
	        } catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void open() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
	    int returnVal = fc.showOpenDialog(handle.frame_EceNetworkRouter);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
            	network.clear();
				network.appendFromFile(file.getAbsolutePath());
				currentFile = file;
			} catch (Exception e) {
				e.printStackTrace();
			}
        } 
	}
	
	private void newFile() {
		network.clear();
		currentFile = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o == menuRadioGrid1) {
			networkGraph.setGridSize(1f);
		} else if (o ==	menuRadioGrid5) {
			networkGraph.setGridSize(5f);
		} else if (o ==	menuRadioGrid10) {
			networkGraph.setGridSize(5f);
		} else if (o ==	menuBtnNew) {
			newFile();
		} else if (o ==	menuBtnOpen) {
			open();
		} else if (o ==	menuBtnSave) {
			save(currentFile);
		} else if (o ==	menuBtnSaveAs) {
			saveAs();
		} else if (o ==	menuBtnExit) {
			handle.frame_EceNetworkRouter.dispose();
		} else if (o ==	menuBtnGrid) {
			networkGraph.displayGrid(menuBtnGrid.isSelected());
		} else if (o ==	menuBtnRange) {
			networkGraph.displayRange(menuBtnRange.isSelected());
		} else if (o ==	menuBtnRange) {
			networkGraph.displayRange(menuBtnRange.isSelected());
		} else if (o ==	menuBtnConnect) {
			networkGraph.displayConnectivity(menuBtnConnect.isSelected());
		} else if (o ==	menuBtnRouting) {
			networkGraph.displayRoutingTree(menuBtnRouting.isSelected());
		} else if (o ==	menuBtnBroadcast) {
			networkGraph.displayBroadcastTree(menuBtnBroadcast.isSelected());
		}
	}
}
