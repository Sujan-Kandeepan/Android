def nth_prime(n):
    if n < 1:
        raise ValueError
    primes = []
    num = 2
    while len(primes) < n:
        prime = True
        for i in primes:
            if num % i == 0:
                prime = False
        if prime:
            primes.append(num)
        num += 1
    return primes[len(primes)-1]
