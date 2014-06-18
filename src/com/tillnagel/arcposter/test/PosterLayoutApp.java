package com.tillnagel.arcposter.test;

import processing.core.PApplet;

public class PosterLayoutApp extends PApplet {
	
	
	public void setup() {
		size(1024, 768);
		smooth();
	}

	public void draw() {
		float boxWidth = 80;
		float boxHeight = 60;
		float boxGap = 50;
		background(245);
		
		float x = 20;
		float yDot = 420;
		float yEven = 450;
		float yOdd = yEven + boxHeight + 10;
		for (int i = 0; i < 16; i++) {
			float xDot = x + boxWidth/2;
			float y = (i % 2 == 0) ? yEven : yOdd;
			
			stroke(0, 200);
			ellipse(xDot, yDot, 10, 10);

			stroke(0, 100);
			rect(x, y, boxWidth, boxHeight);
			
			stroke(140);
			dashline(xDot, yDot + 10, xDot, y - 5, new float[]{2});
			//line(20, 20, 100, 200);

			x = x + boxGap;
		}
	}

	/*
	 * Draw a dashed line with given set of dashes and gap lengths. x0 starting x-coordinate of line. y0 starting
	 * y-coordinate of line. x1 ending x-coordinate of line. y1 ending y-coordinate of line. spacing array giving
	 * lengths of dashes and gaps in pixels; an array with values {5, 3, 9, 4} will draw a line with a 5-pixel dash,
	 * 3-pixel gap, 9-pixel dash, and 4-pixel gap. if the array has an odd number of entries, the values are recycled,
	 * so an array of {5, 3, 2} will draw a line with a 5-pixel dash, 3-pixel gap, 2-pixel dash, 5-pixel gap, 3-pixel
	 * dash, and 2-pixel gap, then repeat.
	 */
	void dashline(float x0, float y0, float x1, float y1, float[] spacing)
	{
		float distance = dist(x0, y0, x1, y1);
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
				xSpacing[i] = lerp(0, (x1 - x0), spacing[i] / distance);
				ySpacing[i] = lerp(0, (y1 - y0), spacing[i] / distance);
			}

			i = 0;
			while (drawn < distance)
			{
				if (drawLine)
				{
					line(x0, y0, x0 + xSpacing[i], y0 + ySpacing[i]);
				}
				x0 += xSpacing[i];
				y0 += ySpacing[i];
				/* Add distance "drawn" by this line or gap */
				drawn = drawn + mag(xSpacing[i], ySpacing[i]);
				i = (i + 1) % spacing.length; // cycle through array
				drawLine = !drawLine; // switch between dash and gap
			}
		}
	}
}
