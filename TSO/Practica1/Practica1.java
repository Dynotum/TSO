import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.Desktop;



public class Practica1{
	Scanner sc = new Scanner(System.in);

	String nameFile = null;//nombre de el archivo que se va a leer
	int numeroInstru, numDatos, direcInicialInstru, direcInicialDatos; /*variables para almacenar las primeras 4 lineas*/

	Vector<String> instrucciones = new Vector<String>(); //vector donde se almacenan las instrucciones
	Vector<String> datos = new Vector<String>(); 		//vector donde se almacenaran los datos


	public  void readFile() {
	//Efecto de limpiado en consola (UNIX).
		System.out.print("\u001B[2J\u001B[H");
		System.out.println();
		

		System.out.print("Nombre de archivo: ");
		nameFile = sc.next();

		//Efecto de limpiado en consola (UNIX).
		System.out.print("\u001B[2J\u001B[H");
		System.out.println();


		FileReader lector = null;
		BufferedReader buffer = null;

		try{
			lector = new FileReader("/home/dyno/Documentos/Practica_SO/"+nameFile+".txt");
			buffer = new BufferedReader(lector);

		    numeroInstru = Integer.parseInt(buffer.readLine().trim()); // el metodo trim() nos ayuda a eliminar espacios!
			numDatos = Integer.parseInt(buffer.readLine().trim());	   // convierto String a entero del buffer donde esta el String
			direcInicialInstru = Integer.parseInt(buffer.readLine().trim());
			direcInicialDatos = Integer.parseInt(buffer.readLine().trim());

			for(int i = 0; i < numeroInstru; i++){
		    	instrucciones.add(buffer.readLine().trim()); // agrego al vector las siguientes intrucciones con la cantidad de numeroInstru
		    }
		    
		    for(int i = 0; i < numDatos; i++){
		    	datos.add(buffer.readLine().trim()); // agrego el numero de datos al vector 
		    }
		    
		    buffer.close();

		   creacionArchivo(); //llamar metodo creacionArchivo


		}catch (FileNotFoundException e) {
			System.out.println("No se encontro el archivo: " + nameFile);

		}catch(IOException e){
			System.out.println("Error al cargar archivo: " + nameFile);

		}finally{
			System.out.println("");
		}


	}

	public void creacionArchivo(){

		File archivo = null;
		BufferedWriter buffwrit= null;

		try{
		//crear un archivo donde se guardara el todos los datos actualizadoos
			archivo = new File("archivoFinal.txt");
			buffwrit = new BufferedWriter(new FileWriter(archivo));

			/*ESCRIBIR EN EL ARCHIVO archivoFinal*/
			buffwrit.write("ARCHIVO: "+nameFile); 
			buffwrit.write("\n\nNumero de instrucciones: "+numeroInstru);
			buffwrit.write("\nNumero de datos: "+numDatos);
			buffwrit.write("\nDireccion inicial de instrucciones: "+direcInicialInstru);
			buffwrit.write("\nDireccion inicial de datos: "+direcInicialDatos);
			buffwrit.write("\n\nInstrucciones");

			/*IMPRIMIR RESULTADO DE ARCHIVO*/
			System.out.println("ARCHIVO: "+nameFile);	
			System.out.println("\nNumero de instrucciones: "+numeroInstru); 
			System.out.println("Numero de datos: "+numDatos);
			System.out.println("Direccion inicial de instrucciones: "+direcInicialInstru);
			System.out.println("Direccion inicial de datos: "+direcInicialDatos);
			System.out.println("\nInstrucciones");

			/*IMPRIMIR INSTRUCCIONES*/
			/*ESCRIBIR EN EL ARCHIVO E IMPRIMIR RESULTADO A LA VEZ*/

			for(int i = 0; i< numeroInstru; i++){

				System.out.println((direcInicialInstru + i)+"\tCod: "+instrucciones.get(i).charAt(0)+"\tDireccion: "+instrucciones.get(i).substring(1, 4));
				buffwrit.write("\r\n"+(direcInicialInstru + i)+"\tCod: "+instrucciones.get(i).charAt(0)+"\tDireccion: "+instrucciones.get(i).substring(1, 4));
				/*Imprime y guarda en el archivo la direccion de la memoria y se incrementa i++, tomo el primer caracter de la instriccion para el cCodigo e imprimo los faltantes para Direccion*/
			}


			System.out.println("\nDatos");
			buffwrit.write("\r\n\r\nDatos");

			for(int i = 0; i< numDatos; i++){

				System.out.println((direcInicialDatos + i)+"\tCod: "+datos.get(i));
				buffwrit.write("\r\n"+(direcInicialDatos + i)+"\tCod: "+datos.get(i));
			}

			buffwrit.close();

			abrirArchivoResultado(archivo);


		}catch(IOException e){
			System.out.println("Error al cargar archivo: " + nameFile);

		}

	}

	public void abrirArchivoResultado(File archivo){

		System.out.print("\nDesea abrir el archivo creado : \n1) Si\n2) No\nRespuesta ----> ");
		String opcion = sc.next();

		if(opcion.matches("1")){

			try{
				Desktop dt = Desktop.getDesktop();
				dt.open(archivo);

			}catch(IOException e){
				System.out.println("Error al cargar archivo: " + nameFile);
			}
		}
	}


	public static void main(String[] args) {

		Practica1 leerArchivo = new Practica1();
		leerArchivo.readFile();
	}
}