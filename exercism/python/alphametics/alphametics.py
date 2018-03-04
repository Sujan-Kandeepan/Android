def solve(equation):
    letters = []
    combos = [[0], [1], [2], [3], [4], [5], [6], [7], [8], [9]]
    for i in range(65, 91):
        if chr(i) in equation:
            letters.append(chr(i))
    for i in range(len(letters)-1):
        temp = []
        for j in combos:
            for k in range(10):
                if k not in j:
                    temp.append(j + [k])
        combos = temp
    for i in combos:
        tempeq = equation
        for j in range(len(letters)):
            tempeq = tempeq.replace(letters[j], str(i[j]))
        print(tempeq)
        try:
            if eval(tempeq):
                solution = {}
                for j in range(len(letters)):
                    solution[letters[j]] = i[j]
                return solution
        except:
            print()
    return {}
