import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Procesador {
	
	Vector<String> contadorPrograma = new Vector<String>();
	Vector<String> registroInstrucciones = new Vector<String>();
	Vector<String> acumulador = new Vector<String>();
	Vector<Integer> saltosDeDireccion = new Vector<Integer>();
	
	public Procesador(){
        try {
        	File archivo = new File("archivo.txt");
            BufferedWriter bw;
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write("0000");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        try {
        	File memoriaSecundaria = new File("memoriaSecundaria.txt");
            BufferedWriter bufferEscrituraMemoria;
			bufferEscrituraMemoria = new BufferedWriter(new FileWriter(memoriaSecundaria));
			bufferEscrituraMemoria.write("");
	        bufferEscrituraMemoria.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static int ultimo = -1;
	Instruccion instruccion = new Instruccion();
	String instruccionString ;
	
	public void Ejecutar(Memoria memoriaVirtual, int direccionInicio) throws IOException{
		int posicion = direccionInicio;
		//System.out.println(posicion);
		instruccionString = memoriaVirtual.lecturaMemoria(posicion); //		return array celdaMemoria[posicion];


		while(!instruccionString.equals("0000")){ // mientras la direccion inicial no sea igual a 0000

			instruccion = obtenerInstruccion(instruccionString); /* ya tengo CodigoOperacion = '0';Operando =  "000"; del objeto Instruccion*/ 
			contadorPrograma.add(Integer.toString(posicion)); 
			registroInstrucciones.add(instruccionString);		


				switch(instruccion.CodigoOperacion){
					case '0': posicion = SaltodeDireccion(instruccion.Operando, posicion, memoriaVirtual); // guardar posicion de la intsOpenrando, posicion actual, y mandar array
					 break;
					case '1': CargaAC(instruccion.Operando, memoriaVirtual); 
					break;
					case '2': EscribeAC(instruccion.Operando, memoriaVirtual); 
					break;
					case '3': SumaAC(instruccion.Operando, memoriaVirtual); 
					break;
					case '4': RestaAC(instruccion.Operando, memoriaVirtual); 
					break;
					case '5': MultiplicacionAC(instruccion.Operando, memoriaVirtual); 
					break;
					case '6': DivisionAC(instruccion.Operando, memoriaVirtual); 
					break;
					case '7': LeeTXT(instruccion.Operando, memoriaVirtual);
					 break;
					case '8': EscribeTXT(instruccion.Operando, memoriaVirtual); 
					break;
					case '9': CopiaMem(instruccion.Operando, memoriaVirtual, posicion);
					 break;
				}
			posicion++;
			instruccionString = memoriaVirtual.lecturaMemoria(posicion);
		}
	}
	
	public Instruccion obtenerInstruccion(String instruccionStr){
		Instruccion instrucciontemporal = new Instruccion();
		instrucciontemporal.CodigoOperacion = instruccionStr.charAt(0);  //CodigoOperacion = '0';
		instrucciontemporal.Operando = instruccionStr.substring(1, 4);//		Operando =  "000";
		return instrucciontemporal;
	}
	
	public int SaltodeDireccion(String operando, int posicionAntigua, Memoria memoriaVirtual){
		acumulador.add(acumulador.get(ultimo));
		ultimo++;
		for(int i=0; i<saltosDeDireccion.size(); i++){
			if(saltosDeDireccion.get(i)==posicionAntigua){
				return posicionAntigua;
			}
		}
		saltosDeDireccion.add(posicionAntigua);
		return (Integer.parseInt(operando)-1);
	}
	
	public void CargaAC(String operando, Memoria memoriaVirtual){
		String valoraCargar = memoriaVirtual.lecturaMemoria(Integer.parseInt(operando));
		valoraCargar = llenarString(valoraCargar);
		acumulador.add(valoraCargar);
		ultimo++;
	}
	
	public void EscribeAC(String operando, Memoria memoriaVirtual){
		memoriaVirtual.escrituraMemoria(Integer.parseInt(operando), acumulador.get(ultimo));
		acumulador.add(acumulador.get(ultimo));
		ultimo++;
	}
	
	public void SumaAC(String operando, Memoria memoriaVirtual){
		String valoraCargar = memoriaVirtual.lecturaMemoria(Integer.parseInt(operando));
		if(acumulador.isEmpty()){
			valoraCargar = Integer.toString(0+Integer.parseInt(valoraCargar));
		}
		else{
			valoraCargar = Integer.toString(Integer.parseInt(acumulador.get(ultimo))+Integer.parseInt(valoraCargar));
		}
		valoraCargar = llenarString(valoraCargar);
		acumulador.add(valoraCargar);
		ultimo++;
	}
	
	public void RestaAC(String operando, Memoria memoriaVirtual){
		String valoraCargar = memoriaVirtual.lecturaMemoria(Integer.parseInt(operando));
		valoraCargar = Integer.toString(Integer.parseInt(acumulador.get(ultimo))-Integer.parseInt(valoraCargar));
		valoraCargar = llenarString(valoraCargar);
		acumulador.add(valoraCargar);
		ultimo++;
	}
	
	public void MultiplicacionAC(String operando, Memoria memoriaVirtual){
		String valoraCargar = memoriaVirtual.lecturaMemoria(Integer.parseInt(operando));
		valoraCargar = Integer.toString(Integer.parseInt(acumulador.get(ultimo))*Integer.parseInt(valoraCargar));
		valoraCargar = llenarString(valoraCargar);
		acumulador.add(valoraCargar);
		ultimo++;
	}
	
	public void DivisionAC(String operando, Memoria memoriaVirtual){
		String valoraCargar = memoriaVirtual.lecturaMemoria(Integer.parseInt(operando));
		valoraCargar = Integer.toString(Integer.parseInt(acumulador.get(ultimo))*Integer.parseInt(valoraCargar));
		valoraCargar = llenarString(valoraCargar);
		acumulador.add(valoraCargar);
		ultimo++;
	}
	
	public void LeeTXT(String operando, Memoria memoriaVirtual)throws IOException{
		FileReader archivoTexto = new FileReader("archivo.txt");
	    BufferedReader bufferLectura = new BufferedReader(archivoTexto);
	    int valorAlmacenado = Integer.parseInt(bufferLectura.readLine().trim());
	    bufferLectura.close();
	    String valoraCargar = Integer.toString(valorAlmacenado+Integer.parseInt(acumulador.get(ultimo)));
	    valoraCargar = llenarString(valoraCargar);
		acumulador.add(valoraCargar);
		ultimo++;
	}
	
	public void EscribeTXT(String operando, Memoria memoriaVirtual) throws IOException{
		File archivo = new File("archivo.txt");
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo));
		bw.write(acumulador.get(ultimo));
		bw.close();
		acumulador.add(acumulador.get(ultimo));
		ultimo++;
	}
	
	public void CopiaMem(String operando, Memoria memoriaVirtual, int posicion) throws IOException{
		File archivo = new File("memoriaSecundaria.txt");
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo,true)); //sobre escribir txt
        bw.append(Integer.toString(posicion)); //agrega al archivo
      
        for(int i=0; i<1000; i++){
        	String instruccionMemoria;
        	instruccionMemoria = memoriaVirtual.lecturaMemoria(i);
			if(!instruccionMemoria.equals("0000")){
				bw.append("\r\n"+i+"\t"+instruccionMemoria);
			}
		}
        bw.append("\r\n\r\n");
		bw.close();

		acumulador.add(acumulador.get(ultimo));
		ultimo++;
	}
	
	public String llenarString(String operando){
		switch(operando.length()){
			case 1: operando = "000"+operando; break;
			case 2: operando = "00"+operando; break;
			case 3: operando = "0"+operando; break;
		}
		return operando;
	}
	
	public void ImprimirRegistros(){
		int cont = 0;
		System.out.println("PC\tIR\tAcumulador");
		for(int i=0; i<=ultimo; i++){
			if(Integer.parseInt(contadorPrograma.get(i)) == 104)
				cont++;

			if(cont<2)
			System.out.println(contadorPrograma.get(i)+"\t"+registroInstrucciones.get(i)+"\t"+acumulador.get(i));
		}
	}
}
