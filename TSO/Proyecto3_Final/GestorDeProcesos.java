import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Collections;


import javax.swing.*;

public class GestorDeProcesos{
	    
    int numeroDeProcesos;
    int cantidadInstrucciones;
    int inicioInstrucciones;
    int algoritmo;
    int inicioDatos;
    static int ultimo = -1;
    Instruccion instruccion = new Instruccion();
    String instruccionString ;
    
   /* Instruccion instruccion = new Instruccion();
	String instruccionString ;*/
    
	String nombreArchivo;
	
	Vector<Proceso> Procesos = new Vector<Proceso>();
	Vector<Proceso> ProcesosListos = new Vector<Proceso>();
	Vector<Proceso> ProcesosBloqueados = new Vector<Proceso>();
	Vector<Proceso> ProcesosTerminados = new Vector<Proceso>();
	
	Proceso ProcesoEjecutando = new Proceso();
	
	Vector<String> instrucciones = new Vector<String>();
	Vector<String> datos = new Vector<String>();
	

	Interfaz Ventana = new Interfaz();
///////
	private int NumInstrucciones, NumDatos, DirInicialInstrucciones, DirInicialDatos;
Vector<String> contadorPrograma = new Vector<String>();
	Vector<String> registroInstrucciones = new Vector<String>();
	Vector<String> acumulador = new Vector<String>();
	Vector<Integer> saltosDeDireccion = new Vector<Integer>();	/////

