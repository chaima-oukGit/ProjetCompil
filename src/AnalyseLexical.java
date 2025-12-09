package Analyse;

public class AnalyseLexical {

// ASSIGNATION
    private static int assign(char car) {
        if (car == ':') return 0;
        else if (car == '=') return 1;
        else return 2;
    }

    private static String checkAssignation(String idA) {
        String id = idA + "#";
        int i = 0;
        int[][] MAT = {
            {1, 2, -1},
            {-1, 2, -1},
            {-1, -1, -1}
        };
        int Ec = 0;
        int Ef = 2;
        char Tc = id.charAt(i);

        while (Tc != '#') {
            int c = assign(Tc);
            if (c != -1 && MAT[Ec][c] != -1) {
                Ec = MAT[Ec][c];
                i++;
                Tc = id.charAt(i);
            } else break;
        }

        if (id.charAt(i) == '#' && Ec == Ef) return "Assignation";
        return null;
    }


// CHAINES
    private static int chaine(char car) {
        if (car == '"') return 0;
        else if (car == '\'') return 1;
        else return 2;
    }

    private static String checkChaine(String idA) {
        String id = idA + "#";
        int i = 0;

        int[][] MAT = {
            {1, 3, -1},
            {2, -1, 1},
            {-1, -1, -1},
            {-1, 4, 3},
            {-1, -1, -1}
        };

        int Ec = 0;
        int[] Ef = {2, 4};

        char Tc = id.charAt(i);
        while (Tc != '#') {
            int c = chaine(Tc);
            if (c != -1 && MAT[Ec][c] != -1) {
                Ec = MAT[Ec][c];
                i++;
                Tc = id.charAt(i);
            } else break;
        }

        // accepté
        for (int f : Ef) {
            if (Ec == f && id.charAt(i) == '#') {
                return "Chaine";
            }
        }

        // non fermée
        if (idA.startsWith("\"") || idA.startsWith("'")) {
            return "Erreur Lexicale : chaîne non fermée";
        }

        return null;
    }

// COMMENTAIRES
    private static int comment(char car) {
        if (car == '#') return 0;
        else if (car == '\n') return 1;
        else return 2;
    }

    private static String checkCommentaire(String idA) {
        if (!idA.startsWith("#")) return null;
        return "Commentaire";
    }

// ESPACES
    private static int space(char car) {
        if (car == ' ') return 0;
        else if (car == '\t') return 1;
        else if (car == '\n') return 2;
        else return 3;
    }

    private static String checkEspace(String idA) {
        for (char c : idA.toCharArray()) {
            if (c != ' ' && c != '\t') return null;
        }
        return "Espace";
    }

// KEYWORD
    private static int idChar(char car) {
        if ((car >= 'a' && car <= 'z') || (car >= 'A' && car <= 'Z') || car == '_')
            return 0;
        else if (car >= '0' && car <= '9')
            return 1;
        else return 2;
    }

    private static String[] KEYWORDS = {
        "if","else","while","for","return",
        "in","not","and","or","True","False","chaima","Oukachbi","while","for","break","def","lambda",
"class"
    };

