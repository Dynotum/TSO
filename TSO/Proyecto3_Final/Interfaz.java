import java.util.Vector;

import javax.swing.*;

public class Interfaz extends JFrame{
	
    JTextArea textarea1;
    
  

	
    public Interfaz() {
    	this.setBounds(0,0,1000,420);
        setLayout(null);
        textarea1=new JTextArea();
        this.setLocationRelativeTo(null);
        textarea1.setBounds(10,10,960,350);
        add(textarea1);
        this.setVisible(true);
    }

    public void dibujar(String cadena){
    	textarea1.setText(cadena);
    	//System.out.println(cadena);
        try {
			Thread.sleep (700);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}