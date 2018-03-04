def count(lines):
    if lines == []:
        return 0
    rectangles = 0
    for x1 in range(len(lines[0])-1):
        for y1 in range(len(lines)-1):
            for x2 in range(x1+1, len(lines[0])):
                for y2 in range(y1+1, len(lines)):
                    isrectangle = True
                    for x in range(x1+1, x2):
                        if lines[y1][x] not in ['-', '+'] or lines[y2][x] not in ['-', '+']:
                            isrectangle = False
                    for y in range(y1+1, y2):
                        if lines[y][x1] not in ['|', '+'] or lines[y][x2] not in ['|', '+']:
                            isrectangle = False
                    if lines[y1][x1] != '+' or lines[y1][x2] != '+' or lines[y2][x1] != '+' or lines[y2][x2] != '+':
                        isrectangle = False
                    if isrectangle:
                        rectangles += 1
    return rectangles
