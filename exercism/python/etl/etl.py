def transform(legacy):
    new = {}
    for i in range(97, 123):
        for j in legacy:
            if chr(i).upper() in legacy[j]:
                new[chr(i)] = j
    return new
