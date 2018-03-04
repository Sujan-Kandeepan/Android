def numeral(arabic):
    thousands = arabic // 1000
    hundreds = arabic // 100 - 10 * thousands
    tens = arabic // 10 - 10 * hundreds - 100 * thousands
    ones = arabic - 10 * tens - 100 * hundreds - 1000 * thousands

    numeral = ""

    for i in range(thousands): numeral += "M"
    for i in range(hundreds): numeral += "C"
    for i in range(tens): numeral += "X"
    for i in range(ones): numeral += "I"

    numeral = numeral.replace("C" * 9, "CM")
    numeral = numeral.replace("C" * 5, "D")
    numeral = numeral.replace("C" * 4, "CD")

    numeral = numeral.replace("X" * 9, "XC")
    numeral = numeral.replace("X" * 5, "L")
    numeral = numeral.replace("X" * 4, "XL")

    numeral = numeral.replace("I" * 9, "IX")
    numeral = numeral.replace("I" * 5, "V")
    numeral = numeral.replace("I" * 4, "IV")

    return numeral
