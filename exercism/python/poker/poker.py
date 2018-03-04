def poker(hands):
    #setup, obtaining kinds and suits for each card
    besthands = []
    kinds = []
    suits = []
    returnlist = []
    for hand in hands:
        temp = [hand[0][0], hand[1][0], hand[2][0], hand[3][0], hand[4][0]]
        for i in range(5):
            if temp[i] == 'T':
                temp[i] = 10
            elif temp[i] == 'J':
                temp[i] = 11
            elif temp[i] == 'Q':
                temp[i] = 12
            elif temp[i] == 'K':
                temp[i] = 13
            elif temp[i] == 'A':
                temp[i] = 14
            else:
                temp[i] = int(temp[i])
        kinds.append(sorted(temp))
        suits.append(sorted([hand[0][1], hand[1][1], hand[2][1], hand[3][1], hand[4][1]]))

    #dealing with straight flushes
    maxtopcard = 0
    for i in range(len(hands)):
        if kinds[i][0] < kinds[i][1] < kinds[i][2] < kinds[i][3] < kinds[i][4] and \
            kinds[i][4] - kinds[i][0] == 4 and \
            suits[i][0] == suits[i][1] == suits[i][2] == suits[i][3] == suits[i][4]:
            besthands.append(i)
            if kinds[i][4] > maxtopcard:
                maxtopcard = kinds[i][4]
    for i in besthands:
        if kinds[i][4] != maxtopcard:
            besthands.remove(i)
    for i in besthands:
        returnlist.append(hands[i])
    if returnlist != []:
        return returnlist

    #dealing with four of a kind
    maxsquarecard = 0
    for i in range(len(hands)):
        if kinds[i][0] == kinds[i][1] == kinds[i][2] == kinds[i][3] or kinds[i][1] == kinds[i][2] == kinds[i][3] == kinds[i][4]:
            besthands.append(i)
            if kinds[i][2] > maxsquarecard:
                maxsquarecard = kinds[i][2]
    for i in besthands:
        if kinds[i][2] != maxsquarecard:
            besthands.remove(i)
    for i in besthands:
        returnlist.append(hands[i])
    if returnlist != []:
        return returnlist

    #dealing with full houses
    maxpair = (0, 0)
    for i in range(len(hands)):
        if kinds[i][0] == kinds[i][1] == kinds[i][2] and kinds[i][3] == kinds[i][4]:
            besthands.append(i)
            if kinds[i][0] > maxpair[0] or (kinds[i][0] == maxpair[0] and kinds[i][2] > maxpair[1]):
                maxpair = (kinds[i][0], kinds[i][2])
        elif kinds[i][0] == kinds[i][1] and kinds[i][2] == kinds[i][3] == kinds[i][4]:
            besthands.append(i)
            if kinds[i][2] > maxpair[0] or (kinds[i][2] == maxpair[0] and kinds[i][0] > maxpair[1]):
                maxpair = (kinds[i][2], kinds[i][0])
    for i in besthands:
        if not (maxpair[0] in kinds[i] and maxpair[1] in kinds[i]):
            besthands.remove(i)
    for i in besthands:
        returnlist.append(hands[i])
    if returnlist != []:
        return returnlist

    #dealing with flushes
    maxflush = [0, 0, 0, 0, 0]
    for i in range(len(hands)):
        if suits[i][0] == suits[i][1] == suits[i][2] == suits[i][3] == suits[i][4]:
            besthands.append(i)
            if kinds[i][4] > maxflush[4] or kinds[i][4] == maxflush[4] and kinds[i][3] > maxflush[3] or \
                kinds[i][3:] == maxflush[3:] and kinds[i][2] > maxflush[2] or \
                kinds[i][2:] == maxflush[2:] and kinds[i][1] > maxflush[1] or \
                kinds[i][1:] == maxflush[1:] and kinds[i][0] > maxflush[0]:
                maxflush = kinds[i]
    for i in besthands:
        if kinds[i] != maxflush:
            besthands.remove(i)
    for i in besthands:
        returnlist.append(hands[i])
    if returnlist != []:
        return returnlist

    #dealing with straights
    maxtopcard = 0
    for i in range(len(hands)):
        if kinds[i][0] < kinds[i][1] < kinds[i][2] < kinds[i][3] < kinds[i][4] and kinds[i][4] - kinds[i][0] == 4:
            besthands.append(i)
            if kinds[i][4] > maxtopcard:
                maxtopcard = kinds[i][4]
    for i in besthands:
        if kinds[i][4] != maxtopcard:
            besthands.remove(i)
    for i in besthands:
        returnlist.append(hands[i])
    if returnlist != []:
        return returnlist

    #dealing with triples
    maxtriple = 0
    for i in range(len(hands)):
        if kinds[i][0] == kinds[i][1] == kinds[i][2] or kinds[i][1] == kinds[i][2] == kinds[i][3] or \
            kinds[i][2] == kinds[i][3] == kinds[i][4]:
            besthands.append(i)
            if kinds[i][2] > maxtriple:
                maxtriple = kinds[i][2]
    for i in besthands:
        if kinds[i][2] != maxtriple:
            besthands.remove(i)
    for i in besthands:
        returnlist.append(hands[i])
    if returnlist != []:
        return returnlist

    #dealing with two pairs
    maxpairs = (0, 0, 0)
    for i in range(len(hands)):
        if kinds[i][0] == kinds[i][1] and kinds[i][2] == kinds[i][3]:
            pairs = tuple(sorted([kinds[i][0], kinds[i][2]]) + [kinds[i][4]])
            besthands.append((i, pairs))
            if pairs[1] > maxpairs[1] or pairs[1] == maxpairs[1] and pairs[0] > maxpairs[0] or \
                pairs[:2] == maxpairs[:2] and pairs[2] > maxpairs[2]:
                maxpairs = pairs
        elif kinds[i][0] == kinds[i][1] and kinds[i][2] == kinds[i][4]:
            pairs = tuple(sorted([kinds[i][0], kinds[i][2]]) + [kinds[i][3]])
            besthands.append((i, pairs))
            if pairs[1] > maxpairs[1] or pairs[1] == maxpairs[1] and pairs[0] > maxpairs[0] or \
                pairs[:2] == maxpairs[:2] and pairs[2] > maxpairs[2]:
                maxpairs = pairs
        elif kinds[i][0] == kinds[i][1] and kinds[i][3] == kinds[i][4]:
            pairs = tuple(sorted([kinds[i][0], kinds[i][3]]) + [kinds[i][2]])
            besthands.append((i, pairs))
            if pairs[1] > maxpairs[1] or pairs[1] == maxpairs[1] and pairs[0] > maxpairs[0] or \
                pairs[:2] == maxpairs[:2] and pairs[2] > maxpairs[2]:
                maxpairs = pairs
        elif kinds[i][0] == kinds[i][2] and kinds[i][3] == kinds[i][4]:
            pairs = tuple(sorted([kinds[i][0], kinds[i][3]]) + [kinds[i][1]])
            besthands.append((i, pairs))
            if pairs[1] > maxpairs[1] or pairs[1] == maxpairs[1] and pairs[0] > maxpairs[0] or \
                pairs[:2] == maxpairs[:2] and pairs[2] > maxpairs[2]:
                maxpairs = pairs
        elif kinds[i][1] == kinds[i][2] and kinds[i][3] == kinds[i][4]:
            pairs = tuple(sorted([kinds[i][1], kinds[i][3]]) + [kinds[i][0]])
            besthands.append((i, pairs))
            if pairs[1] > maxpairs[1] or pairs[1] == maxpairs[1] and pairs[0] > maxpairs[0] or \
                pairs[:2] == maxpairs[:2] and pairs[2] > maxpairs[2]:
                maxpairs = pairs
    for i in besthands:
        if i[1] != maxpairs:
            besthands.remove(i)
    for i in besthands:
        returnlist.append(hands[i[0]])
    if returnlist != []:
        return returnlist

    #dealing with one pair
    maxpair = (0, 0, 0, 0)
    for i in range(len(hands)):
        if kinds[i][0] == kinds[i][1]:
            pair = tuple([kinds[i][0]] + list(reversed(list(sorted([kinds[i][2], kinds[i][3], kinds[i][4]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
        elif kinds[i][0] == kinds[i][2]:
            pair = tuple([kinds[i][0]] + list(reversed(list(sorted([kinds[i][1], kinds[i][3], kinds[i][4]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
        elif kinds[i][0] == kinds[i][3]:
            pair = tuple([kinds[i][0]] + list(reversed(list(sorted([kinds[i][1], kinds[i][2], kinds[i][4]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
        elif kinds[i][0] == kinds[i][4]:
            pair = tuple([kinds[i][0]] + list(reversed(list(sorted([kinds[i][1], kinds[i][2], kinds[i][3]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
        elif kinds[i][1] == kinds[i][2]:
            pair = tuple([kinds[i][1]] + list(reversed(list(sorted([kinds[i][0], kinds[i][3], kinds[i][4]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
        elif kinds[i][1] == kinds[i][3]:
            pair = tuple([kinds[i][1]] + list(reversed(list(sorted([kinds[i][0], kinds[i][2], kinds[i][4]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
        elif kinds[i][1] == kinds[i][4]:
            pair = tuple([kinds[i][1]] + list(reversed(list(sorted([kinds[i][0], kinds[i][2], kinds[i][3]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
        elif kinds[i][2] == kinds[i][3]:
            pair = tuple([kinds[i][2]] + list(reversed(list(sorted([kinds[i][0], kinds[i][1], kinds[i][4]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
        elif kinds[i][2] == kinds[i][4]:
            pair = tuple([kinds[i][2]] + list(reversed(list(sorted([kinds[i][0], kinds[i][1], kinds[i][3]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
        elif kinds[i][3] == kinds[i][4]:
            pair = tuple([kinds[i][3]] + kist(reversed(list(sorted([kinds[i][0], kinds[i][1], kinds[i][2]])))))
            besthands.append((i, pair))
            if pair[0] > maxpair[0] or pair[0] == maxpair[0] and pair[1] > maxpair[1] or \
                pair[:2] == maxpair[:2] and pair[2] > maxpair[2] or pair[:3] == maxpair[:3] and pair[3] > maxpair[3]:
                maxpair = pair
    for i in besthands:
        if i[1] != maxpair:
            besthands.remove(i)
    for i in besthands:
        returnlist.append(hands[i[0]])
    if returnlist != []:
        return returnlist

    #dealing with high card or one hand
    maxorder = (0, 0, 0, 0, 0)
    for i in range(len(hands)):
        order = tuple(list(reversed(list(sorted(kinds[i])))))
        besthands.append(i)
        if order[0] > maxorder[0] or order[0] == maxorder[0] and order[1] > maxorder[1] or \
            order[:2] > maxorder[:2] and order[2] > maxorder[2] or \
            order[:3] > maxorder[:3] and order[3] > maxorder[3] or \
            order[:4] > maxorder[:4] and order[4] > maxorder[4]:
            maxorder = order
    for i in besthands:
        if i != maxorder:
            besthands.remove(i)
    for i in besthands:
        returnlist.append(hands[i])
    if len(hands) <= 1:
        return hands
    if returnlist != []:
        return returnlist
