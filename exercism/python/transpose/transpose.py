def transpose(input):
    rows = input.split("\n")

    maxlength = 0
    for i in rows:
        if len(i) > maxlength:
            maxlength = len(i)

    for i in range(len(rows)):
        rows[i] = rows[i] + " " * (maxlength - len(rows[i]))

    newrows = [""] * maxlength
    for i in rows:
        for j in range(maxlength):
            newrows[j] += i[j]
    return "\n".join(newrows).rstrip()
