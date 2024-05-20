package utilidades;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Util {
    public static int getNumber(int min, int max) {

        int aleatorioDefinitivo = (int) (Math.random() * (max - min + 1)) + min;
        return aleatorioDefinitivo;
    }

    public static int leerNumero(int min, int max) // para leer num entre 0 y 10
    {
        Scanner teclado = new Scanner(System.in);
        boolean incorrecto = false; // creamos una variable de tipo bolean. Solo sirve para decir si es cierto o no
        int num = teclado.nextInt();
        do {
            if (num < min)//
            {
                System.out.print("Error, número demasiado pequeño. Prueba de nuevo: ");
                num = teclado.nextInt();
                incorrecto = true;
            } else if (num > max) {
                System.out.print("Error, número demasiado grande. Prueba de nuevo:  ");
                num = teclado.nextInt();
                incorrecto = true;
            } else // lo ha ledio bien salimos del bucle
                incorrecto = false;
        }
        while (incorrecto);
        return num;
    }

    /**
     * Metodo para controlar el sueldo introducido por el jugador
     * @param teclado
     * @param saldo   --> para luego poder limitar el sueldo apostado, nunca mayor que sueldo
     * @return
     */
    public static int leerCantidad(Scanner teclado, int saldo) {
        int apuesta = 0;
        boolean lecturaCorrecta = false;
        do {
            System.out.println("Introduce la apuesta:");
            try
            {
                apuesta = teclado.nextInt(); // pasa el string a int si no puede es que hay letras, pasamos al catch
                if (apuesta <= 0)//
                {
                    System.out.print("Error, debe ser un número positivo. ");
                    lecturaCorrecta = false;

                } else if (apuesta > saldo) {
                    System.out.print("No puedes apostar más cantidad que la de tu saldo, que es [ "+saldo+" ]");
                    lecturaCorrecta = false;

                } else // lo lee correcto, salimos del bucle
                {
                    lecturaCorrecta = true;
                }
            }
            catch (InputMismatchException ime) // muestra el error que ha sucedidio
            {
                System.out.println("ERROR GRAVE: No has introducido un número.");
                teclado.nextLine();
                lecturaCorrecta = false;

            }
        }
        while (!lecturaCorrecta); // no sale hasta que intruzca un numero correcto

        return apuesta;
    }
    public static int leerOpcMenu(Scanner teclado, int numOpc)
    {
        boolean lecturaCorrecta = false;
        int opc = 0;
        do {
            try
            {
                opc = teclado.nextInt(); // pasa el string a int si no puede es que hay letras, pasamos al catch
                lecturaCorrecta = true;
            }
            catch (InputMismatchException ime) // muestra el error que ha sucedidio
            {
                System.out.println("ERROR GRAVE: No has introducido un número.");
                System.out.println("Vuelve a introducir la opción que deseas: ");
                teclado.nextLine();
                lecturaCorrecta = false;

            }
        }
        while (!lecturaCorrecta); // no sale hasta que intruzca un numero

        return opc;
    }

    /**
     * Metodo para hacer una pausa en el programa
     * @param segundos --> El tiempo que queremos que haya una pausa
     */
    public static void esperar(int segundos){
        try {
            Thread.sleep(segundos * 1000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}