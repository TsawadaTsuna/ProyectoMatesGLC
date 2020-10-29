import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class APN extends JPanel {
    GLC prods;
    ArrayList<String> trancisiones;

    public APN(){
        super();
        this.setPreferredSize(new Dimension(600,500));
        prods=new GLC();
        trancisiones=new ArrayList<>();
        generarTrancisiones();
    }

    public APN(GLC g){
        super();
        prods=g;
        this.setPreferredSize(new Dimension(600,500));
        trancisiones=new ArrayList<>();
        generarTrancisiones();
    }

    public void generarTrancisiones(){
        //┴ alt 8869   ε 949
        trancisiones.add((char)949+","+(char)945+"/"+prods.generadores.get(0)+(char)945);
        for(char g:prods.generadores) {
            Produccion actual = prods.producciones.get(g);
            for (Character[] c : actual.transformaciones) {
                if(c.length>1){
                    String tope="";
                    for (int i=1;i<c.length;i++){
                        tope+=c[i];
                    }
                    trancisiones.add(c[0]+","+g+"/"+tope);
                }else {
                    trancisiones.add(c[0]+","+g+"/"+(char)949);
                }
            }
        }
        trancisiones.add((char)949+","+(char)945+"/"+(char)949);

        System.out.println("Trancision q0 -> q1:");
        System.out.println(trancisiones.get(0));
        System.out.println();
        System.out.println("Trancisiones ciclo de q1:");
        for(int i=1;i<trancisiones.size()-1;i++){
            System.out.println(trancisiones.get(i));
        }
        System.out.println();
        System.out.println("Trancision q1 -> q2:");
        System.out.println(trancisiones.get(trancisiones.size()-1));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial",Font.PLAIN,30));
        g.drawOval(50,50,100,100);
        g.drawString("q0",80,100);
        g.drawOval(300,50,100,100);
        g.drawString("q1",330,100);
        g.drawOval(180,300,100,100);
        g.drawString("q2",210,350);

        g.setFont(new Font("Arial",Font.PLAIN,20));
        g.drawString("Simbolo inicial: "+(char)945,2,20);
        g.drawString(trancisiones.get(0),160,80);
        g.drawLine(150,100,300,100);
        g.drawLine(300,100,290,90);
        g.drawLine(300,100,290,110);
        g.drawString(trancisiones.get(trancisiones.size()-1),320,250);
        g.drawLine(350,150,280,350);
        g.drawLine(280,350,290,340);
        g.drawLine(280,350,290,360);

        g.drawLine(400,100,450,100);
        g.drawLine(450,100,450,20);
        g.drawLine(450,20,350,20);
        g.drawLine(350,20,350,50);
        g.drawLine(350,50,340,40);
        g.drawLine(350,50,360,40);
        for(int i=1;i<trancisiones.size()-1;i++){
            g.drawString(trancisiones.get(i),452,i*20);
        }
    }

}
