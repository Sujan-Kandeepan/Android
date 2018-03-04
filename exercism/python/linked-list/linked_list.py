class Node(object):
    def __init__(self, element, previous, next):
        self.element = element
        self.previous = previous
        self.next = next


class LinkedList(object):
    def __init__(self):
        self.front = None
        self.back = None
        self.length = 0

    def push(self, element):
        if self.front == None:
            self.front = Node(element, None, None)
            self.back = Node(element, None, None)
        else:
            self.back.next = Node(element, self.back, None)
            self.back = self.back.next
            node = self.back
            self.front = None
            while node:
                self.front = Node(node.element, None, self.front)
                node = node.previous
        self.length += 1

    def pop(self):
        popped = self.back
        if self.back.previous == None:
            self.front = None
            self.back = None
        else:
            self.back = self.back.previous
            self.back.next = None
        self.length -= 1
        return popped.element

    def shift(self):
        shifted = self.front
        if self.front.next == None:
            self.front = None
            self.back = None
        else:
            self.front = self.front.next
            self.front.previous = None
        self.length -= 1
        return shifted.element

    def unshift(self, element):
        if self.back == None:
            self.front = Node(element, None, None)
            self.back = Node(element, None, None)
        else:
            self.front.previous = Node(element, None, self.front)
            self.front = self.front.previous
        node = self.front
        self.back = None
        while node:
            self.back = Node(node.element, self.back, None)
            node = node.next
        self.length += 1
