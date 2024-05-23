package integrantes;

import baraja.Carta;
import utilidades.Util;

import java.util.ArrayList;
import java.util.Scanner;

public class Jugador {
    protected ArrayList <Carta> mano;
    protected int dinero;
    protected String nombre="integrantes.Jugador";

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

        while (!mano.isEmpty()) {
            mano.remove(0);
        }
    }

    /**
     * Menu para cuando ya ha jugado una ronda le preguntamos si quiere seguir con el resto de rondas
     */
    public void menuContinuar()
    {
        System.out.println();
        System.out.println("MENÚ OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> CONTINUAR JUGANDO");
        System.out.println("2--> FINALIZAR EL JUEGO");
        System.out.println("===========================================");
        System.out.println("Introduce el número de la opción deseada:");
    }

    /**
     * Menu cuando empieza apostando el jugador
     */
    public void decisionJugador()
    {
        System.out.println();
        System.out.println("MENÚ OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> VISUALIZAR MI MANO");
        System.out.println("2--> VISUALIZAR MESA");
        System.out.println("3--> APOSTAR");
        System.out.println("4--> PASAR");
        System.out.println("5--> ABANDONAR");
        System.out.println("===========================================");
        System.out.println("Introduce el número de la opción deseada:");
    }

    /**
     MENU CUANDO EMPIEZA LA MAQUINA APOSTANDO Y DEBE CONTERSTAR EL JUGADOR
     */
    public void contestaJugador()
    {
        System.out.println();
        System.out.println("MENÚ OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> VISUALIZAR MI MANO");
        System.out.println("2--> VISUALIZAR MESA");
        System.out.println("3--> IGUALAR");
        System.out.println("4--> SUBIR");
        System.out.println("5--> ABANDONAR");
        System.out.println("===========================================");
        System.out.println("Introduce el número de la opción deseada:");
    }

    /**
     * La maquina ha subido la apuesta
     */

    public void contestarSubida(){
        System.out.println();
        System.out.println("MENÚ OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> IGUALAR");
        System.out.println("2--> ABANDONAR");
        System.out.println("===========================================");
        System.out.println("Introduce el número de la opción deseada:");
    }

    /**
     * Contestamos all in maquina pero nuestro saldo es mayor que ese all in
     */
    public void contestarAllInDeMaquina()
    {
        System.out.println();
        System.out.println("MENÚ OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> VISUALIZAR MI MANO");
        System.out.println("2--> VISUALIZAR MESA");
        System.out.println("3--> IGUALAR");
        System.out.println("4--> ABANDONAR");
        System.out.println("===========================================");
        System.out.println("Introduce el número de la opción deseada:");
    }

    /**
     * Nos hace all in la maquina y nosotros no tenemos saldo suficiente para igualarlo --- OBLIGADO A HACER ALL IN
     */
    public void responderAllIn()
    {
        System.out.println();
        System.out.println("No tienes saldo suficiente para igualar la apuesta. Estas son tus opciones:");
        System.out.println();
        System.out.println("MENÚ OPCIONES");
        System.out.println("===========================================");
        System.out.println("1--> ALL IN");
        System.out.println("2--> ABANDONAR.");
        System.out.println("===========================================");
        System.out.println("Introduce el número de la opción deseada:");
    }

    public void mostrarCartasDinero(){
        System.out.println();
        System.out.println("===========================================");
        System.out.println("TUS CARTAS SON LAS SIGUIENTES:");
        System.out.println();
        for(int i = 0; i< mano.size();i++)
        {
            Carta cartaActual = mano.get(i);
            cartaActual.imprimir();
        }
        System.out.println();
        System.out.println("DINERO ACTUAL --> "+ getDinero());
        System.out.println("-------------------------------------------");
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
    public int subirApuesta(Scanner teclado, int apuestaMaquina)
    {
        int apuesta;
        boolean error;
        do
        {
            error = false;
            apuesta = Util.leerCantidad( teclado,getDinero());
            if(apuesta<=apuestaMaquina)
            {
                System.out.println("Debes introducir una cantidad superior a la del contrincante, que es [ "+apuestaMaquina+" ]\n");
                error=true;
            }
        }
        while(error);
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
        return igualar; /*Ya que es la apuesta que se ha igualado*/
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
    public void addDinero(int dinero){this.dinero+=dinero;}
    public ArrayList<Carta> getMano() {
        return mano;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public String getNombre() {
        return nombre;
    }
}
