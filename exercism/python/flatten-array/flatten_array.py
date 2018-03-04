def flatten(nested):
    newlist = []
    for i in nested:
        if type(i) == list:
            newlist.extend(flatten(i))
        elif i != None and i != ():
            newlist.append(i)
    return newlist
