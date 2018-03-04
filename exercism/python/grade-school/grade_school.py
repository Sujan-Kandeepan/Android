class School(object):
    def __init__(self, name):
        self.name = name
        self.dataset = []
        for i in range(8):
            self.dataset.append([])

    def add(self, name, grade):
        self.dataset[grade-1].append(name)

    def grade(self, grade):
        if self.dataset[grade-1] == []:
            return set()
        else: return tuple(self.dataset[grade-1])

    def sort(self):
        sortedlist = []
        for i in range(8):
            self.dataset[i].sort()
            if self.dataset[i] != []:
                sortedlist.append((i+1, tuple(self.dataset[i])))
        return sortedlist
