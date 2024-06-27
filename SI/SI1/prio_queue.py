import itertools
import heapq

class PriorityQueue:
    def __init__(self):
        self.pq = []
        self.entry_finder = {}
        self.counter = itertools.count()

    def __len__(self):
        return len(self.pq)

    def add(self, priority, task):
        if task in self.entry_finder:
            self.update_priority(priority, task)
            return self
        count = next(self.counter)
        entry = [priority, count, task]
        self.entry_finder[task] = entry
        heapq.heappush(self.pq, entry)

    def update_priority(self, priority, task):
        entry = self.entry_finder[task]
        count = next(self.counter)
        entry[0], entry[1] = priority, count

    def pop(self):
        while self.pq:
            priority, _, task = heapq.heappop(self.pq)
            del self.entry_finder[task]
            return priority, task
        raise KeyError()
