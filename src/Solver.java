import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Solver {

    private String[] words;
    private BitSet[] general;
    private BitSet[][] correct;

    private BitSet possibleWords;

    public Solver() {
        try {
            words = new String[2311];
            Scanner s = new Scanner(new FileReader("words-en.txt"));
            int i = 0;
            while (s.hasNext()) {
                words[i] = s.nextLine();
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        general = new BitSet[26];
        for (int i = 0; i < 26; i++) {
            char letter = (char) ('a' + i);
            BitSet b = new BitSet(words.length);
            for (int j = 0; j < words.length; j++) {
                String word = words[j];
                if (word.indexOf(letter) != -1) {
                    b.set(j);
                }
            }
            general[i] = b;
        }

        correct = new BitSet[5][26];
        for (int posLetter = 0; posLetter < 5; posLetter++) {
            for (int character = 0; character < 26; character++) {
                BitSet b = new BitSet(words.length);
                for (int word = 0; word < words.length; word++) {
                    if (words[word].charAt(posLetter) == (char) ('a' + character)) {
                        b.set(word);
                    }
                }
                correct[posLetter][character] = b;
            }
        }

        possibleWords = new BitSet(words.length);
        possibleWords.set(0, words.length);
    }

    public void setCorrect(int pos, char letter) {
        possibleWords.and(correct[pos][letter - 'a']);
    }

    public void setIncorrect(int pos, char letter) {
        possibleWords.andNot(correct[pos][letter - 'a']);
    }

    public void setUnplaced(char letter) {
        possibleWords.and(general[letter - 'a']);
    }

    public void setNotIncluded(char letter) {
        possibleWords.andNot(general[letter - 'a']);
    }

    /**
     * Método auxiliar que, dada una palabra de intento y una solución,
     * genera un código numérico (entre 0 y 3^5-1) que representa el feedback.
     * Cada posición se codifica en base 3:
     * 2 -> Letra en posición correcta (verde)
     * 1 -> Letra presente en otra posición (amarillo)
     * 0 -> Letra ausente (gris)
     */
    private int getPattern(String guess, String solution) {
        int[] feedback = new int[5];
        boolean[] usedSolution = new boolean[5];

        // Primero: marcar letras correctas (verde)
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == solution.charAt(i)) {
                feedback[i] = 2;
                usedSolution[i] = true;
            }
        }

        // Segundo: marcar letras presentes en otra posición (amarillo)
        for (int i = 0; i < 5; i++) {
            if (feedback[i] == 0) { // no es verde
                for (int j = 0; j < 5; j++) {
                    if (!usedSolution[j] && guess.charAt(i) == solution.charAt(j)) {
                        feedback[i] = 1;
                        usedSolution[j] = true;
                        break;
                    }
                }
            }
        }

        // Codificar el feedback en un único entero (base 3)
        int code = 0;
        for (int i = 0; i < 5; i++) {
            code = code * 3 + feedback[i];
        }
        return code;
    }

    /**
     * Recomienda la palabra que maximiza la entropía esperada, es decir,
     * la que, en promedio, proporcionará mayor información para reducir el
     * conjunto de palabras posibles.
     */
    public String recommendWord() {
        double bestEntropy = -1;
        String bestGuess = null;
        int totalPossible = possibleWords.cardinality();

        // Se consideran todas las palabras del diccionario como posibles intentos.
        for (int i = 0; i < words.length; i++) {
            if (possibleWords.get(i)) {
                String guess = words[i];
                Map<Integer, Integer> patternCounts = new HashMap<>();

                // Simular feedback para cada posible solución
                for (int solIndex = possibleWords.nextSetBit(0); solIndex >= 0; solIndex = possibleWords.nextSetBit(solIndex + 1)) {
                    String solution = words[solIndex];
                    int pattern = getPattern(guess, solution);
                    patternCounts.put(pattern, patternCounts.getOrDefault(pattern, 0) + 1);
                }

                // Calcular la entropía para esta palabra candidata
                double entropy = 0.0;
                for (int count : patternCounts.values()) {
                    double p = (double) count / totalPossible;
                    entropy += -p * (Math.log(p) / Math.log(2)); // log en base 2
                }

                // Seleccionar la palabra con mayor entropía
                if (entropy > bestEntropy) {
                    bestEntropy = entropy;
                    bestGuess = guess;
                }
            }
        }
        return bestGuess;
    }

    public void printPossibleWords() {
        for (int i = possibleWords.nextSetBit(0); i >= 0; i = possibleWords.nextSetBit(i + 1)) {
            System.out.println(words[i]);
        }
    }

}
