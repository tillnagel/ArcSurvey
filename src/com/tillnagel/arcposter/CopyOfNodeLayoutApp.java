package com.tillnagel.arcposter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PImage;

import com.tillnagel.arcposter.comparators.NodeDistanceComparator;
import com.tillnagel.arcposter.comparators.NodeYearComparator;

public class CopyOfNodeLayoutApp extends PApplet {

	boolean renderAsPdf = false;

	List<Node> nodes;
	Node selectedNode = null;

	Set<Connection> topConnections;
	Set<Connection> nonWeightedConnections;
	Set<Connection> directedConnections;
	
	PImage titleImg;

	List<ConnectionConfig> connectionConfigs = new ArrayList<ConnectionConfig>();

	class ConnectionConfig {
		NodeFilter nodeFilter;
		boolean upside;
		int color;
		public ConnectionConfig(NodeFilter nodeFilter, boolean upside, int color) {
			this.nodeFilter = nodeFilter;
			this.upside = upside;
			this.color = color;
		}
	}
	
	
	public static void main(String[] args) {
		String[] params = new String[] { "--present", "--bgcolor=#000000", "--hide-stop", "--exclusive",
				"com.tillnagel.arcposter.NodeLayoutApp" };

		PApplet.main(params);
	}
	
	public void setup() {
		if (renderAsPdf) {
			// size(1189, 841, PDF, "arc-poster.pdf"); // DIN A0 : 1189 x 841
			size(1413, 1000, PDF, "arc-poster.pdf");
		} else {
			size(1300, 768);
			//size(1440, 900);
		}
		smooth();
		
		titleImg = loadImage("arcsurvey-title.png");

		// Load nodes
		nodes = loadNodes();
		// Order nodes
		Collections.sort(nodes, new NodeYearComparator());
		// Set positions based on order
		setNodePositions(nodes);

		// Create connections
		connectionConfigs.add(new ConnectionConfig(weightedNodeFilter, true, color(255, 255, 0, 130)));
		connectionConfigs.add(new ConnectionConfig(nonWeightedNodeFilter, true, color(0, 255, 255, 130)));
		connectionConfigs.add(new ConnectionConfig(directedNodeFilter, false, color(150, 150)));

//		topConnections = createConnections(nodes, weightedNodeFilter, true, color(255, 255, 0, 130));
//		nonWeightedConnections = createConnections(nodes, nonWeightedNodeFilter, true, color(0, 255, 255, 130));
//		directedConnections = createConnections(nodes, directedNodeFilter, false, color(150, 150));
	}
	
	

	public Set<Connection> createConnections(List<Node> nodes, NodeFilter nodeFilter, boolean upside, int color) {
		Set<Connection> connections = new HashSet<Connection>();
		for (Node nodeA : nodes) {
			for (Node nodeB : nodes) {
				if (nodeFilter.hasSameProperty(nodeA, nodeB)) {
					Connection connection = new Connection(this, nodeA, nodeB, upside, color);
					connections.add(connection);
				}
			}
		}
		return connections;
	}

	public List<Node> loadNodes() {
		List<Node> nodes = new ArrayList<Node>();

		String[] arcExamplesData = loadStrings("arc-examples-data.csv");
		int i = -1;
		for (String row : arcExamplesData) {
			i++;
			if (i == 0)
				continue;

			String[] cols = row.split(";");

			String name = cols[0];
			String thumbnailName = cols[1];
			PImage thumbnail = loadImage("arc-thumbnails/" + thumbnailName + ".png");

			String distance = cols[2];
			String directionality = (cols.length > 4) ? cols[4] : "";
			String weight = (cols.length > 5) ? cols[5] : "";

			String reference = cols[15];
			String yearStr = cols[16];
			if (yearStr != null && !yearStr.trim().isEmpty()) {
				Integer year = new Integer(yearStr);

				nodes.add(new Node(this, name, thumbnail, reference, distance, directionality, weight, year));
			}
		}
		return nodes;
	}

