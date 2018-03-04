class Garden(object):
    def __init__(self, key, students = ['Alice', 'Bob', 'Charlie', 'David', 'Eve', 'Fred', 'Ginny', \
        'Harriet', 'Ileana', 'Joseph', "Kincaid", 'Larry']):
        self.students = students
        self.students.sort()
        self.key = key

    def plants(self, name):
        index = 0
        for i in range(len(self.students)):
            if self.students[i] == name:
                index = i
        rows = self.key.split('\n')
        garden = [rows[0][2*index], rows[0][2*index+1], rows[1][2*index], rows[1][2*index+1]]
        for i in range(len(garden)):
            if garden[i] == 'V': garden[i] = 'Violets'
            if garden[i] == 'R': garden[i] = 'Radishes'
            if garden[i] == 'C': garden[i] = 'Clover'
            if garden[i] == 'G': garden[i] = 'Grass'
        return garden
