def word_count(string):
    for i in string:
        if not i.isalpha() and i not in ['1','2','3','4','5','6','7','8','9','0']:
            string = string.replace(i, " ")
    words = string.split()
    print(words)
    counts = {}
    for i in words:
        if i.lower() not in counts.keys():
            counts[i.lower()] = 1
        else:
            counts[i.lower()] = counts[i.lower()] + 1
    return counts
