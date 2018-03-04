def largest_product(number, size):
    numstring = str(number)
    if size > len(numstring) or size < 0:
        raise ValueError
    for i in numstring:
        if i not in ['0','1','2','3','4','5','6','7','8','9']:
            raise ValueError
    largestproduct = 0
    for i in range(len(numstring) - size + 1):
        product = 1
        for j in numstring[i:i+size]:
            product *= eval(j)
        if product > largestproduct:
            largestproduct = product
    return largestproduct
