class Clock(object):
    def __init__(self, hours, minutes):
        self.hours = (hours + minutes // 60) % 24
        self.minutes = minutes % 60

    def __str__(self):
        return str(self.hours).zfill(2) + ':' + str(self.minutes).zfill(2)

    def add(self, extra):
        return Clock(self.hours, self.minutes + extra)

    def __eq__(self, other):
        return self.hours == other.hours and self.minutes == other.minutes
