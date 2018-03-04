def check_brackets(brackets):
    temp = ""
    for i in brackets:
        if i in ['(', ')', '[', ']', '{', '}']:
            temp += i
    brackets = temp
    for i in range(len(brackets)):
        brackets = brackets.replace("{}", "")
        brackets = brackets.replace("[]", "")
        brackets = brackets.replace("()", "")
    return brackets == ""
