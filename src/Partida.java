import utilidades.Util;
import java.util.ArrayList;

public class Partida {

    protected ArrayList <Carta> baraja;

    /**
     * Constructor de baraja que no recibe nada
     */
    public Partida() {
        baraja = new ArrayList<Carta>();
    }
    public void generarBaraja()
    {

        /*BANDERAS PARA VER SI SE HA COMPLETADO UN PALO CON SUS 13 CARTAS*/
        boolean picas = false;
        boolean corazones = false;
        boolean trebol = false;
        boolean diamantes = false;
        boolean existeCarta =false;
        do{ // BUCLE PARA LA CREACION DE LAS 52 CARTAS
            existeCarta=false; // con esto salen las 52
            int numCart = Util.getNumber(1,13); // numero aleatorio delos valores de la carta
            int numPalo = Util.getNumber(1,4); // numero aleatorio para escoger palo
            if(baraja.isEmpty()) // si esta vacia add la carta sea cual sea
            {
                baraja.add(new Carta(numCart,obtenerPalo(numPalo)));
            }
            else {
                for (int i=0; i<baraja.size();i++)  // Comprobamos si existe la carta
                {
                    Carta cartaActual = baraja.get(i);
                    if(cartaActual.getNumero()==numCart && cartaActual.getPalo()==obtenerPalo(numPalo))
                    {
                        existeCarta = true; // existe
                    }
                }
                if (!existeCarta) // si no existe carta, la creamos
                {
                    baraja.add(new Carta(numCart,obtenerPalo(numPalo)));
                }
            }
        } while(baraja.size()!=52); // mientras que la baraja no este llena
    }

    /**
     * Metodo para obtener el Palo dando un int
     * @param numPalo
     * @return Palo / null
     */
    public Palo obtenerPalo(int numPalo) {
        switch (numPalo)
        {
            case 1:
                return Palo.PICAS;
            case 2:
                return Palo.CORAZONES;
            case 3:
                return Palo.TREBOL;
            case 4:
                return Palo.DIAMANTES;
        }
        return null;
    }
    public boolean comprobarPaloCompleto(Palo palo)
    {
        int cont = 0;
        for (int j=0; j<baraja.size(); j++)
        {
            Carta cartaActual = baraja.get(j);
            if(palo == cartaActual.getPalo() )
            {
                cont++;
            }
        }
        return false;
    }

    /**
     * Metodo para preparar las manos de cartas de los parametros
     * @param jugador
     * @param maquina
     * @param mesa
     */
    public void inicarRonda(Jugador jugador, Maquina maquina, Mesa mesa)
    {
        if(baraja.size()<9) /* si es menor que 9 cartas necesarias para jugar una ronda entramos y vaciamos la baraja*/
        {
            while(baraja.size()!=0) /* Elimina las cartas hasta que baraja este vacia*/
            {
                baraja.remove(0);
            }
        }
        generarBaraja();
        /*CARTAS JUGADOR*/
        jugador.addCarta(repartirCarta());
        jugador.addCarta(repartirCarta());

        /*CARTAS MAQUINA*/
        maquina.addCarta(repartirCarta());
        maquina.addCarta(repartirCarta());

        /*CARTAS MESA*/
        for(int i = 0; i<5;i++) /*Add cinco cartas de la primera ronda*/
        {
            mesa.addCarta(repartirCarta());
        }
    }

    /**
     * Metodo para ver si jugador debe hacer All In
     * @param apuesta
     * @param saldo
     * @return
     */
    public boolean comprobarAllIn(int apuesta, int saldo)
    {
        if(apuesta>=saldo)
        {
            return true;
        }
        return false;
    }


    /**
     * Metodo repartir 1 carta
     * @return
     */
    public Carta repartirCarta()
    {
        Carta carta = baraja.get(0);
        baraja.remove(0);
        return carta;
    }

    /**
     * Comprueba quein ha ganado la partida
     * @param jugador jugador
     * @param maquina maquina
     * @return 1 Pierde el Jugador      2 Pierde la Maquina
     */
    public int comprobarGanador(Jugador jugador, Maquina maquina)
    {
        if (jugador.getDinero() == 0)
        {
            return 1; //pierde jugador
        }
        if(maquina.getDinero()== 0)
        {
            return 2; //pierde maquina
        }
        return 0; //empate
    }

    /**
     * Nos indica quien ha ganado la ronda
     * @param manoJugador cartas del jugador
     * @param manoMaquina cartas de la amquina
     * @param manoMesa cartas de la mesa
     * @return >0 Si gana el Jugador    <0 Si gana la maquina   0 Si es empate
     */
    public int quienGana(ArrayList<Carta> manoJugador,ArrayList<Carta> manoMaquina,ArrayList<Carta> manoMesa){
        int valorGanador=0;
        for (int i=0;i<2&&valorGanador==0;i++){
            if (Mano.deterctarMano(manoJugador,manoMesa)[i]>Mano.deterctarMano(manoMaquina,manoMesa)[i])valorGanador=1;
            if (Mano.deterctarMano(manoJugador,manoMesa)[i]<Mano.deterctarMano(manoMaquina,manoMesa)[i])valorGanador=-1;
        }
        return valorGanador;
    }
}
