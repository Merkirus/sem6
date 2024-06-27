from prio_queue import PriorityQueue # pyright: ignore
from graph import Graph # pyright: ignore
from helpers import add_time, duration, get_map_place_to_geo, format_route, format_output # pyright: ignore
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

    geo_map = get_map_place_to_geo()

    def manhattan_distance(vertex1, vertex2):
        v1_lat, v1_lon = geo_map[vertex1.value.strip('"')]
        v2_lat, v2_lon = geo_map[vertex2.value.strip('"')]

        return abs(v1_lat - v2_lat) + abs(v1_lon - v2_lon)

    def astar(start, end, opt, time):
        start_time = tt.time()

        line_opt = True if opt == 'p' else False
        previous = { v: None for v in g.neighbours.keys() }
        visited = { v: False for v in g.neighbours.keys() }
        # distance from start, current_time on stop, wait time, distance between stops
        info = { v: (float("inf"), "", float("inf"), float("inf"), "") for v in g.neighbours.keys() }
        distances = { v: float("inf") for v in g.neighbours.keys() }
        distances[start] = 0
        info[start] = (0, time, 0, 0, "")
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
                heuristic = manhattan_distance(edge.vertex, end)
                new_distance = removed_distance + edge.distance + wait_time + heuristic
                time_tracker = removed_distance + edge.distance + wait_time 
                if line_opt:
                    last_transport = info[removed][4]
                    # if is not the same or we just started (start has no transport)
                    if edge.transport != last_transport and last_transport != "":
                        new_distance += 100
                if new_distance < distances[edge.vertex]:
                    info[edge.vertex] = (time_tracker, add_time(time, new_distance), wait_time, edge.distance, edge.transport)
                    distances[edge.vertex] = new_distance
                    previous[edge.vertex] = removed
                    queue.add(new_distance, edge.vertex)

        end_time = tt.time()

        sys.stderr.write(f"Function value of found solution: {distances[end]}\n")
        sys.stderr.write(f"Time to find solution: {end_time - start_time}\n")
        
        route = format_route(previous, start, end)

        return format_output(route, info)


    print(astar(startVertex, endVertex, 't', "12:00:00"))

