def encode(values):
    encoded = []
    for value in values:
        bits = []
        for i in range(7, -1, -1):
            if value // 128**i > 0 or len(bits) > 0:
                bits.append(bin(value // 128**i))
                if value // 128**i > 0:
                    value -= 128**i * (value // 128**i)
        for i in range(len(bits)-1):
            bits[i] = eval(hex(eval(bits[i]) + 128))
        if len(bits) > 0:
            bits[len(bits)-1] = eval(hex(eval(bits[len(bits)-1])))
        else:
            bits = [0]
        encoded.extend(bits)
    return encoded


def decode(bytes):
    if bytes[len(bytes)-1] >= 128:
        raise ValueError
    decoded = []
    values = []
    start, end = 0, 0
    for i in range(len(bytes)):
        if bytes[i] < 128:
            values.append(bytes[start:end+1])
            start = i+1
            end = start
        else:
            end += 1
    for bits in values:
        value = 0
        for i in range(len(bits)-1):
            bits[i] -= 128
        for i in range(len(bits)):
            value += bits[i] * 128**(len(bits)-i-1)
        decoded.append(value)
    return decoded
