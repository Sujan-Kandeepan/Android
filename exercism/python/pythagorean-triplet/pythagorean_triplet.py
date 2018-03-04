def primitive_triplets(b):
    if b % 4 != 0: raise ValueError
    triplets = set()
    for n in range(1, b):
        for m in range(n+1, b):
            if m*n*2 == b:
                a = m**2 - n**2
                c = m**2 + n**2
                primitive = True
                for i in range(2, min(a,b)):
                    if a % i == 0 and b % i == 0 and c % i == 0:
                        primitive = False
                if primitive:
                    triplets.add(tuple(sorted([a,b,c])))
    return triplets

def triplets_in_range(min, max):
    triplets = set()
    for i in range(min, max+1):
        for j in range(i, max+1):
            for k in range(j, max+1):
                if i**2 + j**2 == k**2:
                    triplets.add((i, j, k))
    return triplets

def is_triplet(given):
    ordered = list(given)
    ordered.sort()
    return ordered[0]**2 + ordered[1]**2 == ordered[2]**2
