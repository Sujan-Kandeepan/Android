def handshake(input):
    array = []
    if type(input) == str:
        for i in input:
            if i not in ['0', '1']:
                return []
        input = int(input, 2)
    if input < 1: return []
    if input % 2 == 1: array.append('wink'); input -= 1
    if input % 4 == 2: array.append('double blink'); input -= 2
    if input % 8 == 4: array.append('close your eyes'); input -= 4
    if input % 16 == 8: array.append('jump'); input -= 8
    if input == 16: array.reverse()
    return array

def code(input):
    code = 0
    contained = []
    for i in input:
        if i not in ['wink', 'double blink', 'close your eyes', 'jump']:
            return '0'
    if 'wink' in input: contained.append('wink'); code += 1
    if 'double blink' in input: contained.append('double blink'); code += 10
    if 'close your eyes' in input: contained.append('close your eyes'); code += 100
    if 'jump' in input: contained.append('jump'); code += 1000
    if contained != input: code += 10000
    return str(code)
