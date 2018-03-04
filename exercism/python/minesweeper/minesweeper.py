def board(board):
    for i in board:
        if len(i) != len(board[0]):
            raise ValueError
        for j in i:
            if j not in ['+', '-', '|', '*', ' ']:
                raise ValueError
    for i in [0, len(board)-1]:
        if board[i][0] != '+' or board[i][len(board[i])-1] != '+':
            raise ValueError
        for j in range(1, len(board[i])-1):
            if board[i][j] != '-':
                raise ValueError
    for i in range(1, len(board)-1):
        if board[i][0] != '|' or board[i][len(board[i])-1] != '|':
            raise ValueError
        for j in range(1, len(board[i])-1):
            count = 0
            if board[i][j] != '*':
                if board[i-1][j-1] == '*':
                    count += 1
                if board[i-1][j] == '*':
                    count += 1
                if board[i-1][j+1] == '*':
                    count += 1
                if board[i][j-1] == '*':
                    count += 1
                if board[i][j+1] == '*':
                    count += 1
                if board[i+1][j-1] == '*':
                    count += 1
                if board[i+1][j] == '*':
                    count += 1
                if board[i+1][j+1] == '*':
                    count += 1
            if count > 0:
                board[i] = board[i][:j] + str(count) + board[i][j+1:]
    for i in board: print(i)
    return board
