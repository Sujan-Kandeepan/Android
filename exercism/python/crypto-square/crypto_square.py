def encode(given):
    normalized = ""
    for i in given:
        if i.isalpha() or i.isnumeric():
            normalized += i.lower()
    r = int(len(normalized)**0.5)
    c = -1*(-1*len(normalized)//r) if r != 0 else 0
    encoded = ""
    for i in range(c):
        for j in range(0, len(normalized), c):
            if i+j < len(normalized):
                encoded += normalized[i+j]
        encoded += " "
    return encoded.strip()
