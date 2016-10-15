import java.util.*;
import java.lang.*;

class Monitoreo {

    public void numero() {
        Random random = new Random();
        Vector<String> arreglo = new Vector<String>();
        String RST = "\u001B[0m", BLK = "\u001B[30m", RED = "\u001B[31m";
        String GRN = "\u001B[32m", YLL = "\u001B[33m", BLU = "\u001B[34m";
        String PRP = "\u001B[35m", CYN = "\u001B[36m", WHT = "\u001B[37m";

         

        int turno = 0, consumidor = 0, productor1 = 0, productor2 = 0;

        while ((productor1 < 20) || (productor2 < 20) || (arreglo.isEmpty() != true)) {

            switch (random.nextInt(3 - 1 + 1) + 1) {

                case 1:

                    if (arreglo.size() >= 20) {
                        turno++;
                        productor1++;
                    } else if (productor1 > 20) {
                        turno++;
                        productor1++;
                    } else {
                        char c = (char) (random.nextInt(26) + 'a');
                        String letra = String.valueOf(c);

                        arreglo.add(letra);
                        turno++;
                        productor1++;
                    }

                    break;

                case 2:

                    if (arreglo.size() >= 20) {
                        turno++;
                        productor2++;

                    } else if (productor2 > 20) {
                        turno++;
                        productor2++;
                    } else {
                        arreglo.add(Integer.toString(random.nextInt(9 - 0 + 1) + 0));
                        turno++;
                        productor2++;
                    }

                    break;

                case 3:

                    if (arreglo.isEmpty()) {
                        turno++;
                        consumidor++;
                    } else if (!arreglo.isEmpty()) {

                        int hey = arreglo.size();

                        arreglo.remove(hey - 1);

                        turno++;
                        consumidor++;
                    }

                    break;
            }

            System.out.print("\033c"); //LIMPIA CONSOLA Y BORRA HISTORIAL

            for (int i = 0; i < arreglo.size(); i++) {
                String elemento = arreglo.get(i);

                if (elemento.matches("0") || elemento.matches("1") || elemento.matches("2") || elemento.matches("3") || elemento.matches("4") || elemento.matches("5") || elemento.matches("6") || elemento.matches("7") || elemento.matches("8") || elemento.matches("9")) {
                    System.out.print(BLU + elemento + RST + " ");
                } else {
                    System.out.print(WHT + elemento + RST + " ");
                }
            }

            System.out.println("\n\nElementos: " + YLL + arreglo.size() + RST);

            System.out.println("\n\nTURNO: " + YLL + turno + RST + "\tCONSUMIDOR: " + RED + consumidor + RST + "\tPRODUCTOR1: " + WHT + productor1 + RST + "\tPRODUCTOR2: " + BLU + productor2 + RST);

            try {
                Thread.sleep(600);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
