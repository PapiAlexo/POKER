import utilidades.Util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
public class Main2 {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        Partida partida = new Partida(); // inicamos la partida
        Mesa mesa = new Mesa(0);
        Maquina maquina = new Maquina(1000000);
        Jugador jugador = new Jugador(1000000);
        File archivoJugador = new File("registroJugador.txt");
        File archivoMaquina = new File("registroMaquina.txt");
        try {
            if (!archivoJugador.exists())
                archivoJugador.createNewFile();
        }
        catch(IOException ex) {ex.printStackTrace();}
        try {
            if (!archivoMaquina.exists())
                archivoMaquina.createNewFile();
        }
        catch(IOException ex) {ex.printStackTrace();}
        Registrador registrador = new Registrador(archivoJugador, archivoMaquina);

        int turno = Util.getNumber(1, 2); /*Para ver quien empieza primero, 1--> maquina 2--> Jugador*/
        int ronda = 0; /*Nos sirve para contar las rondas que llevamos y cuando tenemos que desplegar el menu espcifico de contestar apuesta par ajugador*/
        int opc = 0; /*Variable de opcion jugador*/
        int apuestaJugador; /*variable para almacenar la apuesta */
        int apuestaMaquina; /*variable para almacenar la apuesta */
        boolean abandona; // para ver si el jugador abandona una ronda
        boolean acabada = false; // para ver si hay un ganador
        boolean allIn = false; // para detectar si se realiza un all in
        boolean salir = false; // para salir del juego

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("BIENVENIDO/A AL TEXAS HOLDEM");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("¿ Estas preparado para ganar un gran fortuna ? ");


