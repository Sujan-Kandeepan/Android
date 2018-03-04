def abbreviate(full):
    abbreviation = ""
    full = full.replace("-", " ")
    midwordcaps = []
    for i in range(len(full)):
        if full[i].isupper() and i != 0 and full[i-1].islower():
            midwordcaps += full[i]
    for i in midwordcaps:
        full = full.replace(i, " " + i)
    words = full.split()
    abbreviation = ""
    for i in words:
        abbreviation += i[0].upper()
    return abbreviation
