def say(number):
    if number == 0: return "zero"
    if number < 0 or number > 10**12-1: raise AttributeError
    billions, millions, thousands, lastthree = 0, 0, 0, 0
    numstring = str(int(number))
    if len(numstring) < 4:
        lastthree = number
    elif len(numstring) < 7:
        thousands = eval(numstring[:len(numstring)-3].lstrip("0"))
        if numstring[len(numstring)-3:].lstrip("0") != "":
            lastthree = eval(numstring[len(numstring)-3:].lstrip("0"))
    elif len(numstring) < 10:
        millions = eval(numstring[:len(numstring)-6].lstrip("0"))
        if numstring[len(numstring)-6:len(numstring)-3].lstrip("0") != "":
            thousands = eval(numstring[len(numstring)-6:len(numstring)-3].lstrip("0"))
        if numstring[len(numstring)-3:].lstrip("0") != "":
            lastthree = eval(numstring[len(numstring)-3:].lstrip("0"))
    else:
        billions = eval(numstring[:len(numstring)-9].lstrip("0"))
        if numstring[len(numstring)-9:len(numstring)-6].lstrip("0") != "":
            millions = eval(numstring[len(numstring)-9:len(numstring)-6].lstrip("0"))
        if numstring[len(numstring)-6:len(numstring)-3].lstrip("0") != "":
            thousands = eval(numstring[len(numstring)-6:len(numstring)-3].lstrip("0"))
        if numstring[len(numstring)-3:].lstrip("0") != "":
            lastthree = eval(numstring[len(numstring)-3:].lstrip("0"))

    ones = ["one", "two", "three", "four", "five", "six", "seven", "eight", "nine"]
    teens = ["eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"]
    tens = ["ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"]

    def justtwo(just2digits):
        if just2digits < 10:
            return ones[just2digits-1]
        elif just2digits < 100 and just2digits % 10 == 0:
            return tens[just2digits // 10 - 1]
        elif just2digits < 100 and just2digits // 10 == 1:
            return teens[just2digits % 10 - 1]
        elif just2digits < 100:
            return tens[just2digits // 10 - 1] + "-" + ones[just2digits % 10 - 1]

    def justthree(just3digits):
        print(just3digits)
        if just3digits < 100:
            return justtwo(just3digits)
        elif just3digits % 100 == 0:
            return ones[just3digits // 100 - 1] + " hundred"
        else:
            return ones[just3digits // 100 - 1] + " hundred and " + justtwo(just3digits % 100)

    inwords = ""
    if billions != 0: inwords += justthree(billions) + " billion "
    if millions != 0: inwords += justthree(millions) + " million "
    if thousands != 0: inwords += justthree(thousands) + " thousand "
    if lastthree != 0 and thousands == 0 and (millions != 0 or billions != 0): inwords += "and "
    if lastthree != 0: inwords += justthree(lastthree)
    return inwords.strip()
