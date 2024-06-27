from collections import defaultdict

class Graph:
    def __init__(self):
        self.neighbours = defaultdict(list)

    def add_child(self, parent, child, depth):
        self.neighbours[Vertex(parent, depth)].append(Edge(child))

class Vertex:
    def __init__(self, node, depth):
        self.node = node
        self.depth = depth

class Edge:
    def __init__(self, child_node):
        self.child_node = child_node
