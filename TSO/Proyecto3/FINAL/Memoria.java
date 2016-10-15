public class Memoria {
	
	static int tamanoMemoria=1000;
	private String[] celdaMemoria = new String[1000];
	
	public Memoria(){
		for(int i=0; i<tamanoMemoria; i++){
			celdaMemoria[i] = "0000";
		}
	}
	
	public String lecturaMemoria(int direccion){
		return celdaMemoria[direccion];
	}
	
	public void escrituraMemoria(int direccion, String instruccion){
		celdaMemoria[direccion] = instruccion;
	}
}
