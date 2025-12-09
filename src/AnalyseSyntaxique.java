package Analyse;

import java.util.List;

public class AnalyseSyntaxique {

    private List<String> tokens;
    private int i;
    private boolean erreur = false;

    public void analyser(List<String> tokensParam) {
        tokens = tokensParam;
        i = 0;
        erreur = false;

        S();
        
        if (!erreur && i == tokens.size() - 1 && tokens.get(i).equals("#")) {
            
            System.out.println("Chaine acceptee! (Analyse syntaxique reussie)");
           
        } else {
             if (i < tokens.size() && !erreur) {
                 System.err.println("Erreur syntaxique: Structure invalide ou token inattendu près de " + tokens.get(i) + " (à l'index " + i + ")");
            } else if (!erreur) {
                System.err.println("Erreur syntaxique: Marqueur de fin de fichier ('#') manquant ou inattendu.");
            }
        }
    }

    public void S() { statement_list(); }
    
    public void statement_list() {
        if (!erreur && i < tokens.size() && !tokens.get(i).equals("#")) {
            statement();
            if (!erreur) {
                statement_list();
            }
        }
    }

    public void statement() {
        if (erreur) return;
        int save = i; 
        if (i < tokens.size() && tokens.get(i).equals("ID")) {
            i++;
            if (i < tokens.size() && tokens.get(i).equals("Assignation")) { 
                i++;
                expression();
                return; 
            }
        }
        i = save;
        expression_stmt();
    }

    public void expression_stmt() { expression(); }

    public void expression() {
        if (erreur) return;
        or_expr();
        
        if (!erreur && i < tokens.size() && tokens.get(i).equals("if")) {
            i++;
            or_expr();
            
            if (!erreur && i < tokens.size() && tokens.get(i).equals("else")) {
                i++;
                expression();
            } else {
                erreur = true;
                System.err.println("Erreur syntaxique: 'else' manquant dans l'opérateur ternaire (à l'index " + i + ")");
            }
        }
    }

    public void or_expr() {
        if (erreur) return;
        and_expr();
        while (!erreur && i < tokens.size() && tokens.get(i).equals("or")) {
            i++;
            and_expr();
        }
    }

    public void and_expr() {
        if (erreur) return;
        not_expr();
        while (!erreur && i < tokens.size() && tokens.get(i).equals("and")) {
            i++;
            not_expr();
        }
    }

    public void not_expr() {
        if (erreur) return;
        if (i < tokens.size() && tokens.get(i).equals("not")) {
            i++;
            not_expr();
        } else {
            comparison();
        }
    }

    public void comparison() {
        if (erreur) return;
        arith_expr();
        while (!erreur && i < tokens.size() && isCompOp(tokens.get(i))) {
            i++;
            arith_expr();
        }
    }

    private boolean isCompOp(String tok) {
        // Accepte l'Operateur pour les composés (ex: >=, ==, etc.)
        return tok.equals("==") || tok.equals("!=") || tok.equals("<") || tok.equals(">") || tok.equals("<=") || tok.equals(">=") || tok.equals("Operateur");
    }

    public void arith_expr() {
        if (erreur) return;
        term();
        while (!erreur && i < tokens.size() && isAddOp(tokens.get(i))) {
            i++;
            term();
        }
    }

    private boolean isAddOp(String tok) {
        return tok.equals("+") || tok.equals("-");
    }

    public void term() {
        if (erreur) return;
        factor();
        while (!erreur && i < tokens.size() && isMulOp(tokens.get(i))) {
            i++;
            factor();
        }
    }

    private boolean isMulOp(String tok) {
        return tok.equals("*") || tok.equals("/") || tok.equals("%");
    }

    // Accepte le token générique "Operateur" comme unaire pour le signe moins
    private boolean isUnaryOp(String tok) {
        return tok.equals("+") || tok.equals("-") || tok.equals("Operateur");
    }

    //Gère les parenthèses en acceptant "Séparateur"
    public void factor() {
        if (erreur) return;
        String currentToken = tokens.get(i);
        
        // Accepte "(" ou "Séparateur" pour la parenthèse ouvrante
        if (currentToken.equals("(") || currentToken.equals("Séparateur")) { 
            i++; 
            expression();
            
            // Accepte ")" ou "Séparateur" pour la parenthèse fermante
            if (!erreur && i < tokens.size() && (tokens.get(i).equals(")") || tokens.get(i).equals("Séparateur"))) { 
                i++; 
            } else {
                erreur = true;
                System.err.println("Erreur syntaxique: Parenthese fermante ')' manquante ou token inattendu (à l'index " + i + ")");
            }
        } else if (isUnaryOp(currentToken)) { 
            i++;
            factor();
        } else {
            primary();
        }
    }

    public void primary() {
        if (erreur) return;
        String currentToken = tokens.get(i);
        
        if (i < tokens.size() && 
            (currentToken.equals("ID") || 
            currentToken.equals("Number (Int)") || 
            currentToken.equals("Number (Float)") || 
            currentToken.equals("Chaine") || 
            currentToken.equals("True") || 
            currentToken.equals("False"))) 
        {
            i++;
        } else {
            erreur = true;
            System.err.println("Erreur syntaxique: Attendu ID, Nombre, Chaine ou Booléen, trouve " + currentToken + " (à l'index " + i + ")");
        }
    }
}