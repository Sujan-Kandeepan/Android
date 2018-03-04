def divisor_generator(dividend, quotient):
    if dividend % quotient == 0:
        return dividend // quotient


def is_perfect(num):
    factors = []
    for i in range(1, int(num**0.5)+1):
        if divisor_generator(num, i) != None:
            factors.extend([i, divisor_generator(num, i)])
            print(i, divisor_generator(num, i))
    factors = list(set(factors))
    factors.sort()
    factors.remove(num)
    print(factors, sum(factors))
    return sum(factors) == num
