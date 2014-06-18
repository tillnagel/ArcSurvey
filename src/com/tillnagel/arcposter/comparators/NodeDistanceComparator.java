package com.tillnagel.arcposter.comparators;

import java.util.Comparator;

import com.tillnagel.arcposter.Node;

public class NodeDistanceComparator implements Comparator<Node> {

	@Override
	public int compare(Node nodeA, Node nodeB) {
		return nodeA.getDistance().compareTo(nodeB.getDistance());
	}

}
