def binary_search(list, key):
    p, r = 0, len(list)-1
    if list == []:
        raise ValueError
    while p <= r:
        q = (p+r)//2
        if key < list[0] or key > list[len(list)-1] or p == r and list[q] != key:
            raise ValueError
        print(p, q, r)
        if list[q] == key:
            return q
        elif list[q] > key:
            r = q-1
        else:
            p = q+1
