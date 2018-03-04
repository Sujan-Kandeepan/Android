def fence_pattern(text, rails):
    increasing = True
    num = 1
    indexes = ""
    for i in range(len(text)):
        indexes += str(num)
        if num == rails:
            increasing = False
            num -= 1
        elif num == 1:
            increasing = True
            num += 1
        elif increasing:
            num += 1
        else:
            num -= 1
    return indexes

def encode(decoded, rails):
    encoded = ""
    pieces = [""] * rails
    for i in range(len(decoded)):
        railnum = fence_pattern(decoded, rails)[i]
        pieces[eval(railnum)-1] += decoded[i]
    for i in pieces:
        encoded += i
    return encoded


def decode(encoded, rails):
    decoded = ""
    pieces = []
    lengths = [0] * rails
    copy = encoded
    for i in fence_pattern(encoded, rails):
        lengths[eval(i)-1] += 1
    for i in lengths:
        pieces.append(copy[:i])
        copy = copy[i:]
    for i in fence_pattern(encoded, rails):
        decoded += pieces[eval(i)-1][0]
        pieces[eval(i)-1] = pieces[eval(i)-1][1:]
    return decoded
