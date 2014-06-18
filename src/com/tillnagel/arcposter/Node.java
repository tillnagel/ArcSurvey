package com.tillnagel.arcposter;

import processing.core.PApplet;
import processing.core.PImage;

public class Node {

	protected PApplet p;

	boolean showGapBetweenNodeAndEdge = true;

	// Visual properties
	protected float dotX, dotY;
	protected float boxX, boxY;
	protected float boxWidth, boxHeight;

	// Data properties (of the arc diagram)
	protected String name;
	protected PImage thumbnail;
	protected String reference;

	// Arc classification properties
	protected String distance;
	protected String directionality;
	protected String weight;
	protected Integer year;

	// Simple constructor used in sub-class LegendNode
	public Node(PApplet p) {
		this.p = p;
	}

	public Node(PApplet p, String name, PImage thumbnail) {
		this.p = p;
		this.name = name;
		this.thumbnail = thumbnail;
	}

	public Node(PApplet p, String name, PImage thumbnail, String reference, String distance, String directionality,
			String weight, Integer year) {
		this(p, name, thumbnail);
		this.reference = reference;

		this.distance = distance.toLowerCase();
		this.directionality = directionality.toLowerCase();
		this.weight = weight.toLowerCase();
		this.year = year;

		PApplet.println("Created " + name + ": " + distance + ", " + directionality + ", " + weight);
	}

	public void setPositions(float dotX, float dotY, float boxX, float boxY, float boxWidth, float boxHeight) {
		this.dotX = dotX;
		this.dotY = dotY;
		this.boxX = boxX;
		this.boxY = boxY;
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
	}

	public void draw() {

		if (thumbnail != null) {
			// Thumbnail of arc diagram
			drawThumbnail();
			drawThumbnailOutline();
		}

		// Gap around node circles
		showGapBetweenNodeAndEdge = false;
		if (showGapBetweenNodeAndEdge) {
			p.noStroke();
			p.fill(33);
			p.ellipse(dotX, dotY, 12, 12);
		}

		// Node circles
		p.strokeWeight(3);
		// p.stroke(200, 150);
		p.stroke(140);
		p.fill(33);
		p.ellipse(dotX, dotY, 8, 8); // in one line
		// p.ellipse(dotX, boxY -20, 8, 8); // close to thumbnail

		// Dashed line from edge to node
		p.strokeWeight(3);
		p.stroke(140, 150);
		dashline(dotX, dotY + 10, dotX, boxY - 5, new float[] { 1, 6 });

		// ---

		// 2nd nodes below
		if (showGapBetweenNodeAndEdge) {
			p.noStroke();
			p.fill(33);
			p.ellipse(dotX, dotY + 220, 12, 12);
		}
		p.strokeWeight(3);
		p.stroke(140);
		p.fill(33);
		p.ellipse(dotX, dotY + 220, 8, 8);
		p.stroke(140, 150);
		dashline(dotX, dotY + 220 - 10, dotX, boxY + boxHeight, new float[] { 1, 6 });

	}

	public void drawThumbnailOutline() {
		p.strokeWeight(1);
		p.stroke(255, 100);
		p.noFill();
		p.rect(boxX, boxY, boxWidth, boxHeight);
	}

	public void drawThumbnail(float x, float y, float w, float h) {
		// Use background color of thumbnail
		int backgroundColor = thumbnail.get(0, 0);
		p.noStroke();
		p.fill(backgroundColor);
		p.rect(x, y, w, h);

		// Fit thumbnail into rect
		float scaleW = w / thumbnail.width;
		float scaleH = h / thumbnail.height;
		float scale = PApplet.min(scaleW, scaleH);
		float thumbnailWidth = thumbnail.width * scale;
		float thumbnailHeight = thumbnail.height * scale;

		// Center thumbnail
		float thumbnailX = x + (w / 2 - thumbnailWidth / 2);
		float thumbnailY = y + (h / 2 - thumbnailHeight / 2);

		p.image(thumbnail, thumbnailX, thumbnailY, thumbnailWidth, thumbnailHeight);
	}

	public void drawThumbnail() {
		drawThumbnail(boxX, boxY, boxWidth, boxHeight);
	}

	/**
	 * Draw a dashed line with given set of dashes and gap lengths. x0 starting x-coordinate of line. y0 starting
	 * y-coordinate of line. x1 ending x-coordinate of line. y1 ending y-coordinate of line. spacing array giving
	 * lengths of dashes and gaps in pixels; an array with values {5, 3, 9, 4} will draw a line with a 5-pixel dash,
	 * 3-pixel gap, 9-pixel dash, and 4-pixel gap. if the array has an odd number of entries, the values are recycled,
	 * so an array of {5, 3, 2} will draw a line with a 5-pixel dash, 3-pixel gap, 2-pixel dash, 5-pixel gap, 3-pixel
	 * dash, and 2-pixel gap, then repeat.
	 */
	void dashline(float x0, float y0, float x1, float y1, float[] spacing)
	{
		float distance = PApplet.dist(x0, y0, x1, y1);
		float[] xSpacing = new float[spacing.length];
		float[] ySpacing = new float[spacing.length];
		float drawn = 0.0f; // amount of distance drawn

		if (distance > 0)
		{
			int i;
			boolean drawLine = true; // alternate between dashes and gaps

			/*
			 * Figure out x and y distances for each of the spacing values I decided to trade memory for time; I'd
			 * rather allocate a few dozen bytes than have to do a calculation every time I draw.
			 */
			for (i = 0; i < spacing.length; i++)
			{
				xSpacing[i] = PApplet.lerp(0, (x1 - x0), spacing[i] / distance);
				ySpacing[i] = PApplet.lerp(0, (y1 - y0), spacing[i] / distance);
			}

			i = 0;
			while (drawn < distance)
			{
				if (drawLine)
				{
					p.line(x0, y0, x0 + xSpacing[i], y0 + ySpacing[i]);
				}
				x0 += xSpacing[i];
				y0 += ySpacing[i];
				/* Add distance "drawn" by this line or gap */
				drawn = drawn + PApplet.mag(xSpacing[i], ySpacing[i]);
				i = (i + 1) % spacing.length; // cycle through array
				drawLine = !drawLine; // switch between dash and gap
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getDistance() {
		return distance;
	}

	public String getDirectionality() {
		return directionality;
	}

	public String getWeight() {
		return weight;
	}

	public Integer getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isHit(int checkX, int checkY) {
		return checkX > boxX && checkY > boxY && checkX < boxX + boxWidth && checkY < boxY + boxHeight;
	}

}
