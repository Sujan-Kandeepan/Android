class Scale(object):
    def __init__(self, tonic, scale, intervals = 'm'*12):
        self.name = tonic[0].upper() + tonic[1:] + ' ' + scale
        sharps = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']
        flats = ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab']
        sequence = sharps
        if tonic[1:] == '#':
            sequence = sharps
        elif tonic[1:] == 'b':
            sequence = flats
        elif 'major' in scale or 'chromatic' in scale:
            if tonic[0].upper() + tonic[1:] in ['C', 'G', 'D', 'A', 'E', 'B', 'F#']:
                sequence = sharps
            elif tonic[0].upper() + tonic[1:] in ['F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb']:
                sequence = flats
        elif 'minor' in scale:
            if tonic[0].upper() + tonic[1:] in ['A', 'E', 'B', 'F#', 'C#', 'G#', 'D#']:
                sequence = sharps
            elif tonic[0].upper() + tonic[1:] in ['D', 'G', 'C', 'F', 'Bb', 'Eb']:
                sequence = flats
        print(sequence)
        index = 0
        for i in range(len(sequence)):
            if sequence[i] == tonic[0].upper() + tonic[1:]:
                index = i
        self.pitches = []
        for i in intervals:
            self.pitches.append(sequence[index])
            if i == 'A':
                index = (index + 3) % 12
            elif i == 'M':
                index = (index + 2) % 12
            elif i == 'm':
                index = (index + 1) % 12
        if self.pitches[0][0] == self.pitches[1][0] and scale != 'chromatic' and scale != 'enigmatic':
            for i in range(len(self.pitches)):
                for j in range(len(sharps)):
                    if self.pitches[i] == sharps[j]:
                        self.pitches[i] = flats[j]
