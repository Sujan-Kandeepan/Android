def to_rna(dna):
    rna = ""
    for i in dna:
        if i == "G": rna += "C"
        elif i == "C": rna += "G"
        elif i == "T": rna += "A"
        elif i == "A": rna += "U"
        else: return ""
    return rna
