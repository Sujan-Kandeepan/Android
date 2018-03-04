class Matrix(object):
    def __init__(self, string):
        lines = string.split('\n')
        self.rows = []
        for i in lines:
            row = i.split()
            for j in range(len(row)):
                row[j] = eval(row[j])
            self.rows.append(row)
        self.columns = []
        for i in range(len(self.rows[0])):
            column = []
            for j in range(len(self.rows)):
                column.append(self.rows[j][i])
            self.columns.append(column)
