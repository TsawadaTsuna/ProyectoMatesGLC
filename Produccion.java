import java.util.ArrayList;

public class Produccion {
    ArrayList<Character[]> transformaciones;

    public Produccion(String[] prods){
        transformaciones=new ArrayList<>();
        for (int i =1;i<prods.length;i++){
            char[] t=prods[i].toCharArray();
            Character[] chars = new Character[t.length];
            for(int j=0;j<chars.length;j++){
                chars[j]=t[j];
            }
            transformaciones.add(chars);
        }
    }

    public Produccion(ArrayList<Character[]> chars){
        transformaciones=chars;
    }
    public Produccion(Character[] chars){
        transformaciones=new ArrayList<>();
        transformaciones.add(chars);
    }

    public void sustProdcs(String[] prods){
        transformaciones=new ArrayList<>();
        for (int i =0;i<prods.length;i++){
            char[] t=prods[i].toCharArray();
            Character[] chars = new Character[t.length];
            for(int j=0;j<chars.length;j++){
                chars[j]=t[j];
            }
            transformaciones.add(chars);
        }
    }

    public void cambiarProd(int p,Character[] chars){
        if(p<transformaciones.size()){
            transformaciones.remove(p);
            transformaciones.add(p,chars);
        }
    }

}
