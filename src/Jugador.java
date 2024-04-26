import utilidades.Util;

import java.util.ArrayList;
import java.util.Scanner;

public class Jugador {
    protected ArrayList <Carta> mano;
    protected int dinero;

    public Jugador( int dinero) {
        this.mano = new ArrayList<Carta>();
        this.dinero = dinero;
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
     * Metodo para vaciar la mano de la maquina
     */
    public void cleanMano(){
        for (int i=0;i<2;i++){
            mano.remove(0);
        }
    }

    public void menuContinuar()
    {
        System.out.println();
        System.out.println("MENU OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> CONTINUAR JUGANDO");
        System.out.println("2--> FINALIZAR EL JUEGO ");
        System.out.println("===========================================");
        System.out.println("Introduce el numero de la opcion deseada:");

    }
    public void decisionJugador()
    {
        System.out.println();
        System.out.println("MENU OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> VISUALIZAR MI MANO.");
        System.out.println("2--> VISUALIZAR MESA.");
        System.out.println("3--> APOSTAR.");
        System.out.println("4--> PASAR");
        System.out.println("5--> ABANDONAR.");
        System.out.println("===========================================");
        System.out.println("Introduce el numero de la opcion deseada:");


    }
    public void contesteaJugador()
    {
        System.out.println();
        System.out.println("MENU OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> VISUALIZAR MI MANO.");
        System.out.println("2--> VISUALIZAR MESA.");
        System.out.println("3--> IGUALAR.");
        System.out.println("4--> SUBIR");
        System.out.println("5--> ABANDONAR.");
        System.out.println("===========================================");
        System.out.println("Introduce el numero de la opcion deseada:");
    }
    public void contestarSubida(){
        System.out.println();
        System.out.println("MENU OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> IGUALAR");
        System.out.println("2--> ABANDONAR.");
        System.out.println("===========================================");
        System.out.println("Introduce el numero de la opcion deseada:");
    }
    public void decirAllIn()
    {
        System.out.println();
        System.out.println("No tienes saldo suficiente para igualar la apuesta. Estas son tus opciones:");
        System.out.println("MENU OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> ALL IN");
        System.out.println("2--> ABANDONAR.");
        System.out.println("===========================================");
        System.out.println("Introduce el numero de la opcion deseada:");
    }

    public void mostrarCartasDinero(){
        System.out.println();
        System.out.println("===========================================");
        System.out.println("TUS CARTAS SON LAS SIGUIENTES");
        for(int i = 0; i< mano.size();i++)
        {
            Carta cartaActual = mano.get(i);
            cartaActual.imprimir();
        }
        System.out.println("DINERO ACTUAL --> "+ getDinero());
    }

    /**
     * Metodo apostar, si hay que hacer AllIn da solo dos opciones o le deja apostar segun su saldo
     * @param teclado
     * @return
     */
    public int apostar(Scanner teclado)
    {
        int apuesta = Util.leerCantidad( teclado,getDinero());
        setDinero(getDinero()-apuesta); /*Restamos la cantidad apostada*/
        return apuesta;
    }

    /**
     * Metodo para calcular la diferencia de una subida e igualar esa subida
     * @param apuestaJugador
     * @param apuestaMaquina
     * @return
     */
    public int igualarSubida(int apuestaJugador, int apuestaMaquina)
    {
        int igualar = apuestaMaquina - apuestaJugador; /*Calculamos la cantidad que debe igualar*/
        setDinero(getDinero() - igualar); /*Restamos la cantidad*/
        return apuestaMaquina; /*Ya que es la apuesta que se ha igualado*/
    }

    /**
     * Metodo para igualar la apuesta de la maquina
     * @param apuestaMaquina
     * @return
     */
    public int igualar (int apuestaMaquina)
    {
        setDinero(getDinero()-apuestaMaquina);
        return apuestaMaquina;
    }
    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }
}
