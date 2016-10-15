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


    public GestorDeProcesos() throws IOException, InterruptedException {
        
        JFileChooser elegir = new JFileChooser();
        int opcion = elegir.showOpenDialog(elegir);
        
        nombreArchivo = elegir.getSelectedFile().getName();

        
        leerArchivo(nombreArchivo);

        Procesar();
    }


    
    public void leerArchivo(String archivo) throws FileNotFoundException{
	    FileReader procesosEntrada = new FileReader(archivo);
	    BufferedReader bufferLectura = new BufferedReader(procesosEntrada);
	    try {
			numeroDeProcesos = Integer.parseInt(bufferLectura.readLine().trim());
			algoritmo =Integer.parseInt(bufferLectura.readLine().trim());
	    
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
			
			bufferLectura.close();	
	    }
	    catch (NumberFormatException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
    }


    
    public void Procesar() throws IOException, InterruptedException{
    	
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
//////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) throws IOException, InterruptedException{
		//Memoria memoriaVirtual = new Memoria();/
    	GestorDeProcesos abrir = new GestorDeProcesos();
    }

}