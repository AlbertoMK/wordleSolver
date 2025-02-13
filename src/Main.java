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

    public static void cambiarEstado(Solver solver, String resutado, String palabra) {
        for (int i = 0; i < resutado.length(); i++) {
            if(resutado.charAt(i) == 'b') {
                solver.setCorrect(i, palabra.charAt(i));
            } else if (resutado.charAt(i) == 'm') {
                solver.setIncorrect(i, palabra.charAt(i));
                solver.setNotIncluded(palabra.charAt(i));
            } else if (resutado.charAt(i) == 'r') {
                solver.setUnplaced(palabra.charAt(i));
                solver.setIncorrect(i, palabra.charAt(i));
            }
        }
    }


}
