import datetime

def meetup_day(year, month, day, desc):
    days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
    dayindex = 0
    for i in range(len(days)):
        if days[i] == day:
            dayindex = i
    alldays = []
    for i in range(31 if month in [1,3,5,7,8,10,12] else 30 if month in [4,6,9,11] \
        else (datetime.date(year, 3, 1) - datetime.date(year, 2, 1)).days):
        alldays.append(datetime.date(year, month, i+1))
    specificdays = []
    for i in alldays:
        if i.weekday() == dayindex:
            specificdays.append(i)
    if desc == "1st": return specificdays[0]
    if desc == "2nd": return specificdays[1]
    if desc == "3rd": return specificdays[2]
    if desc == "4th": return specificdays[3]
    if desc == "5th": return specificdays[4]
    if desc == "last": return specificdays[len(specificdays)-1]
    if desc == "teenth":
        for i in specificdays:
            if i.day in range(13,20): return i
    print(specificdays, days[dayindex], days[specificdays[0].weekday()])
