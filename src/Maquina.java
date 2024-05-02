
import java.util.ArrayList;

public class Maquina {
    protected ArrayList<Carta> mano;
    protected int dinero;
    protected ArrayList<Carta> manoMesa; //Para poder comparrar la mano con la mesa y apostar
    protected String nombre="Máquina";
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
    public void addDinero( int dinero){
        this.dinero+=dinero;
    }
    private void minusDinero( int dinero){
        this.dinero-=dinero;
    }
    /**
     * Metodo para apostar emn las dos primeras rondas
     * @param numFase fase en la que esta la aprtida
     * @param apuesta la puesta que hay en la mesa
     * @return numero de la apuesta
     */
    public int obtenerCalidadMano(int numFase, int apuesta) {
        /*Vamos a utilizar el booleano para dividir el codigo (ciega o resto) */
        if (numFase==0){
            int valorApuesta=valorManoCiega(apuesta);
            minusDinero(valorApuesta);
            return valorApuesta;
        }
        else {
            if (manoMesa.size()!=5){
                int[] valorApuesta= ManoIncompleta.valorManoSinMesaCompleta(mano,manoMesa);
                int dineroApostar = apostar(valorApuesta);
                if (dineroApostar<apuesta){
                    dinero-=apuesta;
                    return apuesta;
                }
                dinero-=dineroApostar;
                return dineroApostar;
            }
            else{
                int[] valorApuesta= Mano.deterctarMano(mano,manoMesa);
                int dineroApostar = apostar(valorApuesta);
                if (dineroApostar<apuesta){
                    dinero-=apuesta;
                    return apuesta;
                }
                dinero-=dineroApostar;
                return dineroApostar;
            }
        }

    }
    private int apostar(int[] puntajeMano){
        int apuesta=0;
        switch (puntajeMano[0]){
            case 8:
                if (puntajeMano[1]>=10){/*All in*/
                    int dineroApostar=dinero;
                    dinero-=dinero;
                    return dineroApostar;
                }
                apuesta+=250;
            case 7:
                apuesta+=100;
            case 6:
                apuesta+=75;
            case 5:
                apuesta+=50;
            case 4:
                apuesta+=35;
            case 3:
                apuesta+=25;
            case 2:
                apuesta+=15;
            case 1:
                apuesta+=10;
        }
        switch (puntajeMano[1]){ /*SI la carta alta es poderosa sumaremos mas a la apuesta*/
            case 1:
                apuesta+=50;
                break;
            case 13:
                apuesta+=50;
                break;
            case 12:
                apuesta+=50;
                break;
            case 11:
                apuesta+=50;
                break;
            case 10:
                apuesta+=50;
                break;
            case 9:
                apuesta+=50;
                break;
            case 8:
                apuesta+=50;
                break;
            case 7:
                apuesta+=50;
                break;
            case 6:
                apuesta+=50;
                break;
            case 5:
                apuesta+=50;
                break;
        }
        return apuesta;
    }
    private int valorManoCiega(int apuesta){
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
        if (calidadMano==CalidadMano.MUY_BUENA&&apuesta<10){
            return 100;
        }
        if (calidadMano==CalidadMano.BUENA&&apuesta<5){
            return 50;
        }
        if (calidadMano== CalidadMano.REGULAR&&apuesta<2){
            return 20;
        }
        return apuesta;
    }

    public int getDinero() {
        return dinero;
    }
}
