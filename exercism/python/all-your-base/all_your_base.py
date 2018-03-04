import math

def rebase(oldbase, digits, newbase):
    decimal = 0
    newdigits = []
    if oldbase < 2 or newbase < 2:
        raise ValueError
    for i in digits:
        if i < 0 or i >= oldbase:
            raise ValueError
    for i in range(len(digits)):
        decimal += digits[i] * oldbase**(len(digits)-i-1)
    if newbase < decimal:
        numdigits = math.ceil(math.log(decimal) / math.log(newbase))
    else:
        numdigits = len(digits)
    for i in range(numdigits):
        newdigit = min(decimal // newbase**(numdigits-i-1), newbase-1)
        if newdigit != 0 or len(newdigits) > 0:
            newdigits.append(newdigit)
            decimal -= newdigit * newbase**(numdigits-i-1)
    return newdigits
