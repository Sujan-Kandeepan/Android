def find_minimum_coins(total_change, coins):
    bestcombo = -1
    numcoins = total_change // coins[0] + 1
    combos = list(range(total_change // coins[0]+1))
    for i in range(len(combos)):
        combos[i] = [combos[i]]
    for coin in coins[1:]:
        temp = []
        for combo in combos:
            for i in range(total_change // coin + 1):
                temp.append(combo + [i])
        if coin <= total_change:
            combos = temp
        else:
            for i in range(len(combos)):
                combos[i] = combos[i] + [0]
    for combo in combos:
        change = []
        for i in range(len(combo)):
            change.extend([coins[i]] * combo[i])
        if sum(change) == total_change and len(change) <= numcoins:
            bestcombo = change
            numcoins = len(change)
    return bestcombo
