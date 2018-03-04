def of_codon(codon):
    if codon == 'AUG':
        return 'Methionine'
    elif codon in ['UUU', 'UUC']:
        return 'Phenylalanine'
    elif codon in ['UUA', 'UUG']:
        return 'Leucine'
    elif codon in ['UCU', 'UCC', 'UCA', 'UCG']:
        return 'Serine'
    elif codon in ['UAU', 'UAC']:
        return 'Tyrosine'
    elif codon in ['UGU', 'UGC']:
        return 'Cysteine'
    elif codon == 'UGG':
        return 'Tryptophan'
    elif codon in ['UAA', 'UAG', 'UGA']:
        return 'STOP'
    else:
        raise ValueError


def of_rna(strand):
    codons = []
    for i in range(0, len(strand), 3):
        if of_codon(strand[i:i+3]) == 'STOP':
            return codons
        else:
            codons.append(of_codon(strand[i:i+3]))
    return codons
