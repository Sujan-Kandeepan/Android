class TriangleError(Exception):
    pass


class Triangle(object):
    def __init__(self, side1, side2, side3):
        self.sides = [side1, side2, side3]
        self.sides.sort()
        self.side1 = self.sides[0]
        self.side2 = self.sides[1]
        self.side3 = self.sides[2]
        if self.side1 <= 0 or self.side2 <= 0 or self.side3 <= 0 or \
            self.side1 + self.side2 <= self.side3: raise TriangleError

    def kind(self):
        if self.side1 == self.side2 and self.side2 == self.side3:
            return "equilateral"
        elif self.side1 == self.side2 or self.side2 == self.side3:
            return "isosceles"
        else:
            return "scalene"
