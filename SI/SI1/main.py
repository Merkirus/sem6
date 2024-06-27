from prio_queue import PriorityQueue # pyright: ignore
from graph import Graph # pyright: ignore
from helpers import add_time, duration # pyright: ignore

def dijkstra_route(route, start_stop, end_stop):
    result = [end_stop]
    previousStop = end_stop

    i = 0 # limiter

    while route[previousStop] != start_stop and i < 100:
        result.append(route[previousStop])
        previousStop = route[previousStop]
        i += 1

    result.append(start_stop)

    result = result[::-1]

    return result

def dijkstra_output(route, distances):
    result = []

    for stop in route:
        connection_info = distances[stop]
        # total_time = connection_info[0]
        current_time = connection_info[1]
        wait_time = connection_info[2]
        route_distance = connection_info[3]
        
        stop_output = f"Stop: {stop.value}, arrival: {current_time}, waited for commute: {wait_time},  commute took: {route_distance}"
        result.append(stop_output)

    return " -> ".join(result)

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
        previous = { v: None for v in g.neighbours.keys() }
        visited = { v: False for v in g.neighbours.keys() }
        distances = { v: (float("inf"), "", float("inf"), float("inf")) for v in g.neighbours.keys() } # distance from start, current_time on stop, wait time, distance between stops
        distances[start] = (0, time, 0, 0)
        queue = PriorityQueue() # pyright: ignore
        queue.add(0, start)
        while queue:
            removed_distance, removed = queue.pop()
            visited[removed] = True
            for edge in g.neighbours[removed]:
                if visited[edge.vertex]:
                    continue
                current_time = add_time(time, removed_distance)
                wait_time = duration(current_time, edge.start)
                new_distance = removed_distance + edge.distance + wait_time
                if new_distance < distances[edge.vertex][0]:
                    distances[edge.vertex] = (new_distance, add_time(time, new_distance), wait_time, edge.distance)
                    previous[edge.vertex] = removed
                    queue.add(new_distance, edge.vertex)
        
        route = dijkstra_route(previous, start, end)

        return dijkstra_output(route, distances)


    print(dijkstra(startVertex, endVertex, 0, "12:00:00"))

