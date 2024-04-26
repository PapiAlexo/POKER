import utilidades.Util;

import java.util.Scanner;
public class Main2 {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        Partida partida = new Partida(); // inicamos la partida
        Mesa mesa = new Mesa(0);
        Maquina maquina = new Maquina(1000000);
        Jugador jugador = new Jugador(1000000);
        int turno = Util.getNumber(1, 2); /*Para ver quien empieza primero, 1--> maquina 2--> Jugador*/
        int ronda = 0; /*Nos sirve para contar las rondas que llevamos y cuando tenemos que desplegar el menu espcifico de contestar apuesta par ajugador*/
        int opc = 0; /*Variable de opcion jugador*/
        int apuestaJugador; /*variable para almacenar la apuesta */
        int apuestaMaquina; /*variable para almacenar la apuesta */
        boolean abandona; // para ver si el jugador abandona una ronda
        boolean acabada = false; // para ver si hay un ganador
        boolean allIn = false; // para detectar si se realiza un all in

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("BIENVENIDO/A AL TEXAS HOLDEM");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("¿ Estas preparado para ganar un gran fortuna ? ");


        do { /*BUCLE PARA LA PARTIDA */
            ronda++;
            partida.inicarRonda(jugador,maquina,mesa);
            int fase = 0 ;/* Variable para ver las fases que hay en cada Ronda, es decir 4.*/
            abandona = false;

            if(ronda >1 ){
                jugador.menuContinuar();
                opc = Util.leerOpcMenu(teclado,5);
                switch(opc)
                {
                    case 1:
                        System.out.println("CONITNUAMOS.");
                        System.out.println("-------------------------------------------");
                        break;
                    case 2:
                        System.out.println("Has seleccionado ABANDONAR.");
                        System.out.println("-------------------------------------------");


                        break;

                    default: /*Si se equivoca mostrar mensaje de error*/
                        System.out.println("Error, debes de introducir el numero correcto de la opcion que deseas.");
                }
            }
        }
        while (!acabada);
    }

}
