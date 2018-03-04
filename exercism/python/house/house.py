def verse(num):
    phrases = ["house that Jack built.",
               "malt\nthat lay in the ",
               "rat\nthat ate the ",
               "cat\nthat killed the ",
               "dog\nthat worried the ",
               "cow with the crumpled horn\nthat tossed the ",
               "maiden all forlorn\nthat milked the ",
               "man all tattered and torn\nthat kissed the ",
               "priest all shaven and shorn\nthat married the ",
               "rooster that crowed in the morn\nthat woke the ",
               "farmer sowing his corn\nthat kept the ",
               "horse and the hound and the horn\nthat belonged to the "]

    verse = "This is the "
    for i in range(num+1):
        verse = verse[:12] + phrases[i] + verse[12:]
    return verse


def rhyme():
    rhyme = verse(0)
    for i in range(1, 12):
        rhyme = rhyme + "\n\n" + verse(i)
    return rhyme
