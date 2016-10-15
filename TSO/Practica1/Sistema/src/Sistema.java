import java.io.IOException;
import java.util.Scanner;

public class Sistema {
	static Scanner read = new Scanner(System.in);

	static Memoria memoriaVirtual = new Memoria();  //clases que solo esta clase puede usar
	static Procesador microProcesador = new Procesador();// ""	"
	
	public static void main(String[] args) throws IOException{
		
		String nombreArchivoPrograma;
			//Efecto de limpiado en consola (UNIX).
		System.out.print("\u001B[2J\u001B[H");
		System.out.println();
		
		System.out.print("Ingrese el nombre del archivo del programa a cargar: ");
		nombreArchivoPrograma = read.nextLine();

		Programa programaEjecutar = new Programa(nombreArchivoPrograma); //crear objeto de la clase Programa

		programaEjecutar.cargarProgramaMemoria(memoriaVirtual);//usar objeto de la clase Programa y al constructor le mando el archivo
		/*	llenar memoria de 1000 espacios con 0s */

		programaEjecutar.Procesar(memoriaVirtual, microProcesador);


		microProcesador.ImprimirRegistros();
	}
}
