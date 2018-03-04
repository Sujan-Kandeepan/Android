class Point(object):
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return 'Point({}:{})'.format(self.x, self.y)

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def __sub__(self, other):
        return Point(self.x - other.x, self.y - other.y)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __ne__(self, other):
        return not(self == other)


class WordSearch(object):
    def __init__(self, puzzle):
        self.puzzle = puzzle.split('\n')

    def search(self, word):
        for y in range(len(self.puzzle)):
            for x in range(len(self.puzzle[0])-len(word)+1):
                found = True
                for i in range(len(word)):
                    if self.puzzle[y][x+i] != word[i]:
                        found = False
                if found:
                    return (Point(x, y), Point(x+i, y))
                found = True
                for i in range(len(word)):
                    if self.puzzle[y][x+i] != word[len(word)-i-1]:
                        found = False
                if found:
                    return (Point(x+i, y), Point(x, y))
        for x in range(len(self.puzzle[0])):
            for y in range(len(self.puzzle)-len(word)+1):
                found = True
                for i in range(len(word)):
                    if self.puzzle[y+i][x] != word[i]:
                        found = False
                if found:
                    return (Point(x, y), Point(x, y+i))
                found = True
                for i in range(len(word)):
                    if self.puzzle[y+i][x] != word[len(word)-i-1]:
                        found = False
                if found:
                    return (Point(x, y+i), Point(x, y))
        for x in range(len(self.puzzle[0])-len(word)+1):
            for y in range(len(self.puzzle)-len(word)+1):
                found = True
                for i in range(len(word)):
                    if self.puzzle[y+i][x+i] != word[i]:
                        found = False
                if found:
                    return (Point(x, y), Point(x+i, y+i))
                found = True
                for i in range(len(word)):
                    if self.puzzle[y+i][x+i] != word[len(word)-i-1]:
                        found = False
                if found:
                    return (Point(x+i, y+i), Point(x, y))
                found = True
                for i in range(len(word)):
                    if self.puzzle[y+len(word)-i-1][x+i] != word[i]:
                        found = False
                if found:
                    return (Point(x, y+i), Point(x+i, y))
                found = True
                for i in range(len(word)):
                    if self.puzzle[y+len(word)-i-1][x+i] != word[len(word)-i-1]:
                        found = False
                if found:
                    return (Point(x+i, y), Point(x, y+i))
        return None
