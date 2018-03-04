def tally(data):
    data = data.split('\n')
    if data == ['']:
        data = []
    teams = []
    for line in data:
        line = line.split(';')
        found1, found2 = False, False
        for i in range(len(teams)):
            if teams[i][0] == line[0]:
                found1 = True
                team, mp, w, d, l, p = teams[i]
                mp += 1
                if line[2] == 'win':
                    w += 1
                    p += 3
                elif line[2] == 'draw':
                    d += 1
                    p += 1
                elif line[2] == 'loss':
                    l += 1
                teams[i] = (team, mp, w, d, l, p)
            elif teams[i][0] == line[1]:
                found2 = True
                team, mp, w, d, l, p = teams[i]
                mp += 1
                if line[2] == 'win':
                    l += 1
                elif line[2] == 'draw':
                    d += 1
                    p += 1
                elif line[2] == 'loss':
                    w += 1
                    p += 3
                teams[i] = (team, mp, w, d, l, p)
        if not found1:
            if line[2] == 'win':
                teams.append((line[0], 1, 1, 0, 0, 3))
            elif line[2] == 'draw':
                teams.append((line[0], 1, 0, 1, 0, 1))
            elif line[2] == 'loss':
                teams.append((line[0], 1, 0, 0, 1, 0))
        if not found2:
            if line[2] == 'win':
                teams.append((line[1], 1, 0, 0, 1, 0))
            elif line[2] == 'draw':
                teams.append((line[1], 1, 0, 1, 0, 1))
            elif line[2] == 'loss':
                teams.append((line[1], 1, 1, 0, 0, 3))
    teams = sorted(teams)
    for i in range(len(teams)):
        for j in range(i+1, len(teams)):
            if teams[i][5] < teams[j][5]:
                teams[i], teams[j] = teams[j], teams[i]
    table = 'Team                           | MP |  W |  D |  L |  P'
    for team in teams:
        table += '\n' + team[0].ljust(30) + ' | ' + str(team[1]).rjust(2) + ' | ' + str(team[2]).rjust(2) + \
            ' | ' + str(team[3]).rjust(2) + ' | ' + str(team[4]).rjust(2) + ' | ' + str(team[5]).rjust(2)
    return table
