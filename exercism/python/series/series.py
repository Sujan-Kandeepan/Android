def slices(number, size):
    slicelist = []
    if size == 0 or size > len(number): raise ValueError
    for i in range(len(number)-size+1):
        temp = []
        for j in range(size):
            temp.append(int(number[i+j]))
        slicelist.append(temp)
    return slicelist
