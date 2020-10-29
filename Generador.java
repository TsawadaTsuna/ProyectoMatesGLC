import javax.swing.*;

public class Generador extends JFrame {
    APN apn;

    public Generador(){
        super("APN");
        GLC g=new GLC();
        GeneradorNormal gn=new GeneradorNormal(g);
        gn.fNCh();
        g.generarArchivo("forma de Chomsky.txt");
        gn.fNG();
        g.generarArchivo("forma de Greibach.txt");
        apn=new APN(g);
        this.add(apn);
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            Generador g = new Generador();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Se ha ingresado un archivo invalido");
        }
    }
}
