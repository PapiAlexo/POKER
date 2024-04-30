import java.util.ArrayList;
import java.util.Arrays;


public class Mano {
    public static int[] deterctarMano(ArrayList<Carta> mano, ArrayList<Carta> manoMesa){
        int[]valorMano= {0,0};
        if (detectarEscaleraColor(mano, manoMesa)>0){
            valorMano[0]=8;
            valorMano[1]=detectarEscaleraColor(mano, manoMesa);
            return valorMano;
        }
        if (detectarCartasIguales(mano, manoMesa,4)>0){
            valorMano[0]=7;
            valorMano[1]=detectarCartasIguales(mano, manoMesa,4);
            return valorMano;
        }
        if (detectarFull(mano, manoMesa)>0){
            valorMano[0]=6;
            valorMano[1]=detectarFull(mano, manoMesa);
            return valorMano;
        }
        if (detectarColor(mano, manoMesa)>0){
            valorMano[0]=5;
            valorMano[1]=detectarColor(mano, manoMesa);
            return valorMano;
        }
        if (detectarEscalera(mano, manoMesa)>0){
            valorMano[0]=4;
            valorMano[1]=detectarEscalera(mano, manoMesa);
            return valorMano;
        }
        if (detectarCartasIguales(mano, manoMesa,3)>0){
            valorMano[0]=3;
            valorMano[1]=detectarCartasIguales(mano, manoMesa,3);
            return valorMano;
        }
        if (detectarDoblePereja(mano, manoMesa)>0){
            valorMano[0]=2;
            valorMano[1]=detectarDoblePereja(mano, manoMesa);
            return valorMano;
        }
        if (detectarCartasIguales(mano, manoMesa,2)>0){
            valorMano[0]=1;
            valorMano[1]=detectarCartasIguales(mano, manoMesa,2);
            return valorMano;
        }
        valorMano[1]=cartaAlta(mano, manoMesa);
        return valorMano;
    }




    private static int detectarEscalera(ArrayList<Carta> mano,ArrayList<Carta> manoMesa) {
        int[] cartas = new int[manoMesa.size() + mano.size()]; /*Guardar valores de las cartas de la mano y de la mesa*/
        /*Llenamos la array*/
        boolean repetido= false;
        for (int i = 0; i < mano.size(); i++) {
            cartas[i] = mano.get(i).getNumero(); // insertamos cada carta de la mano en el array
        }
        for (int i = mano.size(); i < manoMesa.size() + mano.size(); i++) {
            repetido=false;
            for (int j=0;j<cartas.length;j++){
                if (manoMesa.get(i-mano.size()).getNumero() == cartas[j]) {
                    repetido=true;
                }
            }
            if(!repetido){
                cartas[i] = manoMesa.get(i - mano.size()).getNumero(); // insertamos cada carta de la messa en el array acontinuacion de las de la mesa
            }
        }
        int valorEscalera = 0;
        /*Ordenamos la Array*/
        Arrays.sort(cartas);/*Ordena de forma ascendente*//*@Override*/
        /*Comprobamos que hay escalera*/
        for (int i = 0; i < cartas.length - 4; i++){     /*Ej: 3,4,5,6,7*/
            if (cartas[i + 1] == cartas[i] + 1 &&        /* 4 == 3+1 */
                    cartas[i + 2] == cartas[i]+ 2 &&     /* 5 == 3+2 */
                    cartas[i + 3] == cartas[i] + 3 &&    /* 6 == 3+3 */
                    cartas[i + 4] == cartas[i] + 4) {    /* 7 == 3+4 */
                valorEscalera = cartas[i + 4];
            }
        }
        return valorEscalera;
    }