    private static String checkID(String idA) {
        String id = idA + "#";
        int i = 0;

        int[][] MAT = {
            {1, -1, -1},
            {1, 1, -1},
            {-1, -1, -1}
        };

        int Ec = 0;
        int Ef = 1;

        char Tc = id.charAt(i);
        while (Tc != '#') {
            int c = idChar(Tc);
            if (c != -1 && MAT[Ec][c] != -1) {
                Ec = MAT[Ec][c];
                i++;
                Tc = id.charAt(i);
            } else break;
        }

        // Identifiant valide
        if (id.charAt(i) == '#' && Ec == Ef) {
    for (String k : KEYWORDS) {
        if (k.equalsIgnoreCase(idA)) return k;  // Retourner le mot-clé
    }
    return "ID";
}
        // Erreur lexical : commence par chiffre
        if (idA.charAt(0) >= '0' && idA.charAt(0) <= '9')
            return "Erreur Lexicale : identificateur commence par un chiffre";

        return null;
    }

// NOMBRES
   private static int number(char car) {
       if (car >= '0' && car <= '9'){ return 0;
       }else if (car == '.') {return 1;
       }else{ return 2;}
   } 
   private static String checkNombre(String idA) {
       String id = idA + "#";
       int i = 0;
       int[][] MAT = { 
           {0, 1, -1},
           {1, -1, -1}
       }; 
       int Ec = 0; 
       int[] Ef = {0, 1}; 
       char Tc = id.charAt(i); 
       while (Tc != '#') { 
           int c = number(Tc);
           if (c != -1 && MAT[Ec][c] != -1) { 
               Ec = MAT[Ec][c]; 
               i++;
               Tc = id.charAt(i);
           } else { break; 
           }
       }
       boolean estFinal = false;
       for (int f : Ef) { 
           if (Ec == f) {
               estFinal = true;
               break;
           }
       }
       if (id.charAt(i) == '#' && estFinal && i == id.length() - 1) {
           boolean isFloat = false;
           for (char ch : idA.toCharArray()) { 
               if (ch == '.') {
                   isFloat = true;
                   break;
               }
           }
           if (isFloat) {
               return "Number (Float)";
           } else if (! isFloat) { return "Number (Int)";
           }else{ 
               return "nombre invalid";
           }
       }
       return null;
   }
    
// OPERATEURS
private static int op(char car) { 
    if (car == '=') {return 0;
    }else if (car == '<') {return 1;
    }else if (car == '>') {return 2; 
    }else if (car == '!') {return 3;
    }else if (car == '+') {return 4;
    }else if (car == '-') {return 5; 
    }else if (car == '%') {return 6;
    }else if (car == '/') {return 7;
    }else if (car == '*') {return 8;
    } else {return 9;} }
private static String checkOperateur(String idA) { 
    String id = idA + "#"; 
    int i = 0; 
    int[][] MAT = {
        {1, 1, 1, 1, 2, 2, 2, 2, 2, -1}, 
        {2, -1, -1, -1, -1, -1, -1, -1, -1, -1}, 
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1} }; 
    int Ec = 0; 
    int[] Ef = {1, 2};
    char Tc = id.charAt(i);
    while (Tc != '#') {
        int c = op(Tc); 
        if (c != -1 && MAT[Ec][c] != -1) {
            Ec = MAT[Ec][c]; i++; Tc = id.charAt(i);
        } else { break; } 
    }
    boolean estFinal = false;
    for (int f : Ef) { 
        if (Ec == f) { 
            estFinal = true;
            break;
        }
    } 
    if (id.charAt(i) == '#' && estFinal && i == id.length() - 1) {
        return "Operateur";
    } return null;
}

// SEPARATEURS
private static int separateur(char car) {
    if (car == '(' || car == ')' || car == '{' || car == '}' ||
        car == '[' || car == ']' || car == ',' || car == ':' || car == ';')
        return 0;
    else return 1;
}

private static String checkSeparateur(String idA) {
    String id = idA + "#";
    int i = 0;

    int[][] MAT = {
        {1, -1},
        {-1, -1}
    };

    int Ec = 0;
    int Ef = 1;
    char Tc = id.charAt(i);

    while (Tc != '#') {
        int c = separateur(Tc);
        if (c != -1 && MAT[Ec][c] != -1) {
            Ec = MAT[Ec][c];
            i++;
            Tc = id.charAt(i);
        } else break;
    }

    if (id.charAt(i) == '#' && Ec == Ef && i == id.length() - 1)
        return "Séparateur";

    return "Erreur lexicale : séparateur invalide  " + idA;
}

// GET TOKEN
    public static String getToken(String tok) {
    String r;

    if ((r = checkEspace(tok)) != null) return r;
    if ((r = checkCommentaire(tok)) != null) return r;
    if ((r = checkAssignation(tok)) != null) return r;
    if ((r = checkChaine(tok)) != null) return r;
    if ((r = checkNombre(tok)) != null) return r;
    if ((r = checkID(tok)) != null) return r;
    if ((r = checkOperateur(tok)) != null) return r;
    if ((r = checkSeparateur(tok)) != null) return r;

    return "Erreur Lexicale : token invalide";
}
}
