import json
from pandas import read_csv

d = read_csv('connection_graph.csv', index_col=0)

df = d.values

class Entry:
    def __init__(self, c, l, dt, at, ss, es, sslat, sslon, eslat, eslon) -> None:
        self.company = c
        self.line = l
        self.departue_time = dt
        self.arrival_time = at
        self.start_stop = ss
        self.end_stop = es
        self.ss_lat = sslat
        self.ss_lon = sslon
        self.es_lat = eslat
        self.es_lon = eslon

    def __str__(self) -> str:
        attributes = ", ".join([f"{key}={value}" for key, value in self.__dict__.items()])
        return attributes

# Edge
class ConnectionTime:
    def __init__(self, start, stop):
        self.start = start
        self.stop = stop
        self.alt_start = self.hour_format(self.start)
        self.alt_stop = self.hour_format(self.stop)


    def duration(self):
        s_h, s_m, s_s = [ int(x) for x in self.alt_start.split(":") ]
        e_h, e_m, e_s = [ int(x) for x in self.alt_stop.split(":") ]

        e_m += 60 * (e_h - s_h)

        e_s += 60 * (e_m - s_m)

        return e_s - s_s

    def hour_format(self, time):
        time_arr = time.split(":")
        hour = time_arr[0]

        if int(hour) > 23:
            time_arr[0] = f"{int(hour) - 24:02}"

        return ":".join(time_arr)

    def __eq__(self, other):
        if isinstance(other, ConnectionTime):
            return self.start == other.start and self.stop == other.stop
        return False

# MarkedEdge
class Commute:
    def __init__(self, transport, time) -> None:
        self.transport = transport
        self.time = time

    def __eq__(self, other):
        if isinstance(other, Commute):
            return self.transport == other.transport and self.time == other.time
        return False

# Vertex
class Stop:
    def __init__(self, stop, lat, lon) -> None:
        self.stop = stop
        self.lat = lat
        self.lon = lon

    def __eq__(self, other):
        if isinstance(other, Stop):
            return self.stop == other.stop and self.lat == other.lat and self.lon == other.lon
        return False

# Connection
class Trip:
    def __init__(self, start, stop, commute) -> None:
        self.start = start
        self.stop = stop
        self.commute = commute

    def __eq__(self, other):
        if isinstance(other, Trip):
            return self.start == other.start and self.stop == other.stop and self.commute == other.commute
        return False

# Helper
class Transport:
    def __init__(self, company, line) -> None:
        self.company = company
        self.line = line

    def __eq__(self, other):
        if isinstance(other, Transport):
            return self.company == other.company and self.line == other.line
        return False

entries = []

for i in df:
    entries.append(Entry(*i))

transports = set()
connectionsTimes = set()
commutes = set()
stops = set()
connections = set()

for i in entries:
    # h = Transport(i.company, i.line)
    h = {
        "company": i.company,
        "line": i.line
    }
    transports.add(json.dumps(h, ensure_ascii=False, indent=None, sort_keys=True))
    # e = ConnectionTime(i.departue_time, i.arrival_time, ensure_ascii=False)
    e = {
        "start": i.departue_time,
        "end": i.arrival_time
    }
    connectionsTimes.add(json.dumps(e, ensure_ascii=False, indent=None, sort_keys=True))
    # me = Commute(h, e, ensure_ascii=False)
    me = {
        "transport": h,
        "connectionTime": e
    }
    commutes.add(json.dumps(me, ensure_ascii=False, indent=None, sort_keys=True))
    # v1 = Stop(i.start_stop, i.ss_lat, i.ss_lon, ensure_ascii=False)
    v1 = {
        "stop": i.start_stop,
        "lat": i.ss_lat,
        "lon": i.ss_lon
    }
    stops.add(json.dumps(v1, ensure_ascii=False, indent=None, sort_keys=True))
    # v2 = Stop(i.end_stop, i.es_lat, i.es_lon)
    v2 = {
        "stop": i.end_stop,
        "lat": i.es_lat,
        "lon": i.ss_lon
    }
    stops.add(json.dumps(v2, ensure_ascii=False, indent=None, sort_keys=True))
    # con = Trip(v1, v2, me, ensure_ascii=False)
    con = {
        "vertex1": v1,
        "vertex2": v2,
        "edge": me
    }
    connections.add(json.dumps(con, ensure_ascii=False, indent=None, sort_keys=True))

transports = list(transports)
connectionsTimes = list(connectionsTimes)
commutes = list(commutes)
stops = list(stops)
connections = list(connections)

transports = [json.loads(x) for x in transports]
connectionsTimes = [json.loads(x) for x in connectionsTimes]
commutes = [json.loads(x) for x in commutes]
stops = [json.loads(x) for x in stops]
connections = [json.loads(x) for x in connections]

with open('transports.json', 'w+', encoding='utf-8') as jf:
    json.dump(transports, jf, ensure_ascii=False, indent=4)

with open('connectionsTimes.json', 'w+', encoding='utf-8') as jf:
    json.dump(connectionsTimes, jf, ensure_ascii=False, indent=4)

with open('commutes.json', 'w+', encoding='utf-8') as jf:
    json.dump(commutes, jf, ensure_ascii=False, indent=4)

with open('stops.json', 'w+', encoding='utf-8') as jf:
    json.dump(stops, jf, ensure_ascii=False, indent=4)

with open('connections.json', 'w+', encoding='utf-8') as jf:
    json.dump(connections, jf, ensure_ascii=False, indent=4)
