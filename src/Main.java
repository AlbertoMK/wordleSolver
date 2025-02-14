import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Solver solver = new Solver();
        do {
            String palabra = solver.recommendWord();
            System.out.println("Next word: " + palabra);
            System.out.print("Resultado (b, m, r): "); //bien, mal, regular
            String resultado = s.nextLine();
            cambiarEstado(solver, resultado, palabra);
        } while(true);
    }

    public static void cambiarEstado(Solver solver, String resultado, String palabra) {
        for (int i = 0; i < resultado.length(); i++) {
            if(resultado.charAt(i) == 'b') {
                solver.setCorrect(i, palabra.charAt(i));
            } else if (resultado.charAt(i) == 'm') {
                solver.setIncorrect(i, palabra.charAt(i));
                if(!couldBeIncluded(palabra.charAt(i), resultado, palabra))
                    solver.setNotIncluded(palabra.charAt(i));
            } else if (resultado.charAt(i) == 'r') {
                solver.setUnplaced(palabra.charAt(i));
                solver.setIncorrect(i, palabra.charAt(i));
            }
        }
    }

    private static boolean couldBeIncluded(char letter, String resultado, String palabra) {
        for (int i = 0; i < palabra.length(); i++) {
            if(palabra.charAt(i) == letter && (resultado.charAt(i) == 'r' || resultado.charAt(i) == 'b'))
                return true;
        }
        return false;
    }
}
