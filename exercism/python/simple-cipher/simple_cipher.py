import random
import math

class Cipher(object):
    def __init__(self, key = ""):
        if key == "":
            for i in range(100):
                key += chr(random.randint(97, 122))
        for i in key:
            if not i.isalpha() or i != i.lower():
                raise ValueError
        self.key = key

    def encode(self, original):
        key = self.key[:len(original)] if len(self.key) >= len(original) else self.key * math.ceil(len(original) / len(self.key))
        encoded = ""
        for i in range(len(original)):
            if original[i].isalpha():
                encoded += chr((ord(original[i].lower()) + ord(key[i].lower()) - 194) % 26 + 97)
        return encoded

    def decode(self, original):
        key = self.key[:len(original)] if len(self.key) >= len(original) else self.key * math.ceil(len(original) / len(self.key))
        decoded = ""
        for i in range(len(original)):
            if original[i].isalpha():
                decoded += chr((ord(original[i].lower()) - ord(key[i].lower())) % 26 + 97)
        return decoded


class Caesar(object):
    def __init__(self):
        pass

    def encode(self, original):
        encoded = ""
        for i in original:
            if i.isalpha():
                encoded += chr((ord(i.lower()) - 94) % 26 + 97)
        return encoded

    def decode(self, original):
        decoded = ""
        for i in original:
            if i.isalpha():
                decoded += chr((ord(i.lower()) - 100) % 26 + 97)
        return decoded
