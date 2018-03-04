def grep(key, files, flags = ''):
    matches = []
    print('-n' in flags, '-l' in flags, '-i' in flags, '-v' in flags, '-x' in flags)
    for file in files:
        linenum = 0
        with open(file, 'r') as f:
            lines = f.readlines()
            for line in lines:
                line = line.strip()
                match = ''
                linenum += 1
                if '-i' in flags:
                    keylower, linelower = key.lower(), line.lower()
                else:
                    keylower, linelower = key, line
                if ((keylower == linelower) if '-x' in flags else (keylower in linelower)) != ('-v' in flags):
                        match = line
                if match != '' and '-n' in flags:
                    match = str(linenum) + ':' + match
                if len(files) > 1 and match != '':
                    match = file + ':' + match
                if '-l' in flags and match != '':
                    match = file
                    if match in matches:
                        match = ''
                if match != '':
                    matches.append(match)
    return '\n'.join(matches) + '\n' if matches != [] else ''
