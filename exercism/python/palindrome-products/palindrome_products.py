def largest_palindrome(max_factor, min_factor=1):
    largest = 1
    factors = []
    for i in range(min_factor, max_factor+1):
        for j in range(i, max_factor+1):
            if i*j == int(str(i*j)[::-1]):
                if i*j > largest:
                    largest = i*j
                    factors = [{i, j}]
    print(factors)
    return (largest, factors[0])

def smallest_palindrome(max_factor, min_factor):
    smallest = max_factor**2
    factors = []
    for i in range(min_factor, max_factor+1):
        for j in range(i, max_factor+1):
            if i*j == int(str(i*j)[::-1]):
                if i*j < smallest:
                    smallest = i*j
                    factors = [{i, j}]
    print(factors)
    return (smallest, factors[0])
