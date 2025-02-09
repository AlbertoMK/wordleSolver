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
            words = new String[2309];
            Scanner s = new Scanner(new FileReader("words-en.txt"));
            int i = 0;
            while(s.hasNext()) {
                words[i] = s.nextLine();
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        general = new BitSet[26];
        for (int i = 0; i < 26; i++) {
            char letter = (char)('a' + i);
            BitSet b = new BitSet(words.length);
            for (int j = 0; j < words.length; j++) {
                String word = words[j];
                if(word.indexOf(letter) != -1) {
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
                    if(words[word].charAt(posLetter) == (char)('a' + character)) {
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

    public void printPossibleWords() {
        for (int i = possibleWords.nextSetBit(0); i >= 0; i = possibleWords.nextSetBit(i + 1)) {
            System.out.println(words[i]);
        }
    }

}
