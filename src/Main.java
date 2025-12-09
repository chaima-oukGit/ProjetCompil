package Analyse;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AnalyseSyntaxique parse = new AnalyseSyntaxique();

        System.out.println("Entrez votre code Python (terminez par une ligne vide et Entrée) :");

        // Lecture multi-lignes
        StringBuilder codeSource = new StringBuilder();
        String ligne;
        
        String filePath = "C:\\Users\\dell\\Desktop\\chriiif\\MiniCompilateur\\src\\Analyse\\input.txt"; 
        
        try{
     
            ligne = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
            ligne = ligne + '#';
        }catch( Exception e){
            System.err.println("erreur de lecture de fichier : " +e.getMessage());
            return;
        }
            

        String input = ligne;

        if (input.trim().isEmpty()) {
            System.out.println("Aucun code entré. Fin de l'analyse.");
            return;
        }

        // Tokenisation complète
        List<String> tokens = tokenize(input);
        
        System.out.println("\nTokens lexicaux générés :");
        for (String token : tokens) {
            if (!token.equals("#") && !token.equals("Espace") && !token.startsWith("Commentaire")) {
                System.out.println("-> " + token);
            } else if (token.startsWith("Erreur Lexicale")) {
                System.err.println("-> " + token);
            }
        }
        
        // Analyse syntaxique
        parse.analyser(tokens);
    }
    
    
     // Méthode pour segmenter le code complet caractère par caractère (tokenisation).
    
    private static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        int pos = 0;
        
        while (pos < input.length()) {
            char current = input.charAt(pos);
            
            // ESPACES
            if (Character.isWhitespace(current)) {
                pos++;
                continue;
            }
            
            // IDENTIFICATEUR ou MOT-CLÉ
            if (Character.isLetter(current) || current == '_') {
                StringBuilder sb = new StringBuilder();
                while (pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
                    sb.append(input.charAt(pos));
                    pos++;
                }
                String tokenStr = sb.toString();
                tokens.add(AnalyseLexical.getToken(tokenStr));
                
            // NOMBRES
            } else if (Character.isDigit(current)) {
                StringBuilder sb = new StringBuilder();
                boolean hasDot = false;
                while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
                    if (input.charAt(pos) == '.') {
                        if (hasDot) break;
                        hasDot = true;
                    }
                    sb.append(input.charAt(pos));
                    pos++;
                }
                String tokenStr = sb.toString();
                tokens.add(AnalyseLexical.getToken(tokenStr));
                
            // CHAÎNES
            } else if (current == '"' || current == '\'') {
                StringBuilder sb = new StringBuilder();
                char quote = current;
                sb.append(current);
                pos++;
                
                while (pos < input.length() && input.charAt(pos) != quote) {
                     sb.append(input.charAt(pos));
                     pos++;
                }
                
                if (pos < input.length()) {
                    sb.append(input.charAt(pos));
                    pos++;
                }
                
                String tokenStr = sb.toString();
                tokens.add(AnalyseLexical.getToken(tokenStr));
                
            // OPÉRATEURS ET AFFECTATION (gestion des multi-caractères)
            } else if ("!><=+-*/%".indexOf(current) != -1) {
                StringBuilder sb = new StringBuilder();
                sb.append(current);
                pos++;
                
                if (pos < input.length()) {
                    char next = input.charAt(pos);
                    if (next == '=') {
                        sb.append(next);
                        pos++;
                    } else if ((current == '/' && next == '/') || (current == '*' && next == '*')) {
                        sb.append(next);
                        pos++;
                    }
                }
                
                String tokenStr = sb.toString();
                tokens.add(AnalyseLexical.getToken(tokenStr));
                
            // SÉPARATEURS / PONCTUATION
            } else if ("()[]{},;:".indexOf(current) != -1) {
                String tokenStr = String.valueOf(current);
                tokens.add(AnalyseLexical.getToken(tokenStr));
                pos++;
                
            // COMMENTAIRES
            } else if (current == '#') {
                while (pos < input.length() && input.charAt(pos) != '\n') {
                    pos++;
                }
            } else {
                tokens.add("Erreur Lexicale : Caractère non reconnu '" + current + "'");
                pos++;
            }
        }
        tokens.add("#"); // Marqueur de fin de fichier (EOF)
        return tokens;
    }
   
    
}