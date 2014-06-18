package com.tillnagel.arcposter.comparators;

import java.util.Comparator;

import com.tillnagel.arcposter.Node;

public class NodeYearComparator implements Comparator<Node> {

	@Override
	public int compare(Node nodeA, Node nodeB) {
		return nodeA.getYear().compareTo(nodeB.getYear());
	}

}
