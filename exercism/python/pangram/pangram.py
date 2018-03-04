def is_pangram(sentence):
    for i in range(65, 91):
        if chr(i) not in sentence and chr(i).lower() not in sentence:
            return False
    return True