	public void setNodePositions(List<Node> nodes) {
		float boxWidth = 90;
		float boxHeight = 68;
		float boxGap = 60;
		// float dotY = 500;
		// float yEven = 530;
		float xOffset = 90;
		float yOffset = -30;
		float dotY = 400 + yOffset;
		float yEven = 430 + yOffset;
		float yOdd = yEven + boxHeight + 20;
		float boxX = 20 + xOffset;

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
		strokeWeight(3);
		noFill();

		pushMatrix();
		translate(0, 226);
		for (Connection connection : directedConnections) {
			connection.draw();
		}
		popMatrix();

		for (Connection connection : topConnections) {
			connection.draw();
		}
		for (Connection connection : nonWeightedConnections) {
			connection.draw();
		}

		// Draws nodes, and up and down dotted lines
		for (Node node : nodes) {
			node.draw();
		}
		
		image(titleImg, 24, 24);

		// Draw selected Node
		if (selectedNode != null) {

			// Darken background (arcs arc)
			noStroke();
			fill(0, 140);
			rect(0, 0, width, height);

			float w = min(selectedNode.thumbnail.width, 1000);
			float h = min(selectedNode.thumbnail.height, 600);
			float x = width / 2 - (w / 2);
			float y = height / 2 - (h / 2);

			// Information box (for image plus metadata)
			noStroke();
			fill(127);
			rect(x - 5, y - 5, w + 10, h + 50);
			fill(255);
			textSize(20);
			text(selectedNode.name, x, y + h + 40 - 15);
			textSize(12);
			text(selectedNode.reference, x, y + h + 50 - 10);

			// Large image of arc vis
			selectedNode.drawThumbnail(x, y, w, h);
		}

		if (renderAsPdf) {
			exit();
		}
	}

	public void mouseClicked() {
		selectedNode = null;
		for (Node node : nodes) {
			if (node.isHit(mouseX, mouseY)) {
				selectedNode = node;
			}
		}
	}

	public void keyPressed() {
		if (key == 'd') {
			directedConnections = createConnections(nodes, nonDirectedNodeFilter, false, color(150, 150));
		}
		if (key == 'D') {
			directedConnections = createConnections(nodes, directedNodeFilter, false, color(150, 150));
		}
		

		if (key == 'l') {
			directedConnections = createConnections(nodes, distanceLayoutNodeFilter, false, color(150, 150));
		}
		if (key == 'L') {
			directedConnections = createConnections(nodes, distanceDataNodeFilter, false, color(150, 150));
		}

		if (key == 'y') {
			Collections.sort(nodes, new NodeYearComparator());
			setNodePositions(nodes);
		}
		if (key == '1') {
			Collections.sort(nodes, new NodeDistanceComparator());
			setNodePositions(nodes);
		}
	}
	
	public void updateAllConnections() {
		
	}

	
	NodeFilter directedNodeFilter = new NodeFilter() {
		public boolean hasSameProperty(Node nodeA, Node nodeB) {
			return nodeA.directionality.equals("directed") && nodeA.directionality.equals(nodeB.directionality)
					&& !nodeA.equals(nodeB);
		}
	};

	NodeFilter nonDirectedNodeFilter = new NodeFilter() {
		public boolean hasSameProperty(Node nodeA, Node nodeB) {
			return nodeA.directionality.equals("non") && nodeA.directionality.equals(nodeB.directionality)
					&& !nodeA.equals(nodeB);
		}
	};

	NodeFilter weightedNodeFilter = new NodeFilter() {
		public boolean hasSameProperty(Node nodeA, Node nodeB) {
			return nodeA.weight.equals("yes") && nodeA.weight.equals(nodeB.weight) && !nodeA.equals(nodeB);
		}
	};

	NodeFilter nonWeightedNodeFilter = new NodeFilter() {
		public boolean hasSameProperty(Node nodeA, Node nodeB) {
			return nodeA.weight.equals("no") && nodeA.weight.equals(nodeB.weight) && !nodeA.equals(nodeB);
		}
	};
	
	NodeFilter distanceLayoutNodeFilter = new NodeFilter() {
		public boolean hasSameProperty(Node nodeA, Node nodeB) {
			return nodeA.distance.equals("layout") && nodeA.distance.equals(nodeB.distance) && !nodeA.equals(nodeB);
		}
	};

	NodeFilter distanceDataNodeFilter = new NodeFilter() {
		public boolean hasSameProperty(Node nodeA, Node nodeB) {
			return nodeA.distance.equals("data") && nodeA.distance.equals(nodeB.distance) && !nodeA.equals(nodeB);
		}
	};

	NodeFilter distanceMetadataNodeFilter = new NodeFilter() {
		public boolean hasSameProperty(Node nodeA, Node nodeB) {
			return nodeA.distance.equals("metadata") && nodeA.distance.equals(nodeB.distance) && !nodeA.equals(nodeB);
		}
	};
}
