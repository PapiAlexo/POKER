import java.util.ArrayList;
import java.util.Arrays;


public class Maquina {
    protected ArrayList<Carta> mano;
    protected int dinero;
    protected ArrayList<Carta> manoMesa; //Para poder comparrar la mano con la mesa y apostar


    /**
     * Constructor para crear la maquina
     * @param dinero
     */
    public Maquina(int dinero) {
        mano = new ArrayList<Carta>();
        manoMesa= new ArrayList<Carta>();
        this.dinero = dinero;
    }


    public ArrayList<Carta> getMano() {
        return mano;
    }


    /**
     * Metodo para añadir carta
     * @param carta carta de la baraja añadir mano maquina
     * @return true: se ha podido añadir
     */
    public boolean addCarta(Carta carta){
        mano.add(carta);
        return  true;
    }


    /**
     * Metodo para añadir las cartas de la mesa
     *
     * @param carta carta para añadir
     */
    public void addCartaToMesa(Carta carta){
        manoMesa.add(carta);
    }


    /**
     * Metodo para vaciar la mano de la maquina
     */
    public void cleanMano(){
        for (int i=0;i<2;i++){
            mano.remove(0);
        }
    }


    /**
     * Metodo para añadir el dinero que gaen
     * @param dinero el dinero ganado
     */
    public void addDinero(int dinero){
        this.dinero+=dinero;
    }
    private void minusDinero( int dinero){
        this.dinero-=dinero;
    }
    /**
     * Metodo para apostar emn las dos primeras rondas
     * @param mesaDescubierta bandera para saber si se han descubierto nlas cartas de la mesa
     * @return numero de la apuesta
     */
    public int obtenerCalidadMano(boolean mesaDescubierta) {
        /*Vamos a utilizar el booleano para dividir el codigo (ciega o resto) */
        if (!mesaDescubierta){
            return valorManoCiega();
        }
        else {
            if (manoMesa.size()!=5){
                return valorManoSinMesaCompleta();
            }
            else{
                return Mano.detectarMano(mano,manoMesa);
            }
        }


    }
    private int valorManoCiega(){
        CalidadMano calidadMano = CalidadMano.MALA;


        /*Si la mesa no está descubierta apostaremos solo siguiendo nuestra mano (CIEGA)*/
        if (mano.get(0).getPalo() == mano.get(1).getPalo()) {
            //Mismo color
            /*EN este caso no prejuntaremos por los numeros, ya que nuesro principal objetivo sera ir a por color*/
            calidadMano = CalidadMano.BUENA;
        }
        if (mano.get(0).getPalo() != mano.get(1).getPalo() && mano.get(0).getNumero() != mano.get(1).getNumero()) {
            //Diferente  palo y diferente numero
            calidadMano = CalidadMano.REGULAR;
            if ((mano.get(0).getNumero() < 6 || mano.get(1).getNumero() < 6) && (mano.get(0).getNumero() != 1 && mano.get(1).getNumero() != 1)) {
                calidadMano = CalidadMano.MALA;
            }
        }
        if (mano.get(0).getNumero() == mano.get(1).getNumero() && (mano.get(0).getNumero() > 11 || mano.get(0).getNumero() == 1)) {
            //Pareja Real
            /*Como una mano perfecta es ya de porsi alta, pues no vamos a prejuntar*/
            calidadMano = CalidadMano.MUY_BUENA;
        }
        return switch (calidadMano){
            case MALA -> 1;
            case REGULAR -> 2;
            case BUENA -> 5;
            case MUY_BUENA -> 10;
        };
    }
    private int valorManoSinMesaCompleta(){
        if (detectarEscaleraColor()>0){ /*EscaleraColor 3*/
            return detectarEscaleraColor()+800;
        }
        if (detectarCartasIguales(4)>0){
            return detectarCartasIguales(4)+700;
        }
        if (detectarFull()>0){ /*Pendiente*/
            return detectarFull()+600;
        }
        if (detectarColor()>0){ /*Color 3*/
            return detectarColor()+500;
        }
        if (detectarEscalera()>0){ /*Escalera 4 */
            return detectarEscalera()+400;
        }
        if (detectarCartasIguales(3)>0){
            return detectarCartasIguales(3)+300;
        }
        if (detectarDoblePereja()>0){
            return detectarDoblePereja()+200;
        }
        if (detectarCartasIguales(2)>0){
            return detectarCartasIguales(2)+100;
        }
        return cartaAlta();
    }
    /**************** METODOS PARA DETECTAR LAS COMBINACIONES **************************************************************************************************************************************/
//    Estos metodos estan debilitados para las apuestas basicas
    private int detectarEscalera() {
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
        Arrays.sort(cartas);
        /*Comprobamos que hay escalera*/
        for (int i = 0; i < cartas.length - 4; i++){
            if (cartas[i + 1] == cartas[i] + 1 &&
                    cartas[i + 2] == cartas[i]+ 2 &&
                    cartas[i + 3] == cartas[i] + 3) {
                valorEscalera = cartas[i + 3];
            }
        }
        return valorEscalera;
    }


    private int detectarColor(){
        int contadorColor;
        if(mano.get(0).getPalo()==mano.get(1).getPalo()){ /*Comprobamos que las cartas de las manos sean iguales */
            /*Si son iguales vuscamos otras tres cartas del mismo color en la mesa*/
            contadorColor=2;
            for (int i=0;i<manoMesa.size()&&contadorColor<5;i++){/*Cuando hemos encontrado 5 cartas salimos del bucle*/
                if (manoMesa.get(i).getPalo() == mano.get(0).getPalo()) {
                    contadorColor++;
                }
            }
            return (contadorColor==3)?cartaAlta():0; /*Con este if devolvemos 0 si no se han encontrado las 3 cartas o el valor de la carta ams alta de la mano*/
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
            return (contadorColor==3)?mano.get(contadorVueltas-1).getNumero():0;
        }
    }
    private int cartaAlta(){return Math.max(mano.get(0).getNumero(), mano.get(1).getNumero()); /*Con math sacamos la carta de mayor valor de la mano*/}
    private int detectarEscaleraColor(){
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
                    cartas[i + 2] == cartas[i]+ 2 ) {
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
                if (contador==3)valorEscalera = cartas[i + 4];
            }
        }
        return valorEscalera;
    }
    private int detectarCartasIguales(int cuantoBuscar){
        int contadorNumeros;
        if(mano.get(0).getNumero()==mano.get(1).getNumero()){
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
    private int detectarDoblePereja(){/*todo dar una vukta 1-3*/
        int contadorNumeros;
        if(mano.get(0).getNumero()==mano.get(1).getNumero()&&buscarCartasIgualesEnMesa(2)){
            return mano.get(0).getNumero();
        }else if (buscarCartasIgualesEnMesa(2)) {
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
            return (contadorNumeros==4)?cartaAlta():0;
        }
    }
    private int detectarFull(){
        if (buscarCartasIgualesEnMesa(3)&&detectarCartasIguales(2)>0){
            return detectarCartasIguales(2);
        }
        else if (detectarCartasIguales(2)>0&&detectarCartasIguales(3)>0) {
            return cartaAlta();
        }else if (buscarCartasIgualesEnMesa(2)&&detectarCartasIguales(3)>0){
            return detectarCartasIguales(3);
        }
        return 0;
    }
    private boolean buscarCartasIgualesEnMesa(int numCartas){
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

    public int getDinero() {
        return dinero;
    }
}
