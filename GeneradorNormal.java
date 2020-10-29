import java.util.ArrayList;

public class GeneradorNormal {
    GLC produs;
    public GeneradorNormal(){
        produs=new GLC();
    }

    public GeneradorNormal(GLC g){
        produs=g;
    }

    public GLC fNCh(){
        int in=65;// A en ASCII
        GLC prods = produs;
        ArrayList<NGeneradoresCH> termimales=new ArrayList<>();
        for(Character n:prods.generadores){
            Produccion actual=prods.producciones.get(n);
            for (Character[] c:actual.transformaciones){
                //Recorrer para sustiruir los nuevos generadores
                for(int i=0;i<c.length;i++){
                    char ng;
                    if(c.length>1) {
                        for (int j = 0; j < termimales.size(); j++) {
                            ng = termimales.get(j).terminal;
                            if (ng == c[i]) {
                                c[i] = termimales.get(j).generador;
                            }
                        }
                    }
                }
                //recorrer para crear los nuevos generadores
                for(int k=0;k<c.length;k++){
                    if(c.length>1) {
                        if (c[k] > 90) {
                            while (prods.generadores.contains((char) in)) {
                                in++;
                            }
                            NGeneradoresCH g = new NGeneradoresCH(in, c[k]);
                            termimales.add(g);
                            in++;

                        }
                    }
                }
            }
        }
        for(NGeneradoresCH g:termimales) {
            prods.addProd(g.generador, new Produccion(new Character[]{g.terminal}));
        }
        //Checar transformacion para que si se reemplazen correctamente;

        for(Character n:prods.generadores) {
            Produccion actual = prods.producciones.get(n);
            for (Character[] c : actual.transformaciones) {
                if(c.length>2) {
                    for (int i = 0; i < c.length; i++) {
                        char ng;
                        for (int j = 0; j < termimales.size(); j++) {
                            ng = termimales.get(j).terminal;
                            if (ng == c[i]) {
                                c[i] = termimales.get(j).generador;
                            }
                        }
                    }
                }
            }
        }

        //Checar los de varias mayusculas
        //Revisar el metodo recursivo
        prods.quitarRepetidosFNCH(in);
        return prods;
        //┴ alt 193
    }

    public GLC fNG() {
        int in = 65;// A en ASCII
        GLC prods = produs;
        prods.fnG();
        return prods;
    }

}

class NGeneradoresCH{
    char generador;
    char terminal;

    public NGeneradoresCH(int g,int t){
        generador=(char)g;
        terminal=(char)t;
    }
}

