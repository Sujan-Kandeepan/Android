class Allergies(object):
    def __init__(self, score):
        allergens = ['eggs', 'peanuts', 'shellfish', 'strawberries', 'tomatoes', 'chocolate', 'pollen', 'cats']
        score = score % 256
        lst = []
        for i in range(7, -1, -1):
            if score // 2**i > 0:
                lst.append(allergens[i])
                score -= 2**i
        lst.reverse()
        self.lst = lst

    def is_allergic_to(self, allergen):
        return True if allergen in self.lst else False
