package com.tillnagel.arcposter.legend;

import com.tillnagel.arcposter.Connection;
import com.tillnagel.arcposter.Node;

import processing.core.PApplet;

public class LegendApp extends PApplet {

	Node nodeA;
	Node nodeB;
	Connection connAB;
	
	public void setup() {
		size(400, 400, PDF, "legend-gray.pdf");
		smooth();
		
		nodeA = new LegendNode(this);
		nodeA.setPositions(100, 100, 0, 0, 0, 0);
		nodeB = new LegendNode(this);
		nodeB.setPositions(150, 100, 0, 0, 0, 0);
		
		connAB = new Connection(this, nodeA, nodeB, false);
	}

	public void draw() {
		background(33);

		strokeWeight(3);
		noFill();
			
		strokeWeight(3);
		noFill();
		
		//stroke(255, 255, 0, 190);
		//stroke(0, 255, 255, 190);
		stroke(150, 200);
		connAB.draw();

		
		nodeA.draw();
		nodeB.draw();
		
		exit();
	}

}
