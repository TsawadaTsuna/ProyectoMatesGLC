import javax.swing.*;
import java.io.*;
import java.util.*;

public class GLC {
    Hashtable<Character,Produccion> producciones;
    ArrayList<Character> generadores;

    public GLC(){
        producciones=new Hashtable<>();
        generadores=new ArrayList<>();
        try {
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(new JTextArea());
            if(res== JFileChooser.APPROVE_OPTION) {
                BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
                String l=br.readLine();
                while (l != null) {
                    String[] prod=l.split(" ");
                    generadores.add(prod[0].charAt(0));
                    producciones.put(prod[0].charAt(0),new Produccion(prod));
                    l = br.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GLC(char id, ArrayList<Character[]> prods){
        producciones=new Hashtable<>();
        generadores=new ArrayList<>();
        generadores.add(id);
        producciones.put(id,new Produccion(prods));
    }

    public GLC(GLC g){
        producciones=new Hashtable<>();
        generadores=new ArrayList<>();
        for(Character c:g.generadores){
            generadores.add(c);
            ArrayList<Character[]> actual=producciones.get(c).transformaciones;
            ArrayList<Character[]> nuevos=new ArrayList<>();
            Character[] chars;
            for (Character[] curr:actual){
                chars=new Character[curr.length];
                System.arraycopy(curr,0,chars,0,curr.length);
                nuevos.add(chars);
            }
            producciones.put(c,new Produccion(nuevos));
        }
    }

    public void eliminarRecIzq(){
        ArrayList<RelacinGI> alpha = new ArrayList<>();
        ArrayList<RelacinGI> beta = new ArrayList<>();
        //alpha y beta
        for(char g:generadores){
            Produccion actual=producciones.get(g);
            for(int i=0;i<actual.transformaciones.size();i++){
                if(actual.transformaciones.get(i)[0]==g){
                    alpha.add(new RelacinGI(i,g));
                }else{
                    beta.add(new RelacinGI(i,g));
                }
            }
        }
        ArrayList<NGeneradoresRepCH> arrAlpha=new ArrayList<>();
        //Generadores con producciones a borrar
        ArrayList<Character> gens=new ArrayList<>();
        for(RelacinGI r:alpha){
            if(!gens.contains(r.g))
                gens.add(r.g);
        }
        //borrar
        for(char g:gens){
            ArrayList<Integer> insBorrar=new ArrayList<>();
            Produccion actual = producciones.get(g);
            for(RelacinGI r:alpha){
                if(r.g==g)
                    insBorrar.add(r.ind);
            }
            insBorrar.sort(Collections.reverseOrder());
            for (int i:insBorrar){
                arrAlpha.add(new NGeneradoresRepCH(g,actual.transformaciones.get(i)));
                actual.transformaciones.remove(i);
            }
        }

        //Agregar simbolos a betas
        ArrayList<Integer> productores=new ArrayList<>();
        int sg=gens.size();
        int in=65;
        for(int j=0;j<sg;j++){
            while (generadores.contains((char) in)) {
                in++;
            }
            productores.add(in);
            in++;
        }
        for(int g=0;g<gens.size();g++) {
            Produccion actual=producciones.get(gens.get(g));
            int sa=actual.transformaciones.size();
            for(int i=0;i<sa;i++){
                Character[] c=actual.transformaciones.get(i);
                Character[] nc = new Character[c.length+1];
                System.arraycopy(c,0,nc,0,c.length);
                nc[nc.length-1]=(char)(int) productores.get(g);
                actual.transformaciones.add(nc);
            }

        }

        //Crear producciones nuevas
        for(int i=0;i<sg;i++) {
            generadores.add((char) (int) productores.get(i));
            ArrayList<Character[]> recurs = new ArrayList<>();
            for (NGeneradoresRepCH c : arrAlpha) {
                if(c.generador==gens.get(i)) {
                    Character[] nc = new Character[c.secuencia.length - 1];
                    Character[] ncr = new Character[c.secuencia.length];
                    System.arraycopy(c.secuencia, 1, nc, 0, nc.length);
                    System.arraycopy(c.secuencia, 1, ncr, 0, ncr.length - 1);
                    ncr[ncr.length - 1] = (char) (int) productores.get(i);
                    recurs.add(nc);
                    recurs.add(ncr);
                }
            }
            producciones.put((char)(int) productores.get(i), new Produccion(recurs));
        }

    }



    public void addProd(char id, Produccion prods){
        producciones.put(id,prods);
        generadores.add(id);
    }


    //Auxiliar de la fnCh
    //Elimina las cadenas de length > 2
    public void quitarRepetidosFNCH(int in){
        boolean reduccion=false;
        ArrayList<Reemplazos> remp=new ArrayList<>();
        ArrayList<NGeneradoresRepCH> nuevos=new ArrayList<>();
        for(char g:generadores){
            Produccion actual=producciones.get(g);
            for(int i=0;i<actual.transformaciones.size();i++){
                Character[] c=actual.transformaciones.get(i);
                if(c.length>2){
                    while (generadores.contains((char) in)) {
                        in++;
                    }
                    Character[] nc = new Character[c.length-1];
                    System.arraycopy(c,1,nc,0,c.length-1);
                    NGeneradoresRepCH ng=new NGeneradoresRepCH((char)in,nc);
                    char t=c[0];
                    c= new Character[2];
                    c[0]=t;
                    c[1]=(char)in;
                    remp.add(new Reemplazos(g,i,c));
                    nuevos.add(ng);
                    reduccion=true;
                }
            }
        }
        for (Reemplazos r:remp){
            Produccion actual=producciones.get(r.g);
            actual.cambiarProd(r.pos,r.c);
        }
        for(NGeneradoresRepCH ng: nuevos){
            generadores.add(ng.generador);
            producciones.put(ng.generador,new Produccion(ng.secuencia));
        }
        if(reduccion){
            quitarRepetidosFNCH(in);
        }
        if(!reduccion){
            return;
        }
    }

    public void generarArchivo(String name){
        try {
            PrintWriter pw=new PrintWriter(new FileWriter(name));
            for(char g:generadores){
                pw.print(g+" ");
                Produccion actual=producciones.get(g);
                for(Character[] c:actual.transformaciones){
                    for (int i=0;i<c.length;i++){
                        pw.print(c[i]);
                    }
                    pw.print(" ");
                }
                pw.println();
            }
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fnG(){
        //Genera que una produccion empieze con un generador de orden mayor
        int act=generadores.size();
        for (int j=0;j<act;j++){
            Produccion actual=producciones.get(generadores.get(j));
            for(int i=0;i<actual.transformaciones.size();i++){
                Character[] c=actual.transformaciones.get(i);
                if (c[0]>=65){
                    int order = generadores.indexOf(c[0]);
                    if(order>generadores.indexOf(generadores.get(j))){
                        sust(generadores.get(j),c);
                    }else if(order==generadores.indexOf(generadores.get(j))){
                        eliminarRecIzq();
                    }
                }else {
                    continue;
                }
            }
        }
        //hacer una sustitucion
        //aqui ya no alcanze a probar todas las producciones pero almenos sustituye una para que todas empiezen
        //por un terminal
        for(char g:generadores){
            Stack<Character> sustituto;//=new Stack<>();
            Produccion actual=producciones.get(g);
            int vueltas=actual.transformaciones.size();
            for(int i = vueltas-1; i>=0; i--){
                Character[] c=actual.transformaciones.get(i);
                if(c[0]<97){
                    sustituto=new Stack<>();
                    for (int j=c.length-1;j>0;j--){
                        sustituto.push(c[j]);
                    }
                    sustSt(sustituto,c[0]);
                    actual.transformaciones.remove(i);
                    Character[] nc=new Character[sustituto.size()];
                    for(int k=0;k<nc.length;k++){
                        nc[k]=sustituto.pop();
                    }
                    actual.transformaciones.add(nc);
                }
            }
        }

    }

    //Uso un stack para ayudarme con la sustitucion
    public void sustSt(Stack<Character> current, char g){
        Produccion actual=producciones.get(g);
        int vueltas=actual.transformaciones.size();
        for(int i=vueltas-1;i>=0;i--) {
            Character[] c = actual.transformaciones.get(i);
            for (int j=c.length-1;j>=0;j--){
                current.push(c[j]);
            }
            if(current.peek()>=97){
                return;
            }else {
                char n=current.pop();
                sustSt(current,n);
            }

        }
    }

    //sustituye en base al orden
    public void sust(char g, Character[] cambio){
        boolean change=false;
        Produccion actual=producciones.get(g);
        int pos=0;
        for(Character[] c:actual.transformaciones){
            if(Arrays.equals(c, cambio)){
                break;
            }
            pos++;
        }
        int currents=0;
        int order=-1;
        if(generadores.contains(cambio[0]))
            order = generadores.indexOf(cambio[0]);
        int ordActual = generadores.indexOf(g);
        if(order>ordActual){
            currents=actual.transformaciones.size();

            Produccion cambios=producciones.get(cambio[0]);
            for(Character [] c:cambios.transformaciones){
                Character[] ns=new Character[c.length+cambio.length-1];
                System.arraycopy(c,0,ns,0,c.length);
                System.arraycopy(cambio,1,ns,c.length,cambio.length-1);
                actual.transformaciones.add(ns);
            }
            actual.transformaciones.remove(pos);
            change=true;
        }else if(order==ordActual){
            eliminarRecIzq();
        }
        if(change){
            sust(g,actual.transformaciones.get(pos));
        }
    }
}

class NGeneradoresRepCH{
    char generador;
    Character[] secuencia;

    public NGeneradoresRepCH(char g,Character[] s){
        generador=g;
        secuencia=s;
    }
}

class Reemplazos{
    char g;
    int pos;
    Character[] c;

    public Reemplazos(char g, int pos, Character[] c) {
        this.g = g;
        this.pos = pos;
        this.c = c;
    }
}

class RelacinGI{
    int ind;
    char g;

    public RelacinGI(int i,char g){
        ind=i;
        this.g=g;
    }
}
