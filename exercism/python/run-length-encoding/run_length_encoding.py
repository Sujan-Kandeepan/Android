def decode(string):
    new = ""
    number = ""
    for i in range(0, len(string)):
        if string[i] in ['1','2','3','4','5','6','7','8','9','0']:
            number += string[i]
        else:
            if number == "": number = 1
            for j in range(int(number)):
                new += string[i]
            number = ""
    return new

def encode(string):
    if string == "": return string
    new = ""
    number = 1
    for i in range(len(string)-1):
        if string[i] == string[i+1]:
            number += 1
        else:
            if number != 1: new += str(number)
            new += string[i]
            number = 1
    if string[len(string)-2] == string[len(string)-1]:
        if number != 1: new += str(number)
        new += string[len(string)-1]
    else:
        if number != 1: new += str(number)
        new += string[len(string)-1]
    return new
