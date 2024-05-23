package integrantes;

import baraja.CalidadMano;
import baraja.Carta;
import baraja.Mano;
import baraja.ManoIncompleta;

import java.util.ArrayList;

public class Maquina {
    protected ArrayList<Carta> mano;
    protected int dinero;
    protected ArrayList<Carta> manoMesa; //Para poder comparrar la mano con la mesa y apostar
    protected String nombre = "Máquina";

    /**
     * Constructor para crear la maquina
     * @param dinero
     */
    public Maquina(int dinero) {
        mano = new ArrayList<Carta>();
        manoMesa = new ArrayList<Carta>();
        this.dinero = dinero;
    }
    public String getNombre(){
        return nombre;
    }
    public ArrayList<Carta> getMano() {
        return mano;
    }

    /**
     * Metodo para añadir carta
     * @param carta carta de la baraja añadir mano maquina
     * @return true: se ha podido añadir
     */
    public boolean addCarta(Carta carta) {
        mano.add(carta);
        return true;
    }

    /**
     * Metodo para añadir las cartas de la mesa
     * @param carta carta para añadir
     */
    public void addCartaMesaToMano(Carta carta) {
        manoMesa.add(carta);
    }

    /**
     * Metodo para vaciar la mano de la maquina
     */
    public void cleanMano() {
        while (!mano.isEmpty()) {
            mano.remove(0);
        }
    }
    public void cleanManoMesa() {
        while (!manoMesa.isEmpty()) {
            manoMesa.remove(0);
        }

    }

    /**
     * Metodo para añadir el dinero que ganen
     *
     * @param dinero el dinero ganado
     */
    public void addDinero(int dinero) {
        this.dinero += dinero;
    }
    /**
     * Metodo para apostar en las dos primeras rondas
     * @param numFase fase en la que esta la aprtida
     * @param apuesta la puesta que hay en la mesa
     * @return numero de la apuesta
     */
    public int obtenerCalidadMano(int numFase, int apuesta) {
        /*Vamos a utilizar el numfase para dividir el codigo (ciega o resto) */
        if (numFase == 1) {
            int valorApuesta = valorManoCiega(apuesta);
            dinero-=valorApuesta;
            if (valorApuesta < apuesta) { //iguala la apuesta del jugador
                dinero -= apuesta;
                return apuesta;
            }
            return valorApuesta;
        } else {
            if (manoMesa.size() != 5) { // mano mesa incompleta de ronda 2-3
                int[] valorApuesta = ManoIncompleta.valorManoSinMesaCompleta(mano, manoMesa);
                int dineroApostar = apostar(valorApuesta);
                if (dineroApostar < apuesta) { //iguala la apuesta del jugador
                    dinero -= apuesta;
                    return apuesta;
                }
                dinero -= dineroApostar;
                return dineroApostar;
            } else { //ronda 4
                int[] valorApuesta = Mano.detectarMano(mano, manoMesa);
                int dineroApostar = apostar(valorApuesta);
                if (dineroApostar < apuesta) { //iguala la apuesta del jugador
                    dinero -= apuesta;
                    return apuesta;
                }
                dinero -= dineroApostar;
                return dineroApostar;
            }
        }
    }
    public void mostrarCartasDinero(){
        System.out.println();
        System.out.println("===========================================");
        System.out.println("CARTAS DEL CONTRINCANTE:");
        System.out.println();
        for(int i = 0; i< mano.size();i++)
        {
            Carta cartaActual = mano.get(i);
            cartaActual.imprimir();
        }
        System.out.println();
        System.out.println("DINERO ACTUAL DEL CONTRINCANTE --> "+ getDinero());
    }
    private int apostar(int[] puntajeMano) {
        int apuesta = 0;
        switch (puntajeMano[0]) {
            case 8:
                if (puntajeMano[1] >= 10) {/*All in al ser la mano mas alta posible*/
                    int dineroApostar = dinero;
                    dinero -= dinero;
                    return dineroApostar;
                }
                apuesta += 250;
            case 7:
                apuesta += 100;
            case 6:
                apuesta += 75;
            case 5:
                apuesta += 50;
            case 4:
                apuesta += 35;
            case 3:
                apuesta += 25;
            case 2:
                apuesta += 15;
            case 1:
                apuesta += 10;
        }
        switch (puntajeMano[1]) { /*SI la carta alta es poderosa sumaremos mas a la apuesta*/
            case 1:
                apuesta += 50;
                break;
            case 13:
                apuesta += 50;
                break;
            case 12:
                apuesta += 50;
                break;
            case 11:
                apuesta += 50;
                break;
            case 10:
                apuesta += 50;
                break;
            case 9:
                apuesta += 50;
                break;
            case 8:
                apuesta += 50;
                break;
            case 7:
                apuesta += 50;
                break;
            case 6:
                apuesta += 50;
                break;
            case 5:
                apuesta += 50;
                break;
            case 4:
                apuesta+=5;
                break;
            case 3:
                apuesta+=5;
                break;
            case 2:
                apuesta+=5;
                break;
        }
        return apuesta;
    }

    private int valorManoCiega(int apuesta) { //apuesta de ese momento
        CalidadMano calidadMano = CalidadMano.MALA;

        if (mano.get(0).getPalo() == mano.get(1).getPalo()) {
            //Mismo color
            /*EN este caso no preguntaremos por los numeros, ya que nuestro principal objetivo sera ir a por color*/
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
            /*Como una mano perfecta es ya de por si alta, pues no vamos a preguntar*/
            calidadMano = CalidadMano.MUY_BUENA;
        }
        if (calidadMano == CalidadMano.MUY_BUENA) {
            return 100;
        }
        if (calidadMano == CalidadMano.BUENA) {
            return 50;
        }
        if (calidadMano == CalidadMano.REGULAR) {
            return 20;
        }
        return 10;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }
}