def verse(num):
    def bottles(bottles):
        if bottles > 99 or num < 0:
            raise ValueError
        elif bottles == 1:
            return "1 bottle"
        elif bottles == 0:
            return "No more bottles"
        else:
            return str(bottles) + " bottles"

    if num != 0:
        return bottles(num) + " of beer on the wall, " + bottles(num).lower() + " of beer.\n" \
            + "Take " + ("it" if num == 1 else "one") + " down and pass it around, " \
            + bottles(num-1).lower() + " of beer on the wall.\n"
    else:
        return "No more bottles of beer on the wall, no more bottles of beer.\n" \
            + "Go to the store and buy some more, 99 bottles of beer on the wall.\n"

def song(start, end = 0):
    song = ""
    for i in range(start, end-1, -1):
        song += verse(i) + "\n"
    return song
