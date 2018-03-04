def is_isogram(word):
    for i in range(len(word)):
        for j in range(len(word)):
            if word[i].lower() == word[j].lower() and i != j and word[i].isalpha() and word[j].isalpha():
                return False
    return True
