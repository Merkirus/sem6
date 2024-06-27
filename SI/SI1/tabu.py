from prio_queue import PriorityQueue # pyright: ignore
from graph import Graph # pyright: ignore
from helpers import add_time, duration, get_map_place_to_geo, format_route, format_output # pyright: ignore
from collections import deque
import sys
import random
import time as tt

def astar(start_a, end_a, opt_a, time_a, g):

    output1 = f"{start_a}"
    output2 = f"{end_a}"
    assert start_a is not None, output1
    assert end_a is not None, output2

    geo_map = get_map_place_to_geo()

    def manhattan_distance(vertex1, vertex2):
        v1_lat, v1_lon = geo_map[vertex1.value.strip('"')]
        v2_lat, v2_lon = geo_map[vertex2.value.strip('"')]

        return abs(v1_lat - v2_lat) + abs(v1_lon - v2_lon)

    def inner_astar(start, end, opt, time):
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

        return format_route(previous, start, end), distances, info

    return inner_astar(start_a, end_a, opt_a, time_a)



if __name__ == '__main__':

    g = Graph()

    startVertex = None
    stopsVertex = []

    for k in g.neighbours.keys():
        if '"Piastowska"' == k.value:
            startVertex = k
        if '"PL. JANA PAWŁA II"' == k.value:
            stopsVertex.append(k)
        if '"PL. GRUNWALDZKI"' == k.value:
            stopsVertex.append(k)
        if '"Rondo"' == k.value:
            stopsVertex.append(k)
        if '"Ogród Botaniczny"' == k.value:
            stopsVertex.append(k)


    def get_solution_cost(solution, opt, time, g):
        total_cost = 0
        total_info = []
        total_route = []

        current_time = time

        for i in range(len(solution)-1):
            if solution[i] == solution[i+1]:
                continue
            route, distances, info = astar(solution[i], solution[i+1], opt, current_time, g)
            cost = distances[route[-1]]
            current_time = add_time(current_time, cost)
            local_info = info
            total_cost += cost
            total_info.append(local_info)
            total_route.append(route)

        return total_cost, total_info, total_route

    def get_neighbours(solution):
        start = solution[0]
        end = solution[-1]
        solution = solution[1:-1]
        l = len(solution)

        result = []
        for i in range(l - 1):
            for j in range(i+1, l):
                neighbour = solution
                neighbour[i], neighbour[j] = neighbour[j], neighbour[i]
                neighbour.insert(0, start)
                neighbour.append(end)
                result.append(neighbour)

        return result

    def get_init_solution(start, stops):

        random.shuffle(stops)

        stops.insert(0, start)
        stops.append(start)

        return stops


    def tabu_search(start, stops, opt, time, tabuList_size):
        start_time = tt.time()

        tabuList = deque()
        solution = get_init_solution(start, stops)
        solution_cost, solution_info, solution_route = get_solution_cost(solution, opt, time, g)

        tabuList.append(solution)

        for _ in range(1000):
            neighbours = get_neighbours(solution)

            best_neighbour_cost = float("inf")
            best_neighbour = []
            best_neighbour_info = []
            best_neighbour_route = []

            for neighbour in neighbours:
                if neighbour in tabuList:
                    continue

                neighbour_cost, neighbour_info, neighbour_route = get_solution_cost(neighbour, opt, time, g)
                tabuList.append(neighbour)

                if neighbour_cost < best_neighbour_cost:
                    best_neighbour = neighbour
                    best_neighbour_cost = neighbour_cost
                    best_neighbour_info = neighbour_info
                    best_neighbour_route = neighbour_route

            if best_neighbour_cost < solution_cost:
                solution = best_neighbour
                solution_cost = best_neighbour_cost
                solution_info = best_neighbour_info
                solution_route = best_neighbour_route

            if len(tabuList) > tabuList_size:
                tabuList.popleft()


        end_time = tt.time()

        sys.stderr.write(f"Function value of found solution: {solution_cost}\n")
        sys.stderr.write(f"Time to find solution: {end_time - start_time}\n")

        result = ""

        for (sr, si) in zip(solution_route, solution_info):
            result += format_output(sr, si)
        
        return result


    print(tabu_search(startVertex, stopsVertex, 't', "12:00:00", 10000))

