def prime_factors(num):
    primes = []
    factor = 2
    while factor <= num:
        if num % factor == 0 and num == factor:
            primes.append(factor)
            factor += 1
        elif num % factor == 0 and num != factor:
            primes.append(factor)
            num = num // factor
        else:
            factor += 1
    return primes
