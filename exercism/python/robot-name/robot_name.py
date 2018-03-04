import random

class Robot(object):
    usednames = []

    def __init__(self):
        self.name = None
        while self.name in self.usednames or self.name == None:
            self.name = ""
            for i in range(2):
                self.name += chr(random.randint(65, 90))
            for i in range(3):
                self.name += str(random.randint(0, 9))
        self.usednames.append(self.name)

    def reset(self):
        self = self.__init__()
