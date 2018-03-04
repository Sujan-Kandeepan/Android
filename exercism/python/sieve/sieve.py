def sieve(limit):
    primes = list(range(2, limit + 1)) if limit > 1 else []
    for i in list(range(2, limit + 1)):
        for j in primes:
            if j % i == 0 and i != j:
                primes.remove(j)
    return primes
