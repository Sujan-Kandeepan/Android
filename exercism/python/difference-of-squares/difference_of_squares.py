def square_of_sum(n):
    sum = 0
    for i in range(n):
        sum += i+1
    return sum**2


def sum_of_squares(n):
    sum = 0
    for i in range(n):
        sum += (i+1)**2
    return sum


def difference(n):
    return square_of_sum(n) - sum_of_squares(n)
