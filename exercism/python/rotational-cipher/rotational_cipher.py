def rotate(string, shift):
    new = ""
    for i in string:
        if ord(i) in range(65, 91):
            new += chr((ord(i) - 65 + shift) % 26 + 65)
        elif ord(i) in range(97, 123):
            new += chr((ord(i) - 97 + shift) % 26 + 97)
        else:
             new += i
    return new
