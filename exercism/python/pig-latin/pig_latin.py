def translate(english):
    def vowelstart(word):
        return word + 'ay'

    def consonantstart(word):
        consonants = ""
        for i in word:
            if i not in ['a', 'e', 'i', 'o', 'u'] or consonants.endswith('q') and i not in ['a', 'e', 'i', 'o']:
                consonants += i
                word = word[1:]
            else:
                break
        return word + consonants + 'ay'

    words = english.split()
    piglatin = ""
    for word in words:
        if ((word[0] == 'x' or word[0] == 'y') and word[1] not in ['a', 'e', 'i', 'o']) or word[0] in ['a', 'e', 'i', 'o', 'u']:
            piglatin += vowelstart(word) + ' '
        else:
            piglatin += consonantstart(word) + ' '
    return piglatin.strip()
