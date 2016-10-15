
public class Memoria {
	
	static int tamanoMemoria=1000;
	private String[] celdaMemoria = new String[1000];
	
	public Memoria(){ //constructor por defecto Memoria
		for(int i=0; i<tamanoMemoria; i++){
			celdaMemoria[i] = "0000";
		} // llenar memoria de 1000 espacios con 0s
	}
	
	public String lecturaMemoria(int direccion){
		return celdaMemoria[direccion];
	}
	
	public void escrituraMemoria(int direccion, String instruccion){
		celdaMemoria[direccion] = instruccion;
		//System.out.println("Direccion : "+direccion+"Instruccion : "+instruccion);
	}
}