    private static int detectarColor(ArrayList<Carta> mano,ArrayList<Carta> manoMesa){
        int contadorColor;
        if(mano.get(0).getPalo()==mano.get(1).getPalo()){ /*Comprobamos que las cartas de las manos sean iguales */
            /*Si son iguales vuscamos otras tres cartas del mismo color en la mesa*/
            contadorColor=2;
            for (int i=0;i<manoMesa.size()&&contadorColor<5;i++){/*Cuando hemos encontrado 5 cartas salimos del bucle*/
                if (manoMesa.get(i).getPalo() == mano.get(0).getPalo()) {
                    contadorColor++;
                }
            }
            return (contadorColor==5)?cartaAlta(mano,manoMesa):0; /*Con este if devolvemos 0 si no se han encontrado las 3 cartas o el valor de la carta ams alta de la mano*/
        }else{
            /*Si las cartas de la mano son diferentes cogemos la primera carta y buscamos las 4 que nos faltan y si no se han encontrado buscamos con la siguiente carta*/
            contadorColor=1;
            int contadorVueltas=0;/*Con tesa variable sabemos cuando hemos salido del bucle para saber que cartas sde la mesa hemos utilizado para averiguar el color*/
            for (int i = 0; i <mano.size()&&contadorColor<5; i++) {
                contadorVueltas++;
                contadorColor=1;
                for (int j = 0; j < manoMesa.size(); j++) {
                    if (manoMesa.get(j).getPalo() == mano.get(i).getPalo()) {
                        contadorColor++;
                    }
                }
            }
            return (contadorColor==5)?mano.get(contadorVueltas-1).getNumero():0;
        }
    }
    private static int cartaAlta(ArrayList<Carta> mano,ArrayList<Carta> manoMesa){return Math.max(mano.get(0).getNumero(), mano.get(1).getNumero()); /*Con math sacamos la carta de mayor valor de la mano*/}
    private static int detectarEscaleraColor(ArrayList<Carta> mano,ArrayList<Carta> manoMesa){
        int[] cartas = new int[manoMesa.size() + mano.size()]; /*Guardar valores de las cartas de la mano y de la mesa*/
        /*Llenamos la array*/
        boolean repetido= false;
        for (int i = 0; i < mano.size(); i++) {
            cartas[i] = mano.get(i).getNumero(); // insertamos cada carta de la mano en el array
        }
        for (int i = mano.size(); i < manoMesa.size() + mano.size(); i++) {
            repetido=false;
            /*Como ahora solo nos importa el numero de la carta si hay alguna repetida no se introduce en la array*/
            for (int j=0;j<cartas.length;j++){
                if (manoMesa.get(i-mano.size()).getNumero() == cartas[j]) {
                    repetido=true;
                }
            }
            if(!repetido){
                cartas[i] = manoMesa.get(i - mano.size()).getNumero(); // insertamos cada carta de la mesa en el array acontinuacion de las de la mesa
            }
        }
        int valorEscalera = 0;
        /*Ordenamos la Array*/
        Arrays.sort(cartas);/*Ordena de forma ascendente*//*@Override*/
        /*Comprobamos que hay escalera*/
        for (int i = 0; i < cartas.length - 4; i++){     /*Ej: 3,4,5,6,7*/
            if (cartas[i + 1] == cartas[i] + 1 &&        /* 4 == 3+1 */
                    cartas[i + 2] == cartas[i]+ 2 &&     /* 5 == 3+2 */
                    cartas[i + 3] == cartas[i] + 3 &&    /* 6 == 3+3 */
                    cartas[i + 4] == cartas[i] + 4) {
                Palo paloEscalera=Palo.DIAMANTES; /*Creamos una variable que nos guada el palo de la escalera*/
                for (int g=i;g<cartas.length;g++){ /*i es la primera carta de la escalera e igualamos a g para poder buscar en la array de cartas*/
                    for (int k = 0; k < mano.size(); k++) { /*Buscamos tanbien en la array de la mano*/
                        if (cartas[g]==mano.get(k).getNumero()){ /*Si los numeros coinciden es que es la misma carta y guardamos en la variable paloEscalera dicho palo*/
                            paloEscalera=mano.get(k).getPalo();
                        }
                    }
                }
                int contador=0;
                for (int j=i;j<i+5;j++){ /*LO iniciamos a i y lo terminamos en 5 numeros mayores paar estar en el rango de la Escalera*/
                    /*COn estos dos bucles buscamos el color, si lo enocntramos sumamos uno al contador y si este llega a 5 salimos y se habra detectado la escalera de color*/
                    for (int k = 0; k < mano.size(); k++) {
                        if (cartas[j]==mano.get(k).getNumero()&&mano.get(k).getPalo()==paloEscalera){
                            contador++;
                        }
                    }
                    for (int k=0;k<manoMesa.size();k++){
                        if (manoMesa.get(k).getNumero()==cartas[j]&&manoMesa.get(k).getPalo()==paloEscalera) {
                            contador++;
                        }
                    }
                }
                if (contador==5)valorEscalera = cartas[i + 4];
            }
        }
        return valorEscalera;
    }
    private static int detectarCartasIguales(ArrayList<Carta> mano,ArrayList<Carta> manoMesa,int cuantoBuscar){/*En este caso necesitaremos recibir el numero de cartas iguales que buscar, ya que este metodo nos sirve para poker(4),trio(3) y pareja(2)*/
        int contadorNumeros;
        if(mano.get(0).getNumero()==mano.get(1).getNumero()){/*Al igual que en los metodos anteriores separamos los casos*/
            contadorNumeros=2;
            for (int i=0;i<manoMesa.size();i++){
                if (manoMesa.get(i).getNumero() == mano.get(0).getNumero()) {
                    contadorNumeros++;
                }
            }
            return(contadorNumeros==cuantoBuscar)?mano.get(0).getNumero():0;
        }else{
            contadorNumeros=1;
            int contadorVueltas=0;/*Con tesa variable sabemos cuando hemos salido del bucle para saber que cartas sde la mesa hemos utilizado para averiguar el color*/
            for (int i = 0; i <mano.size()&&contadorNumeros!=cuantoBuscar; i++) {
                contadorVueltas++;
                contadorNumeros=1;
                for (int j = 0; j < manoMesa.size(); j++) {
                    if (manoMesa.get(j).getNumero() == mano.get(i).getNumero()) {
                        contadorNumeros++;
                    }
                }
            }
            return (contadorNumeros==cuantoBuscar)?mano.get(contadorVueltas-1).getNumero():0;
        }
    }
    private static int detectarDoblePereja(ArrayList<Carta> mano,ArrayList<Carta> manoMesa){
       /*En este caso contamos con tres posibles casos, uno en el que tenemos una pareja en la mano y otra en la mesa(caso visto en el if), otro en el que temngo una carta en la mano,
       su pareja en la mesa y otra pareja en la mesa (caso else if) y por ultimno el caso de tener una carta de cada pareja en la mano (else)*/
        int contadorNumeros;
        if(mano.get(0).getNumero()==mano.get(1).getNumero()&&buscarCartasIgualesEnMesa(mano,manoMesa,2)){
            return mano.get(0).getNumero();
        }else if (buscarCartasIgualesEnMesa(mano,manoMesa,2)) {
            contadorNumeros=1;
            int contadorVueltas=0;/*Con tesa variable sabemos cuando hemos salido del bucle para saber que cartas sde la mesa hemos utilizado para averiguar el color*/
            for (int i = 0; i <mano.size()&&contadorNumeros!=2; i++) {
                contadorVueltas++;
                contadorNumeros=1;
                for (int j = 0; j < manoMesa.size(); j++) {
                    if (manoMesa.get(j).getNumero() == mano.get(i).getNumero()) {
                        contadorNumeros++;
                    }
                }
            }
            return (contadorNumeros==2)?mano.get(contadorVueltas-1).getNumero():0;
        }
        else{
            contadorNumeros=0;
            int contadorVueltas=0;/*Con tesa variable sabemos cuando hemos salido del bucle para saber que cartas sde la mesa hemos utilizado para averiguar el color*/
            for (int i = 0; i <mano.size()&&contadorNumeros!=4; i++) {
                contadorVueltas++;
                contadorNumeros++;
                for (int j = 0; j < manoMesa.size(); j++) {
                    if (manoMesa.get(j).getNumero() == mano.get(i).getNumero()) {
                        contadorNumeros++;
                    }
                }
            }
            return (contadorNumeros==4)?cartaAlta(mano,manoMesa):0;
        }
    }
    private static int detectarFull(ArrayList<Carta> mano,ArrayList<Carta> manoMesa){
        if (buscarCartasIgualesEnMesa(mano,manoMesa,3)&&detectarCartasIguales(mano,manoMesa,2)>0){
            return detectarCartasIguales(mano,manoMesa,2);
        }
        else if (detectarCartasIguales(mano,manoMesa,2)>0&&detectarCartasIguales(mano,manoMesa,3)>0) {
            return cartaAlta(mano,manoMesa);
        }else if (buscarCartasIgualesEnMesa(mano,manoMesa,2)&&detectarCartasIguales(mano,manoMesa,3)>0){
            return detectarCartasIguales(mano,manoMesa,3);
        }
        return 0;
    }
    private static boolean buscarCartasIgualesEnMesa(ArrayList<Carta> mano,ArrayList<Carta> manoMesa,int numCartas){
        /*buscamos el numeor de cartas que querramos iguales, pero solo en la mesa*/
        int contadorCarta;
        for (int i=0;i<(manoMesa.size()-1);i++){
            contadorCarta=1;
            for (int j=i+1;j<manoMesa.size();j++){
                if (manoMesa.get(i).getNumero()==manoMesa.get(j).getNumero()){
                    contadorCarta++;
                    if (contadorCarta==numCartas)return true;
                }
            }
        }
        return false;
    }
}
