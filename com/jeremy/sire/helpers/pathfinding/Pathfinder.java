package com.jeremy.sire.helpers.pathfinding;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Pathfinder {

	private NodeMap map;

	private Node destinationNode;

	private final ArrayList<ScoredNode> openNodes;
	private final ArrayList<ScoredNode> closedNodes;

	private final LinkedList<Node> path;

	public Pathfinder(NodeMap map) {
		this.map = map;
		openNodes = new ArrayList<ScoredNode>(0);
		closedNodes = new ArrayList<ScoredNode>(0);
		path = new LinkedList<Node>();
	}

	public void updatePath(Node currentNode, Node destinationNode) {
		this.destinationNode = destinationNode;
		openNodes.clear();
		closedNodes.clear();
		path.clear();
		calculatePath(scoreNode(currentNode.getX(), currentNode.getY(), null));
	}

	public Node getNextStep() {
		return path.poll();
	}

	public LinkedList<Node> getPath() {
		return path;
	}

	public List<Node> getOpenNodes() {
		return openNodes.stream().map(node -> node.node).collect(Collectors.toList());
	}

	public List<Node> getClosedNodes() {
		return closedNodes.stream().map(node -> node.node).collect(Collectors.toList());
	}

	private void calculatePath(ScoredNode currentNode) {
		if (closedNodes.size() > 1024 || currentNode.node == destinationNode) {
			backtracePath(currentNode);
			return;
		}
		addOpenNodes(getAdjacentNodes(currentNode));
		closedNodes.add(currentNode);
		if (openNodes.isEmpty()) {
			backtracePath(currentNode);
			return;
		}
		calculatePath(openNodes.remove(0));
	}

	private HashSet<ScoredNode> getAdjacentNodes(ScoredNode currentNode) {
		HashSet<ScoredNode> result = new HashSet<Pathfinder.ScoredNode>();
		int x, y;
		x = currentNode.node.getX() - 1;
		y = currentNode.node.getY() + 0;
		if (map.hasNode(x, y)) result.add(scoreNode(x, y, currentNode));
		x = currentNode.node.getX() + 0;
		y = currentNode.node.getY() + 1;
		if (map.hasNode(x, y)) result.add(scoreNode(x, y, currentNode));
		x = currentNode.node.getX() + 1;
		y = currentNode.node.getY() + 0;
		if (map.hasNode(x, y)) result.add(scoreNode(x, y, currentNode));
		x = currentNode.node.getX() + 0;
		y = currentNode.node.getY() - 1;
		if (map.hasNode(x, y)) result.add(scoreNode(x, y, currentNode));
		result.removeIf(newScoredNode -> newScoredNode == null || !newScoredNode.node.isWalkable());
		result.removeIf(newScoredNode -> closedNodes.contains(newScoredNode) || openNodes.contains(newScoredNode));
		return result;
	}

	private void addOpenNodes(Collection<ScoredNode> newOpenNodes) {
//		newOpenNodes.forEach(newOpenNode -> {
//			int index = max(0, openNodes.size() - 1);
//			for (int i = 0, len = openNodes.size(); i < len; i++) {
//				if (newOpenNode.getTotalScore() < openNodes.get(i).getTotalScore()) {
//					index = i;
//					break;
//				}
//			}
//			openNodes.add(index, newOpenNode);
//		});
		openNodes.addAll(newOpenNodes);
		Collections.sort(openNodes);
	}

	private ScoredNode scoreNode(int x, int y, ScoredNode parent) {
		Node node = map.getNode(x, y);
		return new ScoredNode(node, parent == null ? 0 : parent.getWalkScore() + 1, manhattanDistance(node, destinationNode), parent);
	}

	private void backtracePath(ScoredNode currentNode) {
		ScoredNode parent = currentNode.getParent();
		if (parent == null) return;
		path.addFirst(parent.node);
		backtracePath(parent);
	}

	private static int manhattanDistance(Node from, Node to) {
		return abs(from.getX() - to.getX()) + abs(from.getY() - to.getY());
	}

	private static class ScoredNode implements Comparable<ScoredNode> {

		private Node node;
		private int walkScore, distanceScore;
		private ScoredNode parent;

		public ScoredNode(Node node, int walkScore, int distanceScore, ScoredNode parent) {
			this.node = node;
			this.walkScore = walkScore;
			this.distanceScore = distanceScore;
			this.parent = parent;
		}

		public ScoredNode getParent() {
			return parent;
		}

		private int getWalkScore() {
			return walkScore;
		}

		private int getDistanceScore() {
			return distanceScore;
		}

		private int getTotalScore() {
			return getWalkScore() + getDistanceScore();
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof ScoredNode)) {
				return false;
			}
			ScoredNode scoredNode = (ScoredNode) obj;
			return node.equals(scoredNode.node);
		}

		@Override
		public String toString() {
			return node.toString();
		}

		@Override
		public int compareTo(ScoredNode node) {
			return getTotalScore() - node.getTotalScore();
		}

	}

}
