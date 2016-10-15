public class Proceso {
	String idProceso;
	int numeroInstrucciones;
	int prioridad;
	int error;
	int bloqueo;
	int estado;
	int quantumBloqueo;
	int posicionInstruccion;


		public void copia(Proceso obj){
		idProceso=obj.idProceso;
		numeroInstrucciones=obj.numeroInstrucciones;
		prioridad=obj.prioridad;
		error=obj.error;
		bloqueo=obj.bloqueo;
		quantumBloqueo = obj.quantumBloqueo;
		posicionInstruccion =obj.posicionInstruccion;
	}
		//public Proceso(){};
}
