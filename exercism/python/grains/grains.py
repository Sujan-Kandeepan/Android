def on_square(square):
    if square not in range(1, 65):
        raise ValueError
    return 2**(square-1)


def total_after(square):
    if square not in range(1, 65):
        raise ValueError
    sum = 0
    for i in range(square):
        sum += 2**i
    return sum
