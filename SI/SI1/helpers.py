import json
import statistics
from collections import defaultdict
from datetime import datetime, timedelta

def is_future(time1, time2):
    dt1 = datetime.strptime(time1, "%H:%M:%S")
    dt2 = datetime.strptime(time2, "%H:%M:%S")

    if dt1.time() > dt2.time():
        return False

    return True

def hour_format(time):
    time_arr = time.split(":")
    hour = time_arr[0]

    if int(hour) > 23:
        time_arr[0] = f"{int(hour) - 24:02}"

    return ":".join(time_arr)

def duration(start, stop):
    dt1 = datetime.strptime(hour_format(start), "%H:%M:%S")
    dt2 = datetime.strptime(hour_format(stop), "%H:%M:%S")

    diff = dt2 - dt1

    return diff.seconds

def add_time(time, seconds):
    dt1 = datetime.strptime(hour_format(time), "%H:%M:%S")

    new_dt = dt1 + timedelta(seconds=seconds)

    return new_dt.strftime("%H:%M:%S")
    

def dict_to_unique_str(dictionary):
    return json.dumps(dictionary, ensure_ascii=False, indent=None, sort_keys=True)

def get_map_place_to_geo():
    connections = None
    
    with open('connections.json', 'r', encoding='utf-8') as f:
        connections = json.load(f)
        
    values_lat = defaultdict(list)
    values_lon = defaultdict(list)

    values = {}

    for x in connections:
        v1 = x["vertex1"]
        v1_stop = v1["stop"]
        v1_lat = v1["lat"]
        v1_lon = v1["lon"]
        v2 = x["vertex2"]
        v2_stop = v2["stop"]
        v2_lat = v2["lat"]
        v2_lon = v2["lon"]

        values_lat[v1_stop].append(v1_lat)
        values_lat[v2_stop].append(v2_lat)
        values_lon[v1_stop].append(v1_lon)
        values_lon[v2_stop].append(v2_lon)
    
    for k in values_lat.keys():
        values[k] = (statistics.mean(values_lat[k]), statistics.mean(values_lon[k]))

    return values

def format_route(route, start_stop, end_stop):
    result = [end_stop]
    previousStop = end_stop

    i = 0 # limiter

    while route[previousStop] != start_stop and i < 100:
        result.append(route[previousStop])
        previousStop = route[previousStop]
        if previousStop is None:
            break
        i += 1

    result.append(start_stop)

    result = result[::-1]

    return result

def format_output(route, distances):
    result = []

    for stop in route:
        connection_info = distances[stop]
        # total_time = connection_info[0]
        current_time = connection_info[1]
        wait_time = connection_info[2]
        route_distance = connection_info[3]
        transport = connection_info[4]
        
        stop_output = f"Stop: {stop.value}, arrival: {current_time}, waited for commute: {wait_time},  commute took: {route_distance}, transport: {transport}"
        result.append(stop_output)

    return " -> ".join(result)
