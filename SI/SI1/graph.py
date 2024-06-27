from collections import defaultdict
import json
from helpers import dict_to_unique_str, duration # pyright: ignore

class Graph:
    def __init__(self):
        self.neighbours = defaultdict(list)

        self.run()
        
    def run(self):
        connections = None

        with open('connections.json', 'r', encoding='utf-8') as f:
            connections = json.load(f)

        for x in connections:
            v1 = Vertex(dict_to_unique_str(x["vertex1"]["stop"]))
            v2 = Vertex(dict_to_unique_str(x["vertex2"]["stop"]))
            
            time_start = x["edge"]["connectionTime"]["start"]
            time_end = x["edge"]["connectionTime"]["end"]
            edge_duration = duration(x["edge"]["connectionTime"]["start"], x["edge"]["connectionTime"]["end"])
            transport = x["edge"]["transport"]

            self.neighbours[v1].append(Edge(time_start, time_end, edge_duration, transport, v2))
            if v2 not in self.neighbours.keys():
                self.neighbours[v2] = []

class Vertex:
    def __init__(self, value):
        self.value = value

    def __hash__(self):
        return hash(self.value)

    def __eq__(self, other):
        return isinstance(other, Vertex) and self.value == other.value

    def __str__(self):
        return f"Vertex: value = {self.value}"

class Edge:
    def __init__(self, start, end, distance, transport, vertex):
        self.distance = distance
        self.start = start
        self.end = end
        self.transport = transport
        self.vertex = vertex

    def __str__(self):
        return f"Edge: start = {self.start}, end = {self.end}, duration = {self.distance}, vertex: {self.vertex}"

