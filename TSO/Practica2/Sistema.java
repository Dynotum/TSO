class Sistema{


	 public static void main(String[] args) {
		//Efecto de limpiado en consola (UNIX).
		System.out.print("\u001B[2J\u001B[H");
		Monitoreo monitor = new Monitoreo();
		monitor.numero();

	}
}