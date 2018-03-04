def saddle_points(matrix):
    points = set()
    for row in range(len(matrix)):
        if len(matrix[row]) != len(matrix[0]):
            raise ValueError
        for element in range(len(matrix[row])):
            if matrix[row][element] == max(matrix[row]) and matrix[row][element] == min([row2[element] for row2 in matrix]):
                points.add((row, element))
    return points
