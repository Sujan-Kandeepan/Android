class Luhn(object):
    def __init__(self, num):
        self.num = num.replace(' ', '')

    def is_valid(self):
        for i in self.num:
            if not self.num.isnumeric():
                return False
        if len(self.num) < 2:
            return False

        doubled = ""
        for i in range(len(self.num) - 1, -1, -1):
            if (len(self.num) - i) % 2 == 0:
                temp = eval(self.num[i]) * 2
                if temp > 9: temp -= 9
                doubled = str(temp) + doubled
            else:
                doubled = self.num[i] + doubled
        
        sumdigits = 0
        for i in doubled:
            sumdigits += eval(i)
        return True if sumdigits % 10 == 0 else False
