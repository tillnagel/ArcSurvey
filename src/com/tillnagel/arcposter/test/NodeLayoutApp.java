package com.tillnagel.arcposter.test;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

import com.tillnagel.arcposter.Connection;
import com.tillnagel.arcposter.Node;

public class NodeLayoutApp extends PApplet {

	List<Node> nodes;
	List<Connection> connections;

	public void setup() {
		size(1400, 768);
		smooth();

		nodes = loadNodes();
		connections = createConnections(nodes);

		setNodePositions(nodes);
	}

	public List<Connection> createConnections(List<Node> nodes) {
		List<Connection> connections = new ArrayList<Connection>();

		for (int i = 0; i < 20; i++) {
			int fromIndex = (int) random(nodes.size());
			int toIndex = (int) random(nodes.size());
			Connection connection = new Connection(this, nodes.get(fromIndex), nodes.get(toIndex));
			connections.add(connection);
		}

		return connections;
	}

	public List<Node> loadNodes() {
		String[] names = new String[] { "shapeofsong", "bibleviz", "similardiversity", "edgemaps", "globaltradeflows",
				"heer-vizzoo", "improvise-arcs", "irc-arcs", "limerickvis", "neoformix", "pivotgraph",
				"threadarcs1", "gundeaths", "heinen", "arctrees", "linkology", "arcdiagrams", "beatles",
				"biblecross", "sankeyarcs-bw" };

		List<Node> nodes = new ArrayList<Node>();
		for (String name : names) {
			PImage thumbnail = loadImage("arc-thumbnails/" + name + ".png");
			nodes.add(new Node(this, name, thumbnail));
		}
		return nodes;
	}

	public void setNodePositions(List<Node> nodes) {
		float boxWidth = 90;
		float boxHeight = 68;
		float boxGap = 60;
		float dotY = 420;
		float yEven = 450;
		float yOdd = yEven + boxHeight + 20;
		float boxX = 20;

		for (int i = 0; i < nodes.size(); i++) {
			float dotX = boxX + boxWidth / 2;
			float boxY = (i % 2 == 0) ? yEven : yOdd;

			Node node = nodes.get(i);
			node.setPositions(dotX, dotY, boxX, boxY, boxWidth, boxHeight);

			boxX = boxX + boxGap;
		}
	}

	public void draw() {
		background(33);

		for (Connection connection : connections) {
			connection.draw();
		}

		for (Node node : nodes) {
			node.draw();
		}

	}

}
