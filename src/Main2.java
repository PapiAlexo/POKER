import utilidades.Util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
public class Main2 {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        Partida partida = new Partida(); // iniciamos la partida
        Mesa mesa = new Mesa(0);
        Maquina maquina = new Maquina(10000);
        Jugador jugador = new Jugador(10000);
        File archivoJugador = new File("registroJugador.txt");
        File archivoMaquina = new File("registroMaquina.txt");
        File archivoApuesta = new File("archivoApuesta");
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
        int ronda = 0; /*Nos sirve para contar las rondas que llevamos y cuando tenemos que desplegar el menu especifico de contestar apuesta para jugador*/
        int opc = 0; /*Variable de opcion jugador*/
        int apuestaJugador; /*variable para almacenar la apuesta */
        int apuestaMaquina; /*variable para almacenar la apuesta */
        boolean abandona; // para ver si el jugador abandona una ronda
        boolean acabada = false; // para ver si hay un ganador
        boolean allInJugador = false; // para detectar si se realiza un all in
        boolean allInMaquina = false;
        boolean salir = false; // para salir del juego

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("BIENVENIDO/A AL TEXAS HOLDEM");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("¿ Estás preparado para ganar un gran fortuna ? ");

        turno=1; //temporal para arreglar la parte del jugador

        do { /*BUCLE PARA LA PARTIDA */
            /*Lo que hacen es limpiar y preparar los actores de las partidas SOLO SI TIENEN ALGO*/

            maquina.cleanManoMesa();
            maquina.cleanMano();
            jugador.cleanMano();
            mesa.limpiarDineroMesa();
            mesa.cleanMano();

            /*Repartimos las cartas para empezar la ronda*/
            partida.inicarRonda(jugador,maquina,mesa);
            int fase = 0 ;/* Variable para ver las fases que hay en cada Ronda, es decir 4.*/
            abandona = false;
            turno++;
            if (turno%2==0) /*EMPIEZA JUGANDO EL JUGADOR*/ {
                System.out.println();
                System.out.println("*** COMIENZAS TÚ ***");
                ronda++;

                //############ CIEGA DE JUGADOR FASE 1 #########################################################################################################################################################################################################
                fase++;
                do { /*BUCLE PARA DECISION DE JUGADOR */
                    apuestaJugador = 0;

                    System.out.println();
                    System.out.println("*******************************************");
                    System.out.println("FASE --> PREFLOP O CIEGA");
                    System.out.println("*******************************************");

                    jugador.decisionJugador(); /*MOSTRAMOS EL MENU*/
                    opc = Util.leerOpcMenu(teclado, 5);
                    switch (opc) {
                        case 1:
                            System.out.println("Has seleccionado VISUALIZAR MI MANO");
                            System.out.println("-------------------------------------------");
                            jugador.mostrarCartasDinero();
                            break;
                        case 2:
                            System.out.println("Has seleccionado VISUALIZAR MESA");
                            System.out.println("-------------------------------------------");
                            mesa.imprimirMano(fase);
                            break;
                        case 3:
                            System.out.println("Has selecionado APOSTAR");
                            System.out.println("-------------------------------------------");
                            int dineroAntiguo=jugador.getDinero();
                            apuestaJugador = jugador.apostar(teclado); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                            mesa.añadirDineroApuestas(apuestaJugador); /*Ponemos en la mesa la apuesta del jugador*/
                            if (partida.comprobarAllIn(apuestaJugador, dineroAntiguo)) // HAY ALL IN
                            {
                                System.out.println();
                                System.out.println("*** HAS HECHO ALL IN ***");
                                allInJugador = true;
                            }
                            break;
                        case 4:
                            System.out.println("Has selecionado PASAR");
                            System.out.println("-------------------------------------------");
                            apuestaJugador = 0; /*Ponemos cero porque no apuesta nada*/
                            break;
                        case 5:
                            System.out.println("Has selecionado ABANDONAR");
                            System.out.println("-------------------------------------------");
                            /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                            abandona = true;
                            break;
                        default: /*Si se equivoca mostrar mensaje de error*/
                            System.out.println("Error, debes introducir el número correcto de la opción que desees");
                    }
                }
                while (opc > 5 || opc < 3); /*Sale cuando la opcion sea 3-4-5*/
                if (!abandona && !allInJugador) { //si no abandono se sigue jugando
                    //############ CONTESTA LA MAQUINA FASE 1#############
                    apuestaMaquina = maquina.obtenerCalidadMano(fase, apuestaJugador);
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa

                    if ((apuestaMaquina == apuestaJugador)) {
                        System.out.println();
                        System.out.println("Tu contrincante ha igualado la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                    } else {
                        System.out.println();
                        System.out.println("Tu contrincante ha subido la apuesta hasta la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                        if(partida.comprobarAllIn(apuestaMaquina, maquina.getDinero())){
                            System.out.println();
                            System.out.println("*** TU CONTRINCANTE HA HECHO ALL IN ***");
                            allInMaquina=true;
                        }
                        jugador.contestarSubida(); /* MUESTRA MENU PARA CONTESTAR SUBIDA */
                        opc = teclado.nextInt();
                        switch (opc) {
                            case 1:
                                System.out.println("Has selecionado IGUALAR");
                                System.out.println("-------------------------------------------");
                                if(allInMaquina){
                                    allInJugador=true;
                                }
                                apuestaJugador = jugador.igualarSubida(apuestaJugador, apuestaMaquina);
                                /*Como iguala pasamos a la siguiente ronda, dejamos las apuestas en la mesa*/
                                mesa.añadirDineroApuestas(apuestaJugador);
                                break;
                            case 2:
                                System.out.println("Has selecionado ABANDONAR");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;
                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes introducir el número correcto de la opción que desees");
                        }
                    }
                }
                //############ FLOP DE JUGADOR FASE 2 ###############################################################################################################################################################################################
                fase++;
                if (!abandona && !allInJugador) {
                    opc = 0;
                    maquina.addCartaMesaToMano(mesa.getCartaFase(1));
                    maquina.addCartaMesaToMano(mesa.getCartaFase(2));
                    maquina.addCartaMesaToMano(mesa.getCartaFase(3));

                    /*BUCLE PARA DECISION DE JUGADOR*/
                    while (opc > 5 || opc < 3) {/*No entra si la opcion es 5-4-3*/
                        apuestaJugador = 0;
                        System.out.println();
                        System.out.println("*******************************************");
                        System.out.println("FASE --> FLOP");
                        System.out.println("*******************************************");
                        jugador.decisionJugador(); /*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado, 5);
                        switch (opc) {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);
                                break;
                            case 3:
                                System.out.println("Has selecionado APOSTAR");
                                System.out.println("-------------------------------------------");
                                int dineroAntiguo=jugador.getDinero();
                                apuestaJugador = jugador.apostar(teclado); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador); /*Ponemos en la mesa la apuesta del jugador*/
                                if (partida.comprobarAllIn(apuestaJugador, dineroAntiguo)) // HAY ALL IN
                                {
                                    System.out.println();
                                    System.out.println("*** HAS HECHO ALL IN ***");
                                    allInJugador = true;
                                }
                                break;
                            case 4:
                                System.out.println("Has selecionado PASAR");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = 0; /*Ponemos cero porque no apuesta nada*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /*Como abandona pasamos el dienro en la mesa a la maquina*/
                                abandona = true;
                                break;
                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes introducir el número correcto de la opción que desees");
                        }
                    }
                    //############ CONTESTA LA MAQUINA FASE 2 #############
                    if (!abandona && !allInJugador) { //si no abandono se sigue jugando
                        apuestaMaquina = maquina.obtenerCalidadMano(fase, apuestaJugador);
                        mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa

                        if ((apuestaMaquina == apuestaJugador)) {
                            System.out.println();
                            System.out.println("Tu contrincante ha igualado la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                        } else {
                            System.out.println();
                            System.out.println("Tu contrincante ha subido la apuesta hasta la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                            if(partida.comprobarAllIn(apuestaMaquina, maquina.getDinero())){
                                System.out.println();
                                System.out.println("*** TU CONTRINCANTE HA HECHO ALL IN ***");
                                allInMaquina=true;
                            }
                            jugador.contestarSubida(); /* MUESTRA MENU PARA CONTESTAR SUBIDA */
                            opc = teclado.nextInt();
                            switch (opc) {
                                case 1:
                                    System.out.println("Has selecionado IGUALAR");
                                    System.out.println("-------------------------------------------");
                                    if(allInMaquina){
                                        allInJugador=true;
                                    }
                                    apuestaJugador = jugador.igualarSubida(apuestaJugador, apuestaMaquina);
                                    /*Como iguala pasamos a la siguiente ronda, dejamos las apuestas en la mesa*/
                                    mesa.añadirDineroApuestas(apuestaJugador);
                                    break;
                                case 2:
                                    System.out.println("Has selecionado ABANDONAR");
                                    System.out.println("-------------------------------------------");
                                    maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                    abandona = true;
                                    break;
                                default: /*Si se equivoca mostrar mensaje de error*/
                                    System.out.println("Error, debes introducir el número correcto de la opción que desees");
                            }
                        }
                    }
                }
                //############ TURNO DE JUGADOR FASE 3 ###############################################################################################################################################################################################
                fase++;
                if (!abandona && !allInJugador) {
                    opc = 0;
                    maquina.addCartaMesaToMano(mesa.getCartaFase(4));
                    while (opc > 5 || opc < 3) {/*No entra si la opcion es 5-4-3*/
                        apuestaJugador = 0;

                        System.out.println();
                        System.out.println("*******************************************");
                        System.out.println("FASE --> TURN");
                        System.out.println("*******************************************");

                        jugador.decisionJugador(); /*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado, 5);
                        switch (opc) {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);
                                break;
                            case 3:
                                System.out.println("Has selecionado APOSTAR");
                                System.out.println("-------------------------------------------");
                                int dineroAntiguo=jugador.getDinero();
                                apuestaJugador = jugador.apostar(teclado); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador); /*Ponemos en la mesa la apuesta del jugador*/
                                if (partida.comprobarAllIn(apuestaJugador, dineroAntiguo)) // HAY ALL IN
                                {
                                    System.out.println();
                                    System.out.println("*** HAS HECHO ALL IN ***");
                                    allInJugador = true;
                                }
                                break;
                            case 4:
                                System.out.println("Has selecionado PASAR");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = 0; /*Ponemos cero porque no apuesta nada*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /*Como abandona pasamos el dienro en la mesa a la maquina*/
                                abandona = true;
                                break;
                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes introducir el número correcto de la opción que desees");
                        }
                    }
                    //############ CONTESTA LA MAQUINA FASE 3 #############
                    if (!abandona && !allInJugador) { //si no abandono se sigue jugando
                        apuestaMaquina = maquina.obtenerCalidadMano(fase, apuestaJugador);
                        mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa

                        if ((apuestaMaquina == apuestaJugador)) {
                            System.out.println();
                            System.out.println("Tu contrincante ha igualado la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                        } else {
                            System.out.println();
                            System.out.println("Tu contrincante ha subido la apuesta hasta la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                            if(partida.comprobarAllIn(apuestaMaquina, maquina.getDinero())){
                                System.out.println();
                                System.out.println("*** TU CONTRINCANTE HA HECHO ALL IN ***");
                                allInMaquina=true;
                            }
                            jugador.contestarSubida(); /* MUESTRA MENU PARA CONTESTAR SUBIDA */
                            opc = teclado.nextInt();
                            switch (opc) {
                                case 1:
                                    System.out.println("Has selecionado IGUALAR");
                                    System.out.println("-------------------------------------------");
                                    if(allInMaquina){
                                        allInJugador=true;
                                    }
                                    apuestaJugador = jugador.igualarSubida(apuestaJugador, apuestaMaquina);
                                    /*Como iguala pasamos a la siguiente ronda, dejamos las apuestas en la mesa*/
                                    mesa.añadirDineroApuestas(apuestaJugador);
                                    break;
                                case 2:
                                    System.out.println("Has selecionado ABANDONAR");
                                    System.out.println("-------------------------------------------");
                                    maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                    abandona = true;
                                    break;
                                default: /*Si se equivoca mostrar mensaje de error*/
                                    System.out.println("Error, debes introducir el número correcto de la opción que desees");
                            }
                        }
                    }
                }
                //############ RIVER DE JUGADOR FASE 4 ###############################################################################################################################################################################################
                fase++;
                if (!abandona && !allInJugador) {
                    opc = 0;
                    maquina.addCartaMesaToMano(mesa.getCartaFase(5));
                    while (opc > 5 || opc < 3) {/*No entra si la opcion es 5-4-3*/
                        apuestaJugador = 0;

                        System.out.println();
                        System.out.println("*******************************************");
                        System.out.println("FASE --> RIVER");
                        System.out.println("*******************************************");

                        jugador.decisionJugador(); /*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado, 5);
                        switch (opc) {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);
                                break;
                            case 3:
                                System.out.println("Has selecionado APOSTAR");
                                System.out.println("-------------------------------------------");
                                int dineroAntiguo=jugador.getDinero();
                                apuestaJugador = jugador.apostar(teclado); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador); /*Ponemos en la mesa la apuesta del jugador*/
                                if (partida.comprobarAllIn(apuestaJugador, dineroAntiguo)) // HAY ALL IN
                                {
                                    System.out.println();
                                    System.out.println("*** HAS HECHO ALL IN ***");
                                    allInJugador = true;
                                }
                                break;
                            case 4:
                                System.out.println("Has selecionado PASAR");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = 0; /*Ponemos cero porque no apuesta nada*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /*Como abandona pasamos el dienro en la mesa a la maquina*/
                                abandona = true;
                                break;
                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes introducir el número correcto de la opción que desees");
                        }
                    }
                    //############ CONTESTA LA MAQUINA FASE 4 #############
                    if (!abandona && !allInJugador) { //si no abandono se sigue jugando
                        apuestaMaquina = maquina.obtenerCalidadMano(fase, apuestaJugador);
                        mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa

                        if ((apuestaMaquina == apuestaJugador)) {
                            System.out.println();
                            System.out.println("Tu contrincante ha igualado la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                        } else {
                            System.out.println();
                            System.out.println("Tu contrincante ha subido la apuesta hasta la siguiente cantidad --> [ " + apuestaMaquina + " ]");
                            if(partida.comprobarAllIn(apuestaMaquina, maquina.getDinero())){
                                System.out.println();
                                System.out.println("*** TU CONTRINCANTE HA HECHO ALL IN ***");
                                allInMaquina=true;
                            }
                            jugador.contestarSubida(); /* MUESTRA MENU PARA CONTESTAR SUBIDA */
                            opc = teclado.nextInt();
                            switch (opc) {
                                case 1:
                                    System.out.println("Has selecionado IGUALAR");
                                    System.out.println("-------------------------------------------");
                                    if(allInMaquina){
                                        allInJugador=true;
                                    }
                                    apuestaJugador = jugador.igualarSubida(apuestaJugador, apuestaMaquina);
                                    /*Como iguala pasamos a la siguiente ronda, dejamos las apuestas en la mesa*/
                                    mesa.añadirDineroApuestas(apuestaJugador);
                                    break;
                                case 2:
                                    System.out.println("Has selecionado ABANDONAR");
                                    System.out.println("-------------------------------------------");
                                    maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                    abandona = true;
                                    break;
                                default: /*Si se equivoca mostrar mensaje de error*/
                                    System.out.println("Error, debes introducir el número correcto de la opción que desees");
                            }
                        }
                    }
                }
            }
            else  /* RONDA CUANDO EMPIEZA LA MAQUINA*/
            {
                System.out.println();
                System.out.println("*** COMIENZA TU CONTRINCANTE ***");
                ronda++;
                //############ CIEGA DE MAQUINA FASE 1 #########################################################################################################################################################################################################
                fase++;
                apuestaJugador = 0;

                System.out.println();
                System.out.println("*******************************************");
                System.out.println("FASE --> PREFLOP O CIEGA");
                System.out.println("*******************************************");

                apuestaMaquina = maquina.obtenerCalidadMano(fase,0);
                mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa
                System.out.println();
                System.out.println("Tu contrincante ha apostado la siguiente cantidad --> [ " + apuestaMaquina + " ]");


                //############ CONTESTA LA JUGADOR FASE 1 #############

                /*Comprobamos si hay All In */
                if(partida.comprobarAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                {
                    jugador.decirAllIn(); // menu de decision de ALL IN
                    opc = teclado.nextInt();
                    switch (opc){
                        case 1:
                            System.out.println("Has seleccionado ALL IN");
                            System.out.println("-------------------------------------------");
                            apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                            mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                            break;
                        case 2:
                            System.out.println("Has selecionado ABANDONAR");
                            System.out.println("-------------------------------------------");
                            maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                            abandona = true;
                            break;
                        default: /*Si se equivoca mostrar mensaje de error*/
                            System.out.println("Error, debes introducir el número correcto de la opción que desees");
                    }
                }
                else { /*NO HAY ALL IN*/
                    do { /*BUCLE PARA DECISION DE JUGADOR */
                        jugador.contestaJugador();/*MOSTRAMOS EL MENU*/
                        opc = Util.leerOpcMenu(teclado, 5);
                        switch (opc) {
                            case 1:
                                System.out.println("Has seleccionado VISUALIZAR MI MANO");
                                System.out.println("-------------------------------------------");
                                jugador.mostrarCartasDinero();
                                break;
                            case 2:
                                System.out.println("Has seleccionado VISUALIZAR MESA");
                                System.out.println("-------------------------------------------");
                                mesa.imprimirMano(fase);
                                break;
                            case 3:
                                System.out.println("Has selecionado IGUALAR");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.igualar(apuestaMaquina); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 4:
                                System.out.println("Has selecionado SUBIR");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 5:
                                System.out.println("Has selecionado ABANDONAR");
                                System.out.println("-------------------------------------------");
                                /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                                abandona = true;
                                break;
                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes introducir el número correcto de la opción que desees");
                        }
                    }
                    while (opc > 5 || opc < 3); /*Sale cuando la opcion sea 3-4-5*/

                    /*Comprobamos si el JUGADOR a subido la apuesta */
                    if(partida.comprobarAllIn(apuestaJugador,jugador.getDinero())) // HAY ALL IN
                    {
                        apuestaMaquina = maquina.getDinero(); // apostamos todo lo que le queda
                        mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la pauesta en la mesa
                        System.out.println();
                        System.out.println("Tu contrincante ha apostado ALL IN--> [ " + apuestaMaquina + " ]");
                        allInJugador = true;
                    }
                    else {
                        apuestaMaquina = maquina.obtenerCalidadMano(fase, mesa.getDinero());
                        mesa.añadirDineroApuestas(apuestaMaquina);
                        System.out.println();
                        System.out.println("Tu contrincante iguala tu apuesta --> [ " + apuestaMaquina + " ]");
                    }
                }
                //############ FLOP DE MAQUINA FASE 2 #########################################################################################################################################################################################################
                fase++;
                if(!abandona)
                {
                    apuestaJugador =0;
                    maquina.addCartaMesaToMano(mesa.getCartaFase(1));
                    maquina.addCartaMesaToMano(mesa.getCartaFase(2));
                    maquina.addCartaMesaToMano(mesa.getCartaFase(3));

                    System.out.println();
                    System.out.println("*******************************************");
                    System.out.println("FASE --> FLOP");
                    System.out.println("*******************************************");

                    apuestaMaquina = maquina.obtenerCalidadMano(fase,0);
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa
                    System.out.println();
                    System.out.println("Tu contrincante ha apostado la siguiente cantidad --> [ " + apuestaMaquina + " ]");

                    //############ CONTESTA LA JUGADOR FASE 2 #############
                    /*Comprobamos si hay All In */
                    if(partida.comprobarAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                    {
                        jugador.decirAllIn(); // menu de decision de ALL IN
                        opc = teclado.nextInt();
                        switch (opc){
                            case 1:
                                System.out.println("Has seleccionado ALL IN");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 2:
                                System.out.println("Has selecionado ABANDONAR");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;
                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes introducir el número correcto de la opción que desees");
                        }
                    }
                    else { /*NO HAY ALL IN*/
                        do { /*BUCLE PARA DECISION DE JUGADOR */

                            jugador.contestaJugador();/*MOSTRAMOS EL MENU*/
                            opc = Util.leerOpcMenu(teclado, 5);
                            switch (opc) {
                                case 1:
                                    System.out.println("Has seleccionado VISUALIZAR MI MANO");
                                    System.out.println("-------------------------------------------");
                                    jugador.mostrarCartasDinero();
                                    break;
                                case 2:
                                    System.out.println("Has seleccionado VISUALIZAR MESA");
                                    System.out.println("-------------------------------------------");
                                    mesa.imprimirMano(fase);
                                    break;
                                case 3:
                                    System.out.println("Has selecionado IGUALAR");
                                    System.out.println("-------------------------------------------");
                                    apuestaJugador = jugador.igualar(apuestaMaquina); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                    mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                    break;
                                case 4:
                                    System.out.println("Has selecionado SUBIR");
                                    System.out.println("-------------------------------------------");
                                    apuestaJugador = jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                    mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                    break;
                                case 5:
                                    System.out.println("Has selecionado ABANDONAR");
                                    System.out.println("-------------------------------------------");
                                    /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                                    abandona = true;
                                    break;
                                default: /*Si se equivoca mostrar mensaje de error*/
                                    System.out.println("Error, debes introducir el número correcto de la opción que desees");
                            }
                        }
                        while (opc > 5 || opc < 3); /*Sale cuando la opcion sea 3-4-5*/
                    }
                    /*Comprobamos si el JUGADOR a subido la apuesta */
                    if(partida.comprobarAllIn(apuestaJugador,maquina.getDinero())) // HAY ALL IN
                    {
                        apuestaMaquina = maquina.getDinero(); // apostamos todo lo que le queda
                        mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la pauesta en la mesa
                        System.out.println();
                        System.out.println("Tu contrincante ha apostado ALLIN --> [ " + apuestaMaquina + " ]");
                        allInJugador = true;
                    }
                    if(apuestaMaquina<apuestaJugador) /*JUGADOR sube la apuesta*/ {
                        {
                            apuestaMaquina = maquina.obtenerCalidadMano(fase,apuestaJugador);
                            mesa.añadirDineroApuestas(apuestaMaquina);
                            System.out.println();
                            System.out.println("Tu contrincante iguala tu apuesta --> [ " + apuestaMaquina + " ]");
                        }
                    }
                }
                //############ TURN DE MAQUINA FASE 3 #########################################################################################################################################################################################################
               if(!abandona)
               {
                   fase++;
                   apuestaJugador =0;
                   maquina.addCartaMesaToMano(mesa.getCartaFase(4));

                   System.out.println();
                   System.out.println("*******************************************");
                   System.out.println("FASE --> TURN");
                   System.out.println("*******************************************");

                   apuestaMaquina = maquina.obtenerCalidadMano(fase,0);
                   mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa
                   System.out.println();
                   System.out.println("Tu contrincante ha apostado la siguiente cantidad --> [ " + apuestaMaquina + " ]");

                   //############ CONTESTA LA JUGADOR FASE 3 #############
                   /*Comprobamos si hay All In */
                   if(partida.comprobarAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                   {
                       jugador.decirAllIn(); // menu de decision de ALL IN
                       opc = teclado.nextInt();
                       switch (opc){
                           case 1:
                               System.out.println("Has seleccionado ALL IN");
                               System.out.println("-------------------------------------------");
                               apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                               mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                               break;
                           case 2:
                               System.out.println("Has selecionado ABANDONAR");
                               System.out.println("-------------------------------------------");
                               maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                               abandona = true;
                               break;
                           default: /*Si se equivoca mostrar mensaje de error*/
                               System.out.println("Error, debes introducir el número correcto de la opción que desees");
                       }
                   }
                   else { /*NO HAY ALL IN*/
                       do { /*BUCLE PARA DECISION DE JUGADOR */

                           jugador.contestaJugador();/*MOSTRAMOS EL MENU*/
                           opc = Util.leerOpcMenu(teclado, 5);
                           switch (opc) {
                               case 1:
                                   System.out.println("Has seleccionado VISUALIZAR MI MANO");
                                   System.out.println("-------------------------------------------");
                                   jugador.mostrarCartasDinero();
                                   break;
                               case 2:
                                   System.out.println("Has seleccionado VISUALIZAR MESA");
                                   System.out.println("-------------------------------------------");
                                   mesa.imprimirMano(fase);
                                   break;
                               case 3:
                                   System.out.println("Has selecionado IGUALAR");
                                   System.out.println("-------------------------------------------");
                                   apuestaJugador = jugador.igualar(apuestaMaquina); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                   mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                   break;
                               case 4:
                                   System.out.println("Has selecionado SUBIR");
                                   System.out.println("-------------------------------------------");
                                   apuestaJugador = jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                   mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                   break;
                               case 5:
                                   System.out.println("Has selecionado ABANDONAR");
                                   System.out.println("-------------------------------------------");
                                   /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                                   abandona = true;
                                   break;
                               default: /*Si se equivoca mostrar mensaje de error*/
                                   System.out.println("Error, debes introducir el número correcto de la opción que desees");
                           }
                       }
                       while (opc > 5 || opc < 3); /*Sale cuando la opcion sea 3-4-5*/
                   }
                   /*Comprobamos si el JUGADOR a subido la apuesta */
                   if(partida.comprobarAllIn(apuestaJugador,maquina.getDinero())) // HAY ALL IN
                   {
                       apuestaMaquina = maquina.getDinero(); // apostamos todo lo que le queda
                       mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la pauesta en la mesa
                       System.out.println();
                       System.out.println("Tu contrincante ha apostado ALL IN --> [ " + apuestaMaquina + " ]");
                       allInJugador = true;
                   }
                   if(apuestaMaquina<apuestaJugador) /*JUGADOR sube la apuesta*/ {
                       {
                           apuestaMaquina = maquina.obtenerCalidadMano(fase, apuestaJugador);
                           mesa.añadirDineroApuestas(apuestaMaquina);
                           System.out.println();
                           System.out.println("Tu contrincante ha igualado tu apuesta --> [ " + apuestaMaquina + " ]");

                       }
                   }
               }
                //############ RIVER DE MAQUINA FASE 4 #########################################################################################################################################################################################################
                if(!abandona)
                {
                    fase++;
                    apuestaJugador =0;
                    maquina.addCartaMesaToMano(mesa.getCartaFase(5));

                    System.out.println();
                    System.out.println("*******************************************");
                    System.out.println("FASE --> RIVER");
                    System.out.println("*******************************************");

                    apuestaMaquina = maquina.obtenerCalidadMano(fase,0);
                    mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la apuesta de la maquina sobre la mesa
                    System.out.println();
                    System.out.println("Tu contrincante ha apostado la siguiente cantidad --> [ " + apuestaMaquina + " ]");

                    //############ CONTESTA LA JUGADOR FASE 4 #############
                    /*Comprobamos si hay All In */
                    if(partida.comprobarAllIn(apuestaMaquina,jugador.getDinero())) // HAY ALL IN
                    {
                        jugador.decirAllIn(); // menu de decision de ALL IN
                        opc = teclado.nextInt();
                        switch (opc){
                            case 1:
                                System.out.println("Has seleccionado ALL IN");
                                System.out.println("-------------------------------------------");
                                apuestaJugador = jugador.getDinero(); /* apuesta todo su dinero*/
                                mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                break;
                            case 2:
                                System.out.println("Has selecionado ABANDONAR");
                                System.out.println("-------------------------------------------");
                                maquina.addDinero(mesa.getDinero()); /* Le damos el dinero de la mesa a la maquina*/
                                abandona = true;
                                break;
                            default: /*Si se equivoca mostrar mensaje de error*/
                                System.out.println("Error, debes introducir el número correcto de la opción que desees");
                        }
                    }
                    else { /*NO HAY ALL IN*/
                        do { /*BUCLE PARA DECISION DE JUGADOR */

                            jugador.contestaJugador();/*MOSTRAMOS EL MENU*/
                            opc = Util.leerOpcMenu(teclado, 5);
                            switch (opc) {
                                case 1:
                                    System.out.println("Has seleccionado VISUALIZAR MI MANO");
                                    System.out.println("-------------------------------------------");
                                    jugador.mostrarCartasDinero();
                                    break;
                                case 2:
                                    System.out.println("Has seleccionado VISUALIZAR MESA");
                                    System.out.println("-------------------------------------------");
                                    mesa.imprimirMano(fase);
                                    break;
                                case 3:
                                    System.out.println("Has selecionado IGUALAR");
                                    System.out.println("-------------------------------------------");
                                    apuestaJugador = jugador.igualar(apuestaMaquina); /*Al ser la primera ronda no hay opcion a realizar All IN*/
                                    mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                    break;
                                case 4:
                                    System.out.println("Has selecionado SUBIR");
                                    System.out.println("-------------------------------------------");
                                    apuestaJugador = jugador.apostar(teclado); /*Al ser la primera en hablar no hay opcion a realizar All IN*/
                                    mesa.añadirDineroApuestas(apuestaJugador);/* Ponemos el dinero en la mesa*/
                                    break;
                                case 5:
                                    System.out.println("Has selecionado ABANDONAR");
                                    System.out.println("-------------------------------------------");
                                    /*No pasamos dinero a la maquina porque al ser la primera fase no ha llegado a postar*/
                                    abandona = true;
                                    break;
                                default: /*Si se equivoca mostrar mensaje de error*/
                                    System.out.println("Error, debes introducir el número correcto de la opción que desees");
                            }
                        }
                        while (opc > 5 || opc < 3); /*Sale cuando la opcion sea 3-4-5*/
                    }
                    /*Comprobamos si el JUGADOR a subido la apuesta */
                    if(partida.comprobarAllIn(apuestaJugador,maquina.getDinero())) // HAY ALL IN
                    {
                        apuestaMaquina = maquina.getDinero(); // apostamos todo lo que le queda
                        mesa.añadirDineroApuestas(apuestaMaquina); // ponemos la pauesta en la mesa
                        System.out.println();
                        System.out.println("Tu contrincante ha apostado  AllIN --> [ " + apuestaMaquina + " ]");
                        allInJugador = true;
                    }
                    if(apuestaMaquina<apuestaJugador) /*JUGADOR sube la apuesta*/ {
                        {
                            apuestaMaquina = maquina.obtenerCalidadMano(fase, apuestaJugador);
                            mesa.añadirDineroApuestas(apuestaMaquina);
                            System.out.println();
                            System.out.println("Tu contrincante ha igualado tu apuesta --> [ " + apuestaMaquina + " ]");
                        }
                    }
                }
            }
            if(allInJugador){
                apuestaMaquina=maquina.getDinero();
                mesa.añadirDineroApuestas(apuestaMaquina);
            }
            if(allInMaquina){
                apuestaJugador=jugador.getDinero();
                mesa.añadirDineroApuestas(apuestaJugador);
            }
            switch (partida.comprobarGanador(jugador, maquina)) {
                case 0:
                    System.out.println("*** FIN DE LA RONDA ***");
                    archivoApuesta.delete();
                    jugador.menuContinuar();
                    opc = teclado.nextInt();
                    switch (opc) {
                        case 1:
                            System.out.println("CONTINUAMOS");
                            System.out.println("-------------------------------------------");
                            break;
                        case 2:
                            System.out.println("Has seleccionado FINALIZAR");
                            System.out.println("-------------------------------------------");
                            salir = true;
                            break;
                        default: /*Si se equivoca mostrar mensaje de error*/
                            System.out.println("Error, debes introducir el número correcto de la opción que desees");
                    }
                case 1:
                    System.out.println();
                    System.out.println("*** HAS PERDIDO, TU SALDO ES IGUAL A 0 ***");
                    registrador.registrarPartida(maquina.getNombre());
                    acabada=true;
                    break;
                case 2:
                    System.out.println();
                    System.out.println("*** HAS GANADO, EL SALDO DE TU CONTRINCANTE ES IGUAL A 0 ***");
                    registrador.registrarPartida(jugador.getNombre());
                    acabada=true;
                    break;
            }
        }
        while (!acabada && !salir);
    }
}
