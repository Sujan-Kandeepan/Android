class BufferFullException(Exception):
    pass


class BufferEmptyException(Exception):
    pass


class CircularBuffer(object):
    def __init__(self, size):
        self.size = size
        self.rpos = 0
        self.wpos = 0
        self.elements = []
        for i in range(size):
            self.elements.append(None)

    def read(self):
        if self.elements[self.rpos] == None:
            raise BufferEmptyException
        value = self.elements[self.rpos]
        self.elements[self.rpos] = None
        self.rpos = (self.rpos + 1) % self.size
        return value

    def write(self, element):
        if self.elements[self.wpos] != None:
            raise BufferFullException
        self.elements[self.wpos] = element
        self.wpos = (self.wpos + 1) % self.size

    def overwrite(self, element):
        self.elements[self.wpos] = element
        self.rpos = (self.rpos + 1) % self.size
        self.wpos = (self.wpos + 1) % self.size

    def clear(self):
        for i in range(len(self.elements)):
            self.elements[i] = None
