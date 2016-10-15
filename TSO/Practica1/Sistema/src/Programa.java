import java.io.*;
import java.util.Vector;

public class Programa {
	
	private int NumInstrucciones, NumDatos, DirInicialInstrucciones, DirInicialDatos;
	Vector<String> instruccion = new Vector<String>();
	Vector<String> datos = new Vector<String>();
		
	public Programa(String archivo) throws IOException{ //CONSTRUCTOR ARCHIVO TXT
		try {
		//System.out.println("entro a programa");
		    FileReader programaEntrada = new FileReader(archivo);
		    BufferedReader bufferLectura = new BufferedReader(programaEntrada); //lectura de archivo de 

		    NumInstrucciones = Integer.parseInt(bufferLectura.readLine().trim()); //primera linea del texto
		    NumDatos = Integer.parseInt(bufferLectura.readLine().trim()); // 2 linea
		    DirInicialInstrucciones = Integer.parseInt(bufferLectura.readLine().trim()); // 3era linea de txt
		    DirInicialDatos = Integer.parseInt(bufferLectura.readLine().trim());	//4arta linea txt
		    	
		    for(int i = 0; i < NumInstrucciones; i++){
		    	instruccion.add(bufferLectura.readLine().trim()); // agrego al vector las siguientes intrucciones con la cantidad de numeroInstru
		    }
		    
		    for(int i = 0; i < NumDatos; i++){
		    	datos.add(bufferLectura.readLine().trim()); // agrego el numero de datos al vector 
		    }
		    
		    bufferLectura.close();
		} catch(FileNotFoundException e){System.out.println("ERROR al CARGAR el programa");} 
	}
	


	protected void cargarProgramaMemoria(Memoria memoriaVirtual){ //// le mando el array de 1000 espacios lleno con 0s
		
		for(int i = 0; i < NumInstrucciones; i++){
			memoriaVirtual.escrituraMemoria((DirInicialInstrucciones+i),instruccion.get(i));//en el array guardo en la direccion 100| 1800			
			/*mando llamar metodo de escrituraMEMORIA del objeto memoriaVirtual de la clase Memoria
				escrituraMemoria	Escribira "celdaMemoria[direccion] = instruccion;"
			*/
	    }
	    
	    for(int i = 0; i < NumDatos; i++){
	    	memoriaVirtual.escrituraMemoria((DirInicialDatos+i),datos.get(i));
	    	/*mando llamar metodo de escrituraMEMORIA del objeto memoriaVirtual de la clase Memoria
				escrituraMemoria	Escribira "celdaMemoria[direccion] = instruccion;"
			*/
	    }
	}
	/*METODO donde se hace todo el PROCESO */
	public void Procesar(Memoria memoriaVirtual, Procesador microProcesador) throws IOException{  
		microProcesador.Ejecutar(memoriaVirtual, DirInicialInstrucciones); // direccion iinicial de intrucciones
	}
	
	public void imprimirMemoria(Memoria memoriaVirtual){
		for(int i = 0; i < 1000; i++){
	    	System.out.println(memoriaVirtual.lecturaMemoria(i));
	    }
	}
}
