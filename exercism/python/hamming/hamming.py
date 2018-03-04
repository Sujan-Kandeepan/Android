def distance(first, second):
    distance = 0
    if len(first) != len(second): raise ValueError
    for i in range(len(first)):
        if first[i] != second[i]:
            distance += 1
    return distance
