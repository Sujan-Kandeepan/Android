def number(grid):
    num0 = [" _ ", "| |", "|_|",  "   "]
    num1 = ["   ", "  |", "  |",  "   "]
    num2 = [" _ ", " _|", "|_ ",  "   "]
    num3 = [" _ ", " _|", " _|",  "   "]
    num4 = ["   ", "|_|", "  |",  "   "]
    num5 = [" _ ", "|_ ", " _|",  "   "]
    num6 = [" _ ", "|_ ", "|_|",  "   "]
    num7 = [" _ ", "  |", "  |",  "   "]
    num8 = [" _ ", "|_|", "|_|",  "   "]
    num9 = [" _ ", "|_|", " _|",  "   "]
    numbers = [num0, num1, num2, num3, num4, num5, num6, num7, num8, num9]

    if len(grid) != 4 or len(grid[0]) % 3 != 0:
        raise  ValueError

    for i in range(1, 4):
        if len(grid[i]) != len(grid[0]):
            raise ValueError

    number = ""
    for i in range(0, len(grid[0]), 3):
        nextnum = "?"
        for j in range(0, 10):
            if [grid[0][i:i+3], grid[1][i:i+3], grid[2][i:i+3], grid[3][i:i+3]] == numbers[j]:
                nextnum = str(j)
        number += nextnum
    return number

def grid(digits):
    num0 = [" _ ", "| |", "|_|",  "   "]
    num1 = ["   ", "  |", "  |",  "   "]
    num2 = [" _ ", " _|", "|_ ",  "   "]
    num3 = [" _ ", " _|", " _|",  "   "]
    num4 = ["   ", "|_|", "  |",  "   "]
    num5 = [" _ ", "|_ ", " _|",  "   "]
    num6 = [" _ ", "|_ ", "|_|",  "   "]
    num7 = [" _ ", "  |", "  |",  "   "]
    num8 = [" _ ", "|_|", "|_|",  "   "]
    num9 = [" _ ", "|_|", " _|",  "   "]
    numbers = [num0, num1, num2, num3, num4, num5, num6, num7, num8, num9]

    if not digits.isnumeric():
        raise ValueError

    grid = ["", "", "", ""]
    for i in digits:
        grid[0] += numbers[int(i)][0]
        grid[1] += numbers[int(i)][1]
        grid[2] += numbers[int(i)][2]
        grid[3] += numbers[int(i)][3]
    return grid
