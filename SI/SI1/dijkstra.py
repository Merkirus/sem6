from prio_queue import PriorityQueue # pyright: ignore
from graph import Graph # pyright: ignore
from helpers import add_time, duration, format_route, format_output # pyright: ignore
import sys
import time as tt

if __name__ == '__main__':

    g = Graph()

    startVertex = None
    endVertex = None

    for k in g.neighbours.keys():
        if '"Piastowska"' == k.value:
            startVertex = k
        if '"FAT"' == k.value:
            endVertex = k


    def dijkstra(start, end, _, time):
        start_time = tt.time()

        previous = { v: None for v in g.neighbours.keys() }
        visited = { v: False for v in g.neighbours.keys() }
        # distance from start, current_time on stop, wait time, distance between stops
        distances = { v: (float("inf"), "", float("inf"), float("inf"), "") for v in g.neighbours.keys() }
        distances[start] = (0, time, 0, 0, "")
        queue = PriorityQueue() # pyright: ignore
        queue.add(0, start)
        while queue:
            removed_distance, removed = queue.pop()
            if removed == end:
                break
            visited[removed] = True
            for edge in g.neighbours[removed]:
                if visited[edge.vertex]:
                    continue
                current_time = add_time(time, removed_distance)
                wait_time = duration(current_time, edge.start)
                new_distance = removed_distance + edge.distance + wait_time
                if new_distance < distances[edge.vertex][0]:
                    distances[edge.vertex] = (new_distance, add_time(time, new_distance), wait_time, edge.distance, edge.transport)
                    previous[edge.vertex] = removed
                    queue.add(new_distance, edge.vertex)

        end_time = tt.time()

        sys.stderr.write(f"Function value of found solution: {distances[end][0]}\n")
        sys.stderr.write(f"Time to find solution: {end_time - start_time}\n")
        
        route = format_route(previous, start, end)

        return format_output(route, distances)


    print(dijkstra(startVertex, endVertex, 0, "12:00:00"))

