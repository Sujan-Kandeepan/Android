def encode(given):
    nospaces = ""
    for i in range(len(given)):
        if ord(given[i]) in range(65, 91):
            nospaces += chr(187-ord(given[i]))
        elif ord(given[i]) in range(97, 123):
            nospaces += chr(219-ord(given[i]))
        elif ord(given[i]) in range(48, 58):
            nospaces += given[i]
    newstring = ""
    for i in range(len(nospaces)):
        if i % 5 == 4:
            newstring += nospaces[i] + " "
        else:
            newstring += nospaces[i]
    return newstring.strip()

def decode(given):
    given.replace(" ", "")
    newstring = ""
    for i in range(len(given)):
        if ord(given[i]) in range(65, 91):
            newstring += chr(187-ord(given[i]))
        elif ord(given[i]) in range(97, 123):
            newstring += chr(219-ord(given[i]))
        elif ord(given[i]) in range(48, 58):
            newstring += given[i]
    return newstring
