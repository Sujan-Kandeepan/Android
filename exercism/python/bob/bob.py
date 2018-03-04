def hey(question):
    hasletters = False
    for i in question:
        if not i.isalpha() and i not in ['1','2','3','4','5','6','7','8','9','0', "?"]:
            question = question.replace(i, " ")
        if i.isalpha(): hasletters = True
    question = question.strip()
    print(question, question.upper(), hasletters)
    if question == "": return "Fine. Be that way!"
    if question == question.upper() and hasletters: return "Whoa, chill out!"
    if question.endswith("?"): return "Sure."
    return "Whatever."