        do { /*BUCLE PARA LA PARTIDA */
            ronda++;
            partida.inicarRonda(jugador,maquina,mesa);
            int fase = 0 ;/* Variable para ver las fases que hay en cada Ronda, es decir 4.*/
            abandona = false;

            if(ronda > 1 ){
                jugador.menuContinuar();
                opc = Util.leerOpcMenu(teclado,2);
                switch(opc)
                {
                    case 1:
                        System.out.println("CONITNUAMOS.");
                        System.out.println("-------------------------------------------");
                        break;
                    case 2:
                        System.out.println("Has seleccionado ABANDONAR.");
                        System.out.println("-------------------------------------------");
                        salir = true;

                        break;

                    default: /*Si se equivoca mostrar mensaje de error*/
                        System.out.println("Error, debes de introducir el numero correcto de la opcion que deseas.");
                }
            }
            if (turno%2==0) /*EMPIEZA JUGANDO EL JUGADOR*/
            {
                do { /*BUCLE PARA LA RONDA*/
                    //############ CIEGA DE JUGADOR FASE 1 #########################################################################################################################################################################################################
                    fase++;
                    do{ /*BUCLE PARA DECISION DE JUGADOR */

                        apuestaJugador =0;

                        System.out.println();
                        System.out.println("*******************************************");
                        System.out.println("FASE --> PREFLOP O CIEGA");
                        System.out.println("*******************************************");
                        System.out.println();

                        jugador.decisionJugador(); /*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado,5);
                        switch(opc)
                        {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO.");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA.");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);
                                break;

                            case 3:
                                System.out.println("Has selecionado APOSTAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador=jugador.apostar(teclado); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador); /*Ponemos en la mesa la apuesta del jugador*/
                                break;
                            case 4:
                                System.out.println("Has selecionado PASAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = 0; /*Ponemos cero porque no apuesta nada*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                                abandona = true;

                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introducir el numero correcto de la opcion que deseas.");
                        }

                    }
                    while(opc>5||opc<3); /*Sale cuando la opcion sea 3-4-5*/

                    //############ CONTESTA LA MAQUIINA FASE 1#############
                    apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa

                    if(apuestaMaquina == apuestaJugador)
                    {
                        System.out.println("Tu contrincante ha igualado la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                    }
                    else{
                        System.out.println("Tu contrincante ha subido la apuesta hasta la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                    }

                    /*Comprobamos si la maquina a subido la apuesta */
                    if(partida.compararAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                    {
                        jugador.decirAllIn(); // menu de decision de ALL IN
                        opc = teclado.nextInt();
                        switch (opc){
                            case 1:
                                System.out.println("Has seleccionado ALL IN.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;

                            case 2:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                        }

                    }
                    if(apuestaJugador<apuestaMaquina) /*Maquina sube la apuesta*/ {
                        jugador.contestarSubida(); /* MUESTRA MENU PARA CONTESTAR SUBIDA */
                        opc = teclado.nextInt();
                        switch (opc) {
                            case 1:
                                System.out.println("Has selecionado IGUALAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.igualarSubida(apuestaJugador, apuestaMaquina);

                                /*Como iguala pasamos a la siguiente ronda, dejamos las apuestas en la mesa*/
                                mesa.añadirDineroApuestas(apuestaJugador);
                                break;

                            case 2:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                        }
                    }

                    //############ FLOP DE JUGADOR FASE 2 ###############################################################################################################################################################################################
                    do{ /*BUCLE PARA DECISION DE JUGADOR*/
                        fase++;
                        apuestaJugador =0;
                        maquina.addCartaToManoMesa(mesa.getCartaFase(1));
                        maquina.addCartaToManoMesa(mesa.getCartaFase(2));
                        maquina.addCartaToManoMesa(mesa.getCartaFase(3));

                        System.out.println();
                        System.out.println("*******************************************");
                        System.out.println("FASE --> FLOP");
                        System.out.println("*******************************************");
                        System.out.println();

                        jugador.decisionJugador(); /*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado,5);
                        switch(opc)
                        {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO.");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA.");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);

                                break;

                            case 3:
                                System.out.println("Has selecionado APOSTAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador=jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 4:
                                System.out.println("Has selecionado PASAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = 0; /*Ponemos cero porque no apuesta nada*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /*Como abandona pasamos el dienro en la mesa a la maquina*/
                                abandona = true;

                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");
                        }

                    }
                    while(opc>5||opc<3); /*Sale cuando la opcion sea 3-4-5*/

                    //############ CONTESTA LA MAQUIINA FASE 2 #############
                    apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa

                    if(apuestaMaquina == apuestaJugador)
                    {
                        System.out.println("Tu contrincante ha igualado la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                    }
                    else{
                        System.out.println("Tu contrincante ha subido la apuesta hasta la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                    }

                    /*Comprobamos si la maquina a subido la apuesta */
                    if(partida.compararAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                    {
                        jugador.decirAllIn(); // menu de decision de ALL IN
                        opc = teclado.nextInt();
                        switch (opc){
                            case 1:
                                System.out.println("Has seleccionado ALL IN.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;

                            case 2:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                        }

                    }
                    if(apuestaJugador<apuestaMaquina) /*Maquina sube la apuesta*/
                    {
                        jugador.contestarSubida(); /* MUESTRA MENU PARA CONTESTAR SUBIDA */
                        opc = teclado.nextInt();
                        switch (opc){
                            case 1:
                                System.out.println("Has selecionado IGUALAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador=jugador.igualarSubida(apuestaJugador,apuestaMaquina);
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                /*Como iguala pasamos a la siguiente ronda, dejamos las apuestas en la mesa*/


                            case 2:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                        }
                    }
                    //############ TURN DE JUGADOR FASE 3 ###############################################################################################################################################################################################
                    do{ /*BUCLE PARA DECISION DE JUGADOR*/
                        fase++;
                        apuestaJugador =0;
                        maquina.addCartaToManoMesa(mesa.getCartaFase(4));

                        System.out.println();
                        System.out.println("*******************************************");
                        System.out.println("FASE --> TURN");
                        System.out.println("*******************************************");
                        System.out.println();

                        jugador.decisionJugador(); /*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado,5);
                        switch(opc)
                        {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO.");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA.");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);

                                break;

                            case 3:
                                System.out.println("Has selecionado APOSTAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador=jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 4:
                                System.out.println("Has selecionado PASAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = 0; /*Ponemos cero porque no apuesta nada*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /*Como abandona pasamos el dienro en la mesa a la maquina*/
                                abandona = true;

                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");
                        }

                    }
                    while(opc>5||opc<3); /*Sale cuando la opcion sea 3-4-5*/

                    //############ CONTESTA LA MAQUIINA FASE 3 #############
                    apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa

                    if(apuestaMaquina == apuestaJugador)
                    {
                        System.out.println("Tu contrincante ha igualado la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                    }
                    else{
                        System.out.println("Tu contrincante ha subido la apuesta hasta la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                    }

                    /*Comprobamos si la maquina a subido la apuesta */
                    if(partida.compararAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                    {
                        jugador.decirAllIn(); // menu de decision de ALL IN
                        opc = teclado.nextInt();
                        switch (opc){
                            case 1:
                                System.out.println("Has seleccionado ALL IN.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/


                            case 2:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                        }

                    }
                    if(apuestaJugador<apuestaMaquina) /*Maquina sube la apuesta*/
                    {
                        jugador.contestarSubida(); /* MUESTRA MENU PARA CONTESTAR SUBIDA */
                        opc = teclado.nextInt();
                        switch (opc){
                            case 1:
                                System.out.println("Has selecionado IGUALAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador=jugador.igualarSubida(apuestaJugador,apuestaMaquina);
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                /*Como iguala pasamos a la siguiente ronda, dejamos las apuestas en la mesa*/


                            case 2:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                        }
                    }
                    //############ RIVER DE JUGADOR FASE 4 ###############################################################################################################################################################################################
                    do{ /*BUCLE PARA DECISION DE JUGADOR*/
                        fase++;
                        apuestaJugador =0;
                        maquina.addCartaToManoMesa(mesa.getCartaFase(5));

                        System.out.println();
                        System.out.println("*******************************************");
                        System.out.println("FASE --> RIVER");
                        System.out.println("*******************************************");
                        System.out.println();

                        jugador.decisionJugador(); /*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado,5);
                        switch(opc)
                        {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO.");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA.");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);

                                break;

                            case 3:
                                System.out.println("Has selecionado APOSTAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador=jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 4:
                                System.out.println("Has selecionado PASAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = 0; /*Ponemos cero porque no apuesta nada*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /*Como abandona pasamos el dienro en la mesa a la maquina*/
                                abandona = true;

                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");
                        }

                    }
                    while(opc>5||opc<3); /*Sale cuando la opcion sea 3-4-5*/

                    //############ CONTESTA LA MAQUIINA FASE 4 #############
                    apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa

                    if(apuestaMaquina == apuestaJugador)
                    {
                        System.out.println("Tu contrincante ha igualado la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                    }
                    else{
                        System.out.println("Tu contrincante ha subido la apuesta hasta la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                    }

                    /*Comprobamos si la maquina a subido la apuesta */
                    if(partida.compararAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                    {
                        jugador.decirAllIn(); // menu de decision de ALL IN
                        opc = teclado.nextInt();
                        switch (opc){
                            case 1:
                                System.out.println("Has seleccionado ALL IN.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;

                            case 2:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                        }

                    }
                    if(apuestaJugador<apuestaMaquina) /*Maquina sube la apuesta*/
                    {
                        jugador.contestarSubida(); /* MUESTRA MENU PARA CONTESTAR SUBIDA */
                        opc = teclado.nextInt();
                        switch (opc){
                            case 1:
                                System.out.println("Has selecionado IGUALAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador=jugador.igualarSubida(apuestaJugador,apuestaMaquina);
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                /*Como iguala pasamos a la siguiente ronda, dejamos las apuestas en la mesa*/


                            case 2:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                        }
                    }
                    if (partida.comprobarGanador(jugador,maquina)!=0)
                    {
                        acabada = true;
                    }

                    else{

                    }
                    jugador.cleanMano();
                    maquina.cleanMano();
                    mesa.cleanMano();
                    mesa.limpiarDineroMesa();
                }
                while(fase>=4||abandona);

            }
            else  /* RONDA CUANDO EMPIEZA LA MAQUINA*/
            {

                //############ CIEGA DE MAQUINA FASE 1 #########################################################################################################################################################################################################
                fase++;
                apuestaJugador = 0;

                System.out.println();
                System.out.println("*******************************************");
                System.out.println("FASE --> PREFLOP O CIEGA");
                System.out.println("*******************************************");
                System.out.println();

                apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa

                System.out.println("Tu contrincante ha apostado la siguiente cantidad --> [ " + apuestaMaquina + " ]");


                //############ CONTESTA LA JUGADOR FASE 1 #############

                /*Comprobamos si hay All In */
                if(partida.compararAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                {
                    jugador.decirAllIn(); // menu de decision de ALL IN
                    opc = teclado.nextInt();
                    switch (opc){
                        case 1:
                            System.out.println("Has seleccionado ALL IN.");
                            System.out.println("-------------------------------------------");
                            apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                            mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                            break;

                        case 2:
                            System.out.println("Has selecionado ABANDONAR.");
                            System.out.println("-------------------------------------------");
                            maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                            abandona = true;
                            break;

                        default: /*Si se equivoca mostrar mensaje de error*/
                            System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                    }

                }
                else { /*NO HAY ALL IN*/
                    do { /*BUCLE PARA DECISION DE JUGADOR */


                        jugador.contesteaJugador();/*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado, 5);
                        switch (opc) {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO.");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA.");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);
                                break;

                            case 3:
                                System.out.println("Has selecionado IGUALAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.igualar(apuestaMaquina); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 4:
                                System.out.println("Has selecionado SUBIR .");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                                abandona = true;

                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");
                        }

                    }
                    while (opc > 5 || opc < 3); /*Sale cuando la opcion sea 3-4-5*/

                    /*Comprobamos si el JUGADOR a subido la apuesta */
                    if(partida.compararAllIn(apuestaJugador,maquina.getDinero())) // HAY ALL IN
                    {
                        apuestaMaquina = maquina.getDinero(); // apostamos todo lo que le queda
                        mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la pauesta en la mesa
                        System.out.println("Tu contrincante ha apostado ALL IN--> [ " + apuestaMaquina + " ]");
                        allIn = true;

                    }
                    if(apuestaMaquina<apuestaJugador) /*JUGADOR sube la apuesta*/ {
                        {
                            apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                            mesa.añadirDineroApuestas(apuestaMaquina);
                            System.out.println("Tu contrincante iguala tu apuesta --> [ " + apuestaMaquina + " ]");
                        }
                    }
                }

                //############ FLOP DE MAQUINA FASE 2 #########################################################################################################################################################################################################
                fase++;
                apuestaJugador =0;
                maquina.addCartaToManoMesa(mesa.getCartaFase(1));
                maquina.addCartaToManoMesa(mesa.getCartaFase(2));
                maquina.addCartaToManoMesa(mesa.getCartaFase(3));


                System.out.println();
                System.out.println("*******************************************");
                System.out.println("FASE --> FLOP");
                System.out.println("*******************************************");
                System.out.println();

                apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa
                System.out.println("Tu contrincante ha apostado la siguiente cantidad --> [ " + apuestaMaquina + " ]");

                //############ CONTESTA LA JUGADOR FASE 2 #############
                /*Comprobamos si hay All In */
                if(partida.compararAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                {
                    jugador.decirAllIn(); // menu de decision de ALL IN
                    opc = teclado.nextInt();
                    switch (opc){
                        case 1:
                            System.out.println("Has seleccionado ALL IN.");
                            System.out.println("-------------------------------------------");
                            apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                            mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                            break;

                        case 2:
                            System.out.println("Has selecionado ABANDONAR.");
                            System.out.println("-------------------------------------------");
                            maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                            abandona = true;
                            break;

                        default: /*Si se equivoca mostrar mensaje de error*/
                            System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                    }

                }
                else { /*NO HAY ALL IN*/
                    do { /*BUCLE PARA DECISION DE JUGADOR */

                        jugador.contesteaJugador();/*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado, 5);
                        switch (opc) {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO.");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA.");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);
                                break;

                            case 3:
                                System.out.println("Has selecionado IGUALAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.igualar(apuestaMaquina); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 4:
                                System.out.println("Has selecionado SUBIR .");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                                abandona = true;

                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");
                        }

                    }
                    while (opc > 5 || opc < 3); /*Sale cuando la opcion sea 3-4-5*/
                }

                /*Comprobamos si el JUGADOR a subido la apuesta */
                if(partida.compararAllIn(apuestaJugador,maquina.getDinero())) // HAY ALL IN
                {
                    apuestaMaquina = maquina.getDinero(); // apostamos todo lo que le queda
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la pauesta en la mesa
                    System.out.println("Tu contrincante ha apostado ALLIN --> [ " + apuestaMaquina + " ]");
                    allIn = true;

                }
                if(apuestaMaquina<apuestaJugador) /*JUGADOR sube la apuesta*/ {
                    {
                        apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                        mesa.añadirDineroApuestas(apuestaMaquina);
                        System.out.println("Tu contrincante iguala tu apuesta --> [ " + apuestaMaquina + " ]");

                    }
                }

                //############ TURN DE MAQUINA FASE 3 #########################################################################################################################################################################################################
                fase++;
                apuestaJugador =0;
                maquina.addCartaToManoMesa(mesa.getCartaFase(4));

                System.out.println();
                System.out.println("*******************************************");
                System.out.println("FASE --> TURN");
                System.out.println("*******************************************");
                System.out.println();

                apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa
                System.out.println("Tu contrincante ha apostado la siguiente cantidad --> [ " + apuestaMaquina + " ]");

                //############ CONTESTA LA JUGADOR FASE 3 #############
                /*Comprobamos si hay All In */
                if(partida.compararAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                {
                    jugador.decirAllIn(); // menu de decision de ALL IN
                    opc = teclado.nextInt();
                    switch (opc){
                        case 1:
                            System.out.println("Has seleccionado ALL IN.");
                            System.out.println("-------------------------------------------");
                            apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                            mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                            break;

                        case 2:
                            System.out.println("Has selecionado ABANDONAR.");
                            System.out.println("-------------------------------------------");
                            maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                            abandona = true;
                            break;

                        default: /*Si se equivoca mostrar mensaje de error*/
                            System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                    }

                }
                else { /*NO HAY ALL IN*/
                    do { /*BUCLE PARA DECISION DE JUGADOR */

                        jugador.contesteaJugador();/*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado, 5);
                        switch (opc) {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO.");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA.");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);
                                break;

                            case 3:
                                System.out.println("Has selecionado IGUALAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.igualar(apuestaMaquina); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 4:
                                System.out.println("Has selecionado SUBIR .");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                                abandona = true;

                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");
                        }

                    }
                    while (opc > 5 || opc < 3); /*Sale cuando la opcion sea 3-4-5*/
                }

                /*Comprobamos si el JUGADOR a subido la apuesta */
                if(partida.compararAllIn(apuestaJugador,maquina.getDinero())) // HAY ALL IN
                {
                    apuestaMaquina = maquina.getDinero(); // apostamos todo lo que le queda
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la pauesta en la mesa
                    System.out.println("Tu contrincante ha apostado ALL IN --> [ " + apuestaMaquina + " ]");
                    allIn = true;

                }
                if(apuestaMaquina<apuestaJugador) /*JUGADOR sube la apuesta*/ {
                    {
                        apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                        mesa.añadirDineroApuestas(apuestaMaquina);
                        System.out.println("Tu contrincante ha igualado tu apuesta --> [ " + apuestaMaquina + " ]");

                    }
                }

                //############ RIVER DE MAQUINA FASE 4 #########################################################################################################################################################################################################
                fase++;
                apuestaJugador =0;
                maquina.addCartaToManoMesa(mesa.getCartaFase(5));

                System.out.println();
                System.out.println("*******************************************");
                System.out.println("FASE --> RIVER");
                System.out.println("*******************************************");
                System.out.println();

                apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa
                System.out.println("Tu contrincante ha apostado la siguiente cantidad --> [ " + apuestaMaquina + " ]");

                //############ CONTESTA LA JUGADOR FASE 4 #############
                /*Comprobamos si hay All In */
                if(partida.compararAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                {
                    jugador.decirAllIn(); // menu de decision de ALL IN
                    opc = teclado.nextInt();
                    switch (opc){
                        case 1:
                            System.out.println("Has seleccionado ALL IN.");
                            System.out.println("-------------------------------------------");
                            apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                            mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                            break;

                        case 2:
                            System.out.println("Has selecionado ABANDONAR.");
                            System.out.println("-------------------------------------------");
                            maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                            abandona = true;
                            break;

                        default: /*Si se equivoca mostrar mensaje de error*/
                            System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");

                    }

                }
                else { /*NO HAY ALL IN*/
                    do { /*BUCLE PARA DECISION DE JUGADOR */

                        jugador.contesteaJugador();/*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado, 5);
                        switch (opc) {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO.");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA.");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);
                                break;

                            case 3:
                                System.out.println("Has selecionado IGUALAR.");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.igualar(apuestaMaquina); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 4:
                                System.out.println("Has selecionado SUBIR .");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR.");
                                System.out.println("-------------------------------------------");
                                /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                                abandona = true;

                                break;

                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes de introudir el numero correcto de la opcion que deseas.");
                        }

                    }
                    while (opc > 5 || opc < 3); /*Sale cuando la opcion sea 3-4-5*/
                }

                /*Comprobamos si el JUGADOR a subido la apuesta */
                if(partida.compararAllIn(apuestaJugador,maquina.getDinero())) // HAY ALL IN
                {
                    apuestaMaquina = maquina.getDinero(); // apostamos todo lo que le queda
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la pauesta en la mesa
                    System.out.println("Tu contrincante ha apostado  AllIN --> [ " + apuestaMaquina + " ]");
                    allIn = true;

                }
                if(apuestaMaquina<apuestaJugador) /*JUGADOR sube la apuesta*/ {
                    {
                        apuestaMaquina = maquina.obtenerCalidadMano(fase,mesa.getDinero());
                        mesa.añadirDineroApuestas(apuestaMaquina);
                        System.out.println("Tu contrincante ha igualado tu apuesta --> [ " + apuestaMaquina + " ]");

                    }
                }

                jugador.cleanMano();
                maquina.cleanMano();
                mesa.cleanMano();
                mesa.limpiarDineroMesa();
            }


        }
        while (!acabada||!salir);
    }

}