    public GestorDeProcesos(Memoria memoriaVirtual) throws IOException, InterruptedException {
        
        JFileChooser elegir = new JFileChooser();
        int opcion = elegir.showOpenDialog(elegir);
        
        nombreArchivo = elegir.getSelectedFile().getName();

        
        leerArchivo(nombreArchivo);
        cargarProgramaMemoria(memoriaVirtual);
        Procesar(memoriaVirtual, DirInicialInstrucciones);
        ImprimirRegistros();

    }


    
    public void leerArchivo(String archivo) throws FileNotFoundException{
	    FileReader procesosEntrada = new FileReader(archivo);
	    BufferedReader bufferLectura = new BufferedReader(procesosEntrada);
	    try {
	    	algoritmo =Integer.parseInt(bufferLectura.readLine().trim());

			numeroDeProcesos = Integer.parseInt(bufferLectura.readLine().trim());
	    
	    	String dato;
		
	    		    		
			for(int i = 0; i < numeroDeProcesos; i++){
				Proceso procesoTemporal = new Proceso();

				dato = bufferLectura.readLine();
				
			    StringTokenizer procesosDelimitador = new StringTokenizer(dato,":");
			    
			    procesoTemporal.idProceso = procesosDelimitador.nextToken();
			    procesoTemporal.numeroInstrucciones = Integer.parseInt(procesosDelimitador.nextToken());
			    procesoTemporal.prioridad = Integer.parseInt(procesosDelimitador.nextToken());
			    procesoTemporal.error = Integer.parseInt(procesosDelimitador.nextToken());
			    procesoTemporal.bloqueo = Integer.parseInt(procesosDelimitador.nextToken());
			    procesoTemporal.estado = 1;
		    
			    
			    Procesos.add(procesoTemporal);
			    
			}


			///////////// AGREGUE AQUI EL PEDASO DEL OTRO CODIGO //////////////////////////////////////
		//	bufferLectura.readLine();

			NumInstrucciones = Integer.parseInt(bufferLectura.readLine().trim()); //primera linea del texto
		    NumDatos = Integer.parseInt(bufferLectura.readLine().trim()); // 2 linea
		    DirInicialInstrucciones = Integer.parseInt(bufferLectura.readLine().trim()); // 3era linea de txt
		    DirInicialDatos = Integer.parseInt(bufferLectura.readLine().trim());	//4arta linea txt
		    	
		    for(int i = 0; i < NumInstrucciones; i++){
		    	instrucciones.add(bufferLectura.readLine().trim()); // agrego al vector las siguientes intrucciones con la cantidad de numeroInstru
		    }

		    
		    for(int i = 0; i < NumDatos; i++){
		    	datos.add(bufferLectura.readLine().trim()); // agrego el numero de datos al vector 
		    }

    	

			///////////////////////////////////////////////////////////////////////////////////
			
			bufferLectura.close();	
	    }
	    catch (NumberFormatException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
    }

public void cargarProgramaMemoria(Memoria memoriaVirtual){ //// le mando el array de 1000 espacios lleno con 0s
		
		for(int i = 0; i < NumInstrucciones; i++){
			memoriaVirtual.escrituraMemoria((DirInicialInstrucciones+i),instrucciones.get(i));//en el array guardo en la direccion 100| 1800			
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

 public void Procesar(Memoria memoriaVirtual, int direccionInicio) throws IOException, InterruptedException{
    	
    	int quantum = 0;  
	    		
	    SalidaPantalla();	
	    		
	    PasarAListo();
	    	
	    SalidaPantalla();
	    		
	    while(ProcesosTerminados.size()<numeroDeProcesos){
	    	cargarProceso();
	    	quantum=0;

	    	while(ProcesoEjecutando.numeroInstrucciones>0 && quantum<20){

	    		SalidaPantalla();
	    		if(ProcesoEjecutando.error==ProcesoEjecutando.numeroInstrucciones){
	    			TerminarProceso();
	    			quantum = 0;
	    			continue;
	    		}
	    		if(ProcesoEjecutando.bloqueo==ProcesoEjecutando.numeroInstrucciones){
	    			BloquearProceso();
	    			continue;
	    		}
////////////////////////////////////////////////////////////////////////////////////////////////////////

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
//////////////////////////////////////////////////////////////////////////////////////////
				
				
	    		
	    		ProcesoEjecutando.numeroInstrucciones--;
	    		ProcesoEjecutando.posicionInstruccion++;
	    		quantum++;
	    		
	    		if(ProcesoEjecutando.numeroInstrucciones==0 || ProcesoEjecutando.error==ProcesoEjecutando.numeroInstrucciones){
	    			TerminarProceso();
	    			quantum = 0;
	    		}
	    				
	    		if(ProcesosBloqueados.size()>0){
	    			for(int i=0; i<ProcesosBloqueados.size(); i++){


	    				if(ProcesosBloqueados.get(i).quantumBloqueo>0){

	    						ProcesosBloqueados.get(i).quantumBloqueo--;////
		   						//SalidaPantalla();////						    					

	    				}
	    				 else if(ProcesosBloqueados.get(i).quantumBloqueo==0){
	    					
	    					DesbloquearProceso();
	    					SalidaPantalla();

	    					if(ProcesosListos.size()==1 && ProcesoEjecutando.estado==0){
	    						quantum = 19; 

	    					}
	    				}
	    			}
	    		}
	    		
	    		if(ProcesosListos.size()==0 && ProcesoEjecutando.estado==1){
	    			quantum=0;
	    		}
	    		
	    		if(ProcesosTerminados.size()==numeroDeProcesos && ProcesoEjecutando.estado==0){
	    			
	    			SalidaPantalla(); quantum=20 ;
	    		}
	    	}

	    	
	    }
	    
    }
    
    public void SalidaPantalla(){
    	
    	String cadenaSalida;
	    cadenaSalida="";
	    cadenaSalida+="NUEVO\t\tLISTO\t\tEJECUCION\t\tBLOQUEADO\t\tTERMINADO\n";
	    
	    for(int i=0; i<numeroDeProcesos;i++){
		
	    	if(Procesos.size()-i>0)
	    		cadenaSalida+=Procesos.get(i).idProceso +" "+ Procesos.get(i).numeroInstrucciones +" "+ Procesos.get(i).prioridad;
	    	else
	    		cadenaSalida+="\t";
			
	    	cadenaSalida+="\t";
			
	    	if(ProcesosListos.size()-i>0)
	    		cadenaSalida+=ProcesosListos.get(i).idProceso +" "+ ProcesosListos.get(i).numeroInstrucciones +" "+ ProcesosListos.get(i).prioridad;
	    	else
	    		if((i!=0 || ProcesoEjecutando.estado==0))
	    			cadenaSalida+="\t";
			
	    	cadenaSalida+="\t";
			
	    	if(ProcesoEjecutando.estado==1 && i==0)
	    		cadenaSalida+="\t"+ProcesoEjecutando.idProceso +" "+ ProcesoEjecutando.numeroInstrucciones+"\t";
	    	else
	    		cadenaSalida+="\t";
	    	
	    	if(ProcesoEjecutando.estado==0 && i==0 && ProcesosListos.size()>0)
	    		cadenaSalida+="\t";
	    	cadenaSalida+="\t";
			
	    	if(ProcesosBloqueados.size()-i>0)
	    		cadenaSalida+=ProcesosBloqueados.get(i).idProceso +" "+ ProcesosBloqueados.get(i).numeroInstrucciones+"\t"+"# "+ ProcesosBloqueados.get(i).quantumBloqueo;
	    	else
	    		cadenaSalida+="\t";
			
	    	cadenaSalida+="\t";
			
	    	if(ProcesosTerminados.size()-i>0)
	    		cadenaSalida+=ProcesosTerminados.get(i).idProceso +" "+ ProcesosTerminados.get(i).numeroInstrucciones;
	    	else
	    		cadenaSalida+="\t";
			
	    	cadenaSalida+="\n";


	    }
	    Ventana.dibujar(cadenaSalida);
    }
	
	
	public void cargarProceso(){
		Proceso procesoTemporal = new Proceso();
		
		procesoTemporal.idProceso = ProcesoEjecutando.idProceso;
	    procesoTemporal.numeroInstrucciones = ProcesoEjecutando.numeroInstrucciones;
	    procesoTemporal.prioridad = ProcesoEjecutando.prioridad;
	    procesoTemporal.error = ProcesoEjecutando.error;
	    procesoTemporal.bloqueo = ProcesoEjecutando.bloqueo;
	    procesoTemporal.quantumBloqueo = ProcesoEjecutando.quantumBloqueo;
	    procesoTemporal.posicionInstruccion = ProcesoEjecutando.posicionInstruccion;
	    procesoTemporal.estado = 1;
	    
	    if(ProcesosListos.size()>0){
	    	ProcesoEjecutando.idProceso = ProcesosListos.get(0).idProceso;
	    	ProcesoEjecutando.numeroInstrucciones = ProcesosListos.get(0).numeroInstrucciones;
	    	ProcesoEjecutando.prioridad = ProcesosListos.get(0).prioridad;
	    	ProcesoEjecutando.error = ProcesosListos.get(0).error;
	    	ProcesoEjecutando.quantumBloqueo = ProcesosListos.get(0).quantumBloqueo;
	    	ProcesoEjecutando.bloqueo = ProcesosListos.get(0).bloqueo;
	    	ProcesoEjecutando.posicionInstruccion = ProcesosListos.get(0).posicionInstruccion;
	    
	    	ProcesosListos.remove(0);
	    }
	    
	    if(ProcesoEjecutando.estado > 0)
	    	ProcesosListos.add(procesoTemporal);
	    
	    ProcesoEjecutando.estado = 1;
	}
	
	public void BloquearProceso(){
		Proceso procesoTemporal = new Proceso();
		
		procesoTemporal.idProceso = ProcesoEjecutando.idProceso;
	    procesoTemporal.numeroInstrucciones = ProcesoEjecutando.numeroInstrucciones;
	    procesoTemporal.prioridad = ProcesoEjecutando.prioridad;
	    procesoTemporal.error = ProcesoEjecutando.error;
	    procesoTemporal.bloqueo = ProcesoEjecutando.bloqueo;
	    procesoTemporal.posicionInstruccion = ProcesoEjecutando.posicionInstruccion;
	    procesoTemporal.estado = 1;
	    procesoTemporal.quantumBloqueo = 10;
	    
	    ProcesosBloqueados.add(procesoTemporal);
	    
	    if(ProcesosListos.size()>0){ 
		    ProcesoEjecutando.idProceso = ProcesosListos.get(0).idProceso;
		    ProcesoEjecutando.numeroInstrucciones = ProcesosListos.get(0).numeroInstrucciones;
		    ProcesoEjecutando.prioridad = ProcesosListos.get(0).prioridad;
		    ProcesoEjecutando.error = ProcesosListos.get(0).error;
		    ProcesoEjecutando.bloqueo = ProcesosListos.get(0).bloqueo;
		    ProcesoEjecutando.posicionInstruccion = ProcesosListos.get(0).posicionInstruccion;
		    
		    ProcesosListos.remove(0);
	    }
	    
	    else{
	    	ProcesoEjecutando.idProceso = "";
		    ProcesoEjecutando.numeroInstrucciones = 1000;
		    ProcesoEjecutando.prioridad = 0;
		    ProcesoEjecutando.error = 0;
		    ProcesoEjecutando.bloqueo = 0;
		    ProcesoEjecutando.quantumBloqueo = 0;
		    ProcesoEjecutando.estado = 0;
	    }
   
//System.out.print("\033c"); //LIMPIA CONSOLA Y BORRA HISTORIAL

	}
	
	public void DesbloquearProceso(){
		Proceso procesoTemporal = new Proceso();
		
		procesoTemporal.idProceso = ProcesosBloqueados.get(0).idProceso;
	    procesoTemporal.numeroInstrucciones = ProcesosBloqueados.get(0).numeroInstrucciones;
	    procesoTemporal.prioridad = ProcesosBloqueados.get(0).prioridad;
	    procesoTemporal.error = ProcesosBloqueados.get(0).error;
	    procesoTemporal.posicionInstruccion = ProcesosBloqueados.get(0).posicionInstruccion;
	    procesoTemporal.bloqueo = 0;
	    procesoTemporal.estado = 1;
	    procesoTemporal.quantumBloqueo = 0;
	    
	    ProcesosListos.add(procesoTemporal);
	    
	    ProcesosBloqueados.remove(0);
	       
	}
	
	public void TerminarProceso(){
		Proceso TemporalTerminado = new Proceso();
		TemporalTerminado.idProceso = ProcesoEjecutando.idProceso;
		TemporalTerminado.numeroInstrucciones = ProcesoEjecutando.numeroInstrucciones;
		TemporalTerminado.prioridad = ProcesoEjecutando.prioridad;
		TemporalTerminado.error = ProcesoEjecutando.error;
		TemporalTerminado.bloqueo = ProcesoEjecutando.bloqueo;
		TemporalTerminado.posicionInstruccion = ProcesoEjecutando.posicionInstruccion;
		ProcesosTerminados.add(TemporalTerminado);
				
		if(ProcesosListos.size()>0){
			ProcesoEjecutando.idProceso = ProcesosListos.get(0).idProceso;
		    ProcesoEjecutando.numeroInstrucciones = ProcesosListos.get(0).numeroInstrucciones;
		    ProcesoEjecutando.prioridad = ProcesosListos.get(0).prioridad;
		    ProcesoEjecutando.error = ProcesosListos.get(0).error;
		    ProcesoEjecutando.bloqueo = ProcesosListos.get(0).bloqueo;
		    ProcesoEjecutando.posicionInstruccion = ProcesosListos.get(0).posicionInstruccion;
		    
		    ProcesosListos.remove(0);
		}
		else
			ProcesoEjecutando.estado=0;
		
	}
	
	public void PasarAListo(){
		int numProcesos = Procesos.size();
		for(int j=0;j<numProcesos; j++){
			ProcesosListos.add(Procesos.get(j));
		}
			
		switch(algoritmo){
			case 1: ordenarprioridad();
	 						break;
			case 2: break;
			case 3: ordenarnuminst();
	 						break;

		}
//Vector
		Procesos.removeAllElements();
	}
	
	



	////////////////////////////
public void merge(Vector<Proceso> Lis) {
    		if (Lis.size() > 1) {

    	       	Vector<Proceso> izq = izquierda(Lis);
    	       	Vector<Proceso> der= derecha(Lis);
    	       	
    	        merge(izq);
    	        merge(der);
    	            
    	        merge(Lis, izq, der);
    	    }
    	}
    	    
    	public Vector<Proceso> izquierda(Vector<Proceso> Lis) {
    	    int tam1 = Lis.size() / 2;
    	    Vector<Proceso> izq = new Vector<Proceso>();
    	    for (int i =0; i < tam1; i++) {
    	    	Proceso aux=new Proceso();
    	        aux.copia(Lis.get(i));
    	        izq.add(aux);
    	    }
    	    return izq;
    	}
    	    
    	public Vector<Proceso> derecha(Vector<Proceso> Lis) {
    		int tam1 = Lis.size() / 2;
    	    int tam2 = Lis.size() - tam1;
    	    Vector<Proceso> der = new Vector<Proceso>();
    	        
    	    for (int i = 0; i < tam2; i++){
    	    	Proceso aux=new Proceso();
    	        aux.copia(Lis.get(i+tam1));
    	        der.add(aux);
    	    }
    	        
    	    return der;
    	}
    	    
    	private void merge(Vector<Proceso> lis, Vector<Proceso> izq, Vector<Proceso> der){
    		int i1 = 0;
    	    int i2 = 0;
    	        
    	    for (int i = 0; i < (izq.size()+der.size()); i++) {
    	    	if (i2 >= der.size() || (i1 < izq.size() && (izq.get(i1).prioridad < der.get(i2).prioridad)) ){
    	          	lis.get(i).copia(izq.get(i1));
    	           	i1++;
    	        } else {
    	           	lis.get(i).copia(der.get(i2));
    	            i2++;
    	        }
    	    }
    	}
    	
//************************************************************************************
        public void merge2(Vector<Proceso> Lis) {
    		if (Lis.size() > 1) {

    	       	Vector<Proceso> izq = izquierda2(Lis);
    	       	Vector<Proceso> der= derecha2(Lis);
    	       	
    	        merge2(izq);
    	        merge2(der);
    	            
    	        merge2(Lis, izq, der);
    	    }
    	}
    	    
    	public Vector<Proceso> izquierda2(Vector<Proceso> Lis) {
    	    int tam1 = Lis.size() / 2;
    	    Vector<Proceso> izq = new Vector<Proceso>();
    	    for (int i =0; i < tam1; i++) {
    	    	Proceso aux=new Proceso();
    	        aux.copia(Lis.get(i));
    	        izq.add(aux);
    	    }
    	    return izq;
    	}
    	    
    	public Vector<Proceso> derecha2(Vector<Proceso> Lis) {
    		int tam1 = Lis.size() / 2;
    	    int tam2 = Lis.size() - tam1;
    	    Vector<Proceso> der = new Vector<Proceso>();
    	        
    	    for (int i = 0; i < tam2; i++){
    	    	Proceso aux=new Proceso();
    	        aux.copia(Lis.get(i+tam1));
    	        der.add(aux);
    	    }
    	        
    	    return der;
    	}
    	    
    	private void merge2(Vector<Proceso> lis, Vector<Proceso> izq, Vector<Proceso> der){
    		int i1 = 0;
    	    int i2 = 0;
    	        
    	    for (int i = 0; i < (izq.size()+der.size()); i++) {
    	    	if (i2 >= der.size() || (i1 < izq.size() && (izq.get(i1).numeroInstrucciones < der.get(i2).numeroInstrucciones)) ){
    	          	lis.get(i).copia(izq.get(i1));
    	           	i1++;
    	        } else {
    	           	lis.get(i).copia(der.get(i2));
    	            i2++;
    	        }
    	    }
    	}
    	
    	public void ordenarprioridad(){
    		merge(ProcesosListos);
    	}
    	
    	public void ordenarnuminst(){
    		merge2(ProcesosListos);
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

			if(contadorPrograma.get(i).equals("100") && registroInstrucciones.get(i).equals("1800") && acumulador.get(i).equals("0234"))
				break;

			if(cont<2)
			System.out.println(contadorPrograma.get(i)+"\t"+registroInstrucciones.get(i)+"\t"+acumulador.get(i));
			
			


		}
	}


/////////////////////////////////////////////////////////////////////////////////////////   	
	public static void main(String[] args) throws IOException, InterruptedException{
		Memoria memoriaVirtual = new Memoria();
    	GestorDeProcesos abrir = new GestorDeProcesos(memoriaVirtual);
   		


    }

}