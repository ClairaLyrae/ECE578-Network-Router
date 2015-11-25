package SecondPart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;





public class GraphingNetwork extends JFrame{
	private ArrayList<Router> _network;
	private double _Xmin, _Ymin, _Xmax, _Ymax;
	private double _rangeX, _rangeY;
	private int _part;
	private int _sourceNumber;
	
	static final int _MARGIN = 60;
	static final int _WIDTH = 1000;
	static final int _HEIGHT = 900;

public GraphingNetwork(ArrayList<Router> network, int sourceNumber, int part){
	super("Connectivity Graph");
	_network = network;
	_part = part;
	_sourceNumber = sourceNumber;
	setUpLogic();
	this.setSize(_WIDTH+40, _HEIGHT+80);
	this.add(new Graph());
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setVisible(true);
	
}
private void setUpLogic() {
	_Xmin = Integer.MAX_VALUE;
	_Ymin = Integer.MAX_VALUE;
	_Xmax = Integer.MIN_VALUE;
	_Ymax = Integer.MIN_VALUE;
	for (Router n : _network){
		if (n.getX() < _Xmin) _Xmin = n.getX();
		if (n.getX() > _Xmax) _Xmax = n.getX();
		if (n.getY() < _Ymin) _Ymin = n.getY();
		if (n.getY() > _Ymax) _Ymax = n.getY();
	}
	_rangeX = _Xmax - _Xmin;
	_rangeY = _Ymax - _Ymin; 
	
}

private class Graph extends JPanel{
	
	private double _deltaX;
	private double _deltaY;
	
	public Graph(){
		this.setSize(_WIDTH, _HEIGHT);
		setUp();
	}
	
	private void setUp() {
		_deltaX = (_WIDTH-_MARGIN-_MARGIN)/_rangeX;
		_deltaY = (_HEIGHT-_MARGIN-_MARGIN)/_rangeY;
		this.setBackground(Color.WHITE);
	}

	public void paint(Graphics g) {
	      Graphics2D g2 = (Graphics2D)g;
	      g2.drawRect(_MARGIN, _MARGIN, _WIDTH-_MARGIN-_MARGIN, _HEIGHT-_MARGIN-_MARGIN);
	      
	      drawDashedLines(g);
	      drawNodes(g);
	      drawConnections(g);
	    
	   }

	private void drawConnections(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Graphics2D g2t = (Graphics2D) g.create();
	    g2t.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    RenderingHints.VALUE_ANTIALIAS_ON);
	    Font font = new Font("Serif", Font.BOLD, 25);
	    g2t.setFont(font);
		
		g2d.setColor(Color.BLACK);
		for (Router n : _network){
			for (Router inN : n.getroutersInRange()){
				int x1 = _MARGIN +(int)((n.getX()-_Xmin)*_deltaX);
				int y1 = _HEIGHT -_MARGIN- (int)((n.getY()-_Ymin)*_deltaY);
				int x2 = _MARGIN +(int)((inN.getX()-_Xmin)*_deltaX);
				int y2 = _HEIGHT -_MARGIN- (int)((inN.getY()-_Ymin)*_deltaY);
				g2d.drawLine(x1, y1, x2, y2);
			}
		}
		Router source = null;
		g2d.setColor(Color.BLUE);
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
		for (Router n : _network){
			if (n.getprevRouter() != null){
				int x1 = _MARGIN +(int)((n.getX()-_Xmin)*_deltaX);
				int y1 = _HEIGHT -_MARGIN- (int)((n.getY()-_Ymin)*_deltaY);
				int x2 = _MARGIN +(int)((n.getprevRouter().getX()-_Xmin)*_deltaX);
				int y2 = _HEIGHT -_MARGIN- (int)((n.getprevRouter().getY()-_Ymin)*_deltaY);
				g2d.drawLine(x1, y1, x2, y2);
			} else if (n.getrouterNumber() == _sourceNumber){
				source = n;
				g2t.setColor(Color.RED);
				int x = _MARGIN +(int)((n.getX()-_Xmin)*_deltaX)-10;
				int y = _HEIGHT -_MARGIN- (int)((n.getY()-_Ymin)*_deltaY)-10;
				g2t.fillOval(x, y, 20, 20);
				g2t.setColor(Color.BLACK);
				g2t.fillOval(x+5, y+5, 10, 10);
				g2t.drawString(String.valueOf(n.getrouterNumber()), x+11, y);
			}
		}
		
		if (_part == 2){
			g2t.drawString("Spanning Tree for Router "+source.getrouterNumber(), _WIDTH/4 - 100, _MARGIN/2);
		}
		
		g2t.dispose();
		g2d.dispose();
	}

	private void drawNodes(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Graphics2D g2t = (Graphics2D) g.create();
	    g2t.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    RenderingHints.VALUE_ANTIALIAS_ON);
	    Font font = new Font("Serif", Font.BOLD, 25);
	    g2t.setFont(font);
	    
		for (Router n : _network){
			int x = _MARGIN +(int)((n.getX()-_Xmin)*_deltaX)-10;
			int y = _HEIGHT -_MARGIN- (int)((n.getY()-_Ymin)*_deltaY)-10;
			g2d.fillOval(x, y, 20, 20);
			g2t.drawString(String.valueOf(n.getrouterNumber()), x+11, y);
		}
		
		g2t.dispose();
		g2d.dispose();
	}

	private void drawDashedLines(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Graphics2D g2t = (Graphics2D) g.create();
	    g2t.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    RenderingHints.VALUE_ANTIALIAS_ON);
	    Font font = new Font("Serif", Font.PLAIN, 20);
	    g2t.setFont(font);

        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        for (int i = (int)_Xmin; i < _Xmax; ++i){
        	if ((int)i%5==0) {
        		int x = _MARGIN +(int)(((i-_Xmin)*_deltaX));
        		g2d.drawLine(x, _MARGIN, x, _HEIGHT-_MARGIN);
        		g2t.drawString(String.valueOf((int)i), x, _HEIGHT-(_MARGIN/2));
        	}
	    }
	    for (int i = (int)_Ymin; i < _Ymax; ++i){
	    	if ((int)i%5==0) {
	    		int y = _HEIGHT -_MARGIN- (int)(((i-_Ymin)*_deltaY));
	    		g2d.drawLine(_MARGIN, y, _WIDTH-_MARGIN, y);
	    		g2t.drawString(String.valueOf((int)i), _MARGIN/3, y);
	    	}
	    }
	    
	    g2t.dispose();
        g2d.dispose();
		
	}
}
}
