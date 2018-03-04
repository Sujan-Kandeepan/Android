def calculate_total(books):
    def groupnums(num):
        if num == 1:
            return [[0], [1], [2]]
        else:
            returnlist = []
            for i in groupnums(num-1):
                returnlist.extend([i+[0], i+[1], i+[2]])
            return returnlist

    bestprice = len(books)*8
    if books == []:
        return 0.0
    for i in groupnums(len(books)):
        groups = [[], [], []]
        for j in range(len(books)):
            groups[i[j]].append(books[j])
        if groups[0] == sorted(groups[0]) and groups[0] == list(set(groups[0])) and \
            groups[1] == sorted(groups[1]) and groups[1] == list(set(groups[1])) and \
            groups[2] == sorted(groups[2]) and groups[2] == list(set(groups[2])):
            price = 0.0
            for j in groups:
                if len(j) == 5:
                    price += 30.0
                elif len(j) == 4:
                    price += 25.6
                elif len(j) == 3:
                    price += 21.6
                elif len(j) == 2:
                    price += 15.2
                elif len(j) == 1:
                    price += 8.0
            if price < bestprice:
                bestprice = price
    return bestprice
