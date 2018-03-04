NORTH = (0, 1)
EAST = (1, 0)
SOUTH = (0, -1)
WEST = (-1, 0)

class Robot(object):
    def __init__(self, bearing = NORTH, x = 0, y = 0):
        self.coordinates = (x, y)
        self.bearing = bearing

    def turn_right(self):
        if self.bearing == NORTH: self.bearing = EAST
        elif self.bearing == EAST: self.bearing = SOUTH
        elif self.bearing == SOUTH: self.bearing = WEST
        elif self.bearing == WEST: self.bearing = NORTH

    def turn_left(self):
        if self.bearing == NORTH: self.bearing = WEST
        elif self.bearing == WEST: self.bearing = SOUTH
        elif self.bearing == SOUTH: self.bearing = EAST
        elif self.bearing == EAST: self.bearing = NORTH

    def advance(self):
        if self.bearing == NORTH: self.coordinates = (self.coordinates[0], self.coordinates[1] + 1)
        elif self.bearing == EAST: self.coordinates = (self.coordinates[0] + 1, self.coordinates[1])
        elif self.bearing == SOUTH: self.coordinates = (self.coordinates[0], self.coordinates[1] - 1)
        elif self.bearing == WEST: self.coordinates = (self.coordinates[0] - 1, self.coordinates[1])

    def simulate(self, instructions):
        for i in instructions:
            if i == "A": self.advance()
            elif i == "L": self.turn_left()
            elif i == "R": self.turn_right()
