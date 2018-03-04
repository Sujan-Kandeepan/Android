def map_clone(function, list):
    for i in range(len(list)):
        list[i] = function(list[i])
    return list


def length(list):
    length = 0
    for i in list:
        length += 1
    return length


def filter_clone(condition, list):
    newlist = []
    for i in list:
        if condition(i):
            newlist.append(i)
    return newlist


def reverse(list):
    newlist = []
    for i in list:
        newlist = [i] + newlist
    return newlist


def append(list, newentry):
    return list + [newentry]


def foldl(function, list, item):
    for i in range(len(list)):
        item = function(item, list[i])
    return item


def foldr(function, list, item):
    for i in range(len(list)-1, -1, -1):
        item = function(list[i], item)
    return item


def flat(nested):
    newlist = []
    for i in nested:
        if type(i) == list:
            newlist.extend(flat(i))
        else:
            newlist.append(i)
    return newlist


def concat(list1, list2):
    if list1 == None:
        list1 = []
    if list2 == None:
        list2 = []
    return list1 + list2
