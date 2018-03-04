def detect_anagrams(word, candidates):
    anagrams = []
    for candidate in candidates:
        letterlist = []
        for i in candidate:
            letterlist += i.lower()
        for i in word:
            if i.lower() in letterlist:
                letterlist.remove(i.lower())
            else:
                break
        if letterlist == [] and candidate.lower() != word.lower():
            anagrams.append(candidate)
    return anagrams
