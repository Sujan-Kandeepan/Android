def verse(day):
    days = ["first", "second", "third", "fourth", "fifth", "sixth", \
           "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"]
    gifts = ["twelve Drummers Drumming, ", \
            "eleven Pipers Piping, ", \
            "ten Lords-a-Leaping, ", \
            "nine Ladies Dancing, ", \
            "eight Maids-a-Milking, ", \
            "seven Swans-a-Swimming, ", \
            "six Geese-a-Laying, ", \
            "five Gold Rings, ", \
            "four Calling Birds, ", \
            "three French Hens, ", \
            "two Turtle Doves, ", \
            "and a Partridge in a Pear Tree.\n"]
    verse = "On the " + days[day-1] + " day of Christmas my true love gave to me, "
    if day == 1:
        verse += "a Partridge in a Pear Tree.\n"
    else:
        for i in range(12-day, 12):
            verse += gifts[i]
    return verse

def verses(start, end):
    verses = ""
    for i in range(start, end+1):
        verses += verse(i) + "\n"
    return verses


def sing():
    return verses(1, 12)
