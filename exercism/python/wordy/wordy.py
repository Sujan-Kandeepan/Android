def calculate(question):
    question = question.replace("plus", "+")
    question = question.replace("minus", "-")
    question = question.replace("multiplied by", "*")
    question = question.replace("divided by", "/")
    question = question.replace("What is ", "")
    question = question.replace("?", "")
    print(question)
    try:
        symbols = question.split()
        while len(symbols) > 1:
            if len(symbols) > 3:
                symbols = [str(eval(symbols[0] + symbols[1] + symbols[2]))] + symbols[3:]
            else:
                symbols = [str(eval(symbols[0] + symbols[1] + symbols[2]))]
        return eval(symbols[0])
    except:
        raise ValueError
