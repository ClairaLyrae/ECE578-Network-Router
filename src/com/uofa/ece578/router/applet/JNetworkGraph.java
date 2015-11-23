package com.uofa.ece578.router.applet;

import javax.swing.JPanel;

import com.uofa.ece578.router.Network;
import com.uofa.ece578.router.NetworkListener;
import com.uofa.ece578.router.Router;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

/**
 * GUI class for displaying a network graph of a given network opject
 */

public class JNetworkGraph extends JPanel implements NetworkListener {
	
	private static final long serialVersionUID = -7844496628232170644L;
	
	private boolean drawRange = false;
	private boolean drawGrid = true;
	private boolean drawRoutingTree = false;
	private boolean drawSpanningTree = false;
	private boolean drawConnectivity = false;
	
	private float[] dash = {5f};
	private BasicStroke strokeSolid = new BasicStroke();
	private BasicStroke strokeBold = new BasicStroke(2);
	private BasicStroke strokeDash = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5f, dash, 0f);
	
	public void displayRange(boolean enable) {
		drawRange = enable;
		repaint();
	}

	public void setGridSize(float f) {
		gridlines = f;
		repaint();
	}
	
	public void displayGrid(boolean enable) {
		drawGrid = enable;
		repaint();
	}

	public void displayConnectivity(boolean enable) {
		drawConnectivity = enable;
		repaint();
	}

	public void displaySpanningTree(boolean enable) {
		drawSpanningTree = enable;
		repaint();
	}

	public void displayRoutingTree(boolean enable) {
		drawRoutingTree = enable;
		repaint();
	}

	private int pad = 40;
	private int strpad = 5;
	private int icon = 8;
	private float scalex = 4f;
	private float scaley = 4f;
	private float offsetx = 0f;
	private float offsety = 0f;
	private float gridlines = 5f;
	
	private final JPanel canvas = new JPanel() {
		@Override
		public void paintComponent(Graphics g1) {
			Graphics2D g = (Graphics2D)g1;
			super.paintComponent(g);
			
			
			
			int width = this.getWidth();
			int height = this.getHeight();
			scalex = (width - pad*2)/(network.getXMax() - network.getXMin());
			scaley = (height - pad*2)/(network.getYMax() - network.getYMin());
			offsetx = network.getXMin();
			offsety = network.getYMin();
			
			if(network.size() > 0) {
				
				if(drawRange) {
					// Draw Ranges
					g.setColor(new Color(0, 0, 255, 31));
					for(Router r : network.getRouters()) {
						int xloc = (int)((r.getXPos()-offsetx)*scalex);
						int yloc = (int)((r.getYPos()-offsety)*scaley);
						g.fillOval(pad + xloc - (int)(r.getRange()*scalex), height - pad - yloc - (int)(r.getRange()*scaley), (int)(r.getRange()*scalex*2), (int)(r.getRange()*scaley*2));
					}	
					
					// Clip drawing
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, width, pad);
					g.fillRect(0, height-pad, width, pad);
					g.fillRect(0, 0, pad, width);
					g.fillRect(width - pad, 0, pad, width);
				}
			
				
			
				if(drawGrid) {
					// Draw grid & ticks
					g.setStroke(strokeDash);
					g.setColor(new Color(0, 0, 0, 127));
					String gridstr;
					float grid = (float)Math.ceil(offsetx/gridlines)*gridlines;
					while(true) {
						int xloc = (int)((grid - offsetx)*scalex);
						if(xloc >= width - pad*2)
							break;
						g.drawLine(pad + xloc, pad, pad + xloc, height - pad);
						gridstr = String.format("%.1f", grid);
						g.drawString(gridstr, pad + xloc - g.getFontMetrics().stringWidth(gridstr)/2, height - pad + g.getFontMetrics().getAscent() + strpad);
						grid += gridlines;
					}
					grid = (float)Math.ceil(offsety/gridlines)*gridlines;
					while(true) {
						int yloc = (int)((grid - offsety)*scaley);
						if(yloc >= height-pad*2)
							break;
						g.drawLine(pad, height - pad - yloc, width - pad, height - pad - yloc);
						gridstr = String.format("%.1f", grid);
						g.drawString(gridstr, pad - g.getFontMetrics().stringWidth(gridstr) - strpad, height - pad - yloc + g.getFontMetrics().getAscent()/2);
						grid += gridlines;
					}
					g.setStroke(strokeSolid);
				}
				
				
				// Draw links
				if(drawConnectivity) {
					g.setStroke(strokeDash);
					g.setColor(Color.GREEN);
					for(Router r : network.getRouters()) {
						for(Router d : network.getRouters()) {
							int xloc1 = (int)((r.getXPos()-offsetx)*scalex);
							int yloc1 = (int)((r.getYPos()-offsety)*scaley);
							int xloc2 = (int)((d.getXPos()-offsetx)*scalex);
							int yloc2 = (int)((d.getYPos()-offsety)*scaley);
							if(r.isWithinRange(d)) {
								g.drawLine(pad + xloc1, height - pad - yloc1, pad + xloc2, height - pad - yloc2);
							}
						}
					}
					g.setStroke(strokeSolid);
				}

				// Draw links
				if(drawRoutingTree) {
					Router src = network.getSource();
					if(src != null) {
						g.setStroke(strokeBold);
						g.setColor(Color.GREEN);
						for(Router r : network.getRouters()) {
							if(r != src) {
								Router prev = src;
								while(prev.getForwardingTable().hasEntry(r.getID())) {
									Router next = network.getRouterByID(prev.getForwardingTable().getNextHop(r.getID()));
									int xloc1 = (int)((prev.getXPos()-offsetx)*scalex);
									int yloc1 = (int)((prev.getYPos()-offsety)*scaley);
									int xloc2 = (int)((next.getXPos()-offsetx)*scalex);
									int yloc2 = (int)((next.getYPos()-offsety)*scaley);
									g.drawLine(pad + xloc1, height - pad - yloc1, pad + xloc2, height - pad - yloc2);
									prev = next;
								}
							}
						}
						g.setStroke(strokeSolid);						
					}
				}

				// Draw Routers
				for(Router r : network.getRouters()) {
					if(r == network.getSource())
						g.setColor(Color.RED);
					else
						g.setColor(Color.BLUE);
					int xloc = (int)((r.getXPos()-offsetx)*scalex);
					int yloc = (int)((r.getYPos()-offsety)*scaley);
					g.fillOval(pad + xloc - icon/2, height - pad - yloc - icon/2, icon, icon);
					g.drawString(Integer.toString(r.getID()), pad + xloc + icon + 2, height - pad - yloc - 2);
				}

			}
			
						

			// Draw borders
			g.setColor(Color.BLACK);
			g.drawRect(pad, pad, width - pad*2, height - pad*2);
			g.drawString("Network Graph", width/2 - g.getFontMetrics().stringWidth("Network Graph")/2, pad/2 + g.getFontMetrics().getAscent()/2);
		}
	};
	private Network network;
	
	public JNetworkGraph(Network n) {
		this.network = n;
		
		setLayout(new BorderLayout(0, 0));
		canvas.setBackground(Color.WHITE);
		add(canvas, BorderLayout.CENTER);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				int width = canvas.getWidth();
				int height = canvas.getHeight();
				scalex = (width - pad*2)/(network.getXMax() - network.getXMin());
				scaley = (height - pad*2)/(network.getYMax() - network.getYMin());
				offsetx = network.getXMin();
				offsety = network.getYMin();
				
				for(Router r : network.getRouters()) {
					int xloc = pad + (int)((r.getXPos()-offsetx)*scalex);
					int yloc = height - pad - (int)((r.getYPos()-offsety)*scaley);
					if(Math.sqrt(Math.pow(arg0.getX()-xloc, 2) + Math.pow(arg0.getY()-yloc, 2)) < 10) {
						network.setSource(r);
						break;
					}
				}
			}
		});
	}

	@Override
	public void onEditNetwork() {
		this.repaint();
	}
}
