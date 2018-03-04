def score(word):
    score = 0
    for i in word:
        if i.lower() in ['a', 'e', 'i', 'o', 'u', 'l', 'n', 'r', 's', 't']: score += 1
        if i.lower() in ['d', 'g']: score += 2
        if i.lower() in ['b', 'c', 'm', 'p']: score += 3
        if i.lower() in ['f', 'h', 'v', 'w', 'y']: score += 4
        if i.lower() in ['k']: score += 5
        if i.lower() in ['j', 'x']: score += 8
        if i.lower() in ['q', 'z']: score += 10
    return score
