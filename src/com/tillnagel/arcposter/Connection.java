package com.tillnagel.arcposter;

import processing.core.PApplet;

public class Connection {

	PApplet p;
	Node fromNode;
	Node toNode;
	boolean upside = true;
	int color;

	public Connection(PApplet p, Node fromNode, Node toNode) {
		this(p, fromNode, toNode, true);
	}

	public Connection(PApplet p, Node fromNode, Node toNode, boolean upside) {
		this(p, fromNode, toNode, upside, p.color(150, 150));
	}

	public Connection(PApplet p, Node fromNode, Node toNode, boolean upside, int color) {
		this.p = p;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.upside = upside;
		this.color = color;
	}
	
	public void draw() {
		//float dist = PApplet.dist(fromNode.dotX, fromNode.dotY, toNode.dotX, toNode.dotY);
		float dist = toNode.dotX - fromNode.dotX;
		float height = dist * 0.45f * (upside ? 1 : -1);
		
		p.stroke(color);
		//p.strokeWeight(3);
		p.bezier(fromNode.dotX, fromNode.dotY, fromNode.dotX, fromNode.dotY - height, toNode.dotX,
				toNode.dotY - height,
				toNode.dotX, toNode.dotY);
	}

	@Override
	public int hashCode() {
		return fromNode.hashCode() + toNode.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connection other = (Connection) obj;
		
		// Either A>B or B>A is ok
		return (fromNode.equals(other.fromNode) && toNode.equals(other.toNode))
				|| (toNode.equals(other.fromNode) && fromNode.equals(other.toNode));
	}

}
