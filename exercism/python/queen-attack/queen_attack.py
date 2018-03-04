def board(w, b):
    if w == b: raise ValueError
    if w[0] not in range(8): raise ValueError
    if w[1] not in range(8): raise ValueError
    if b[0] not in range(8): raise ValueError
    if b[1] not in range(8): raise ValueError
    board = ['_'*8]*8
    board[w[0]] = '_'*w[1] + 'W' + '_'*(7-w[1])
    board[b[0]] = '_'*b[1] + 'B' + '_'*(7-b[1])
    return board

def can_attack(w, b):
    if w == b: raise ValueError
    if w[0] not in range(8): raise ValueError
    if w[1] not in range(8): raise ValueError
    if b[0] not in range(8): raise ValueError
    if b[1] not in range(8): raise ValueError
    return w[0] == b[0] or w[1] == b[1] or abs(w[0] - b[0]) == abs(w[1] - b[1])
