package com.tillnagel.arcposter.legend;

import com.tillnagel.arcposter.Node;

import processing.core.PApplet;

public class LegendNode extends Node {

	public LegendNode(PApplet p) {
		super(p);
	}

	@Override
	public void draw() {
		p.strokeWeight(3);
		// p.stroke(200, 150);
		p.stroke(140);
		p.fill(33);
		p.ellipse(dotX, dotY, 8, 8); // in one line
	}
	
}
