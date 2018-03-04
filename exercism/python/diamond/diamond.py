def make_diamond(letter):
    if letter == 'A':
        return 'A\n'
    letternum = ord(letter)-64
    diamond = [letter + ' '*(2*letternum-3) + letter]
    for i in range(letternum-1, 1, -1):
        row = [' '*(letternum-i) + chr(i+64) + ' '*(2*i-3) + chr(i+64) + ' '*(letternum-i)]
        diamond = row + diamond + row
    row = [' '*(letternum-1) + 'A' + ' '*(letternum-1)]
    diamond = row + diamond + row
    return '\n'.join(diamond) + '\n'
