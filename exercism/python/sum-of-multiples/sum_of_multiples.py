def sum_of_multiples(number, factors):
    multiples = set()
    for i in factors:
        for j in range(number):
            if j % i == 0:
                multiples.add(j)
    return sum(multiples)
