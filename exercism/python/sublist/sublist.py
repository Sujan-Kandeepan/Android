SUBLIST, SUPERLIST, EQUAL, UNEQUAL = "sublist", "superlist", "equal", "unequal"

def check_lists(first, second):
    firstInSecond, secondInFirst = False, False
    for i in range(len(second)-len(first)+1):
        if second[i:i+len(first)] == first:
            firstInSecond = True
    for i in range(len(first)-len(second)+1):
        if first[i:i+len(second)] == second:
            secondInFirst = True
    if firstInSecond and secondInFirst:
        return EQUAL
    elif firstInSecond:
        return SUBLIST
    elif secondInFirst:
        return SUPERLIST
    else:
        return UNEQUAL
