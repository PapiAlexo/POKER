import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class Registrador {
    protected File archivoJugador, archivoMaquina;
    public Registrador(File archivoJugador, File archivoMaquina) {
        this.archivoMaquina=archivoMaquina;
        this.archivoJugador=archivoJugador;
    }
    /**
     * Añade al archivo de registros de partidas un registro de victoria
     * @param nombreGanador //Introducimos el jugador que ha ganado
     * @throws IOException
     */
    public void registrarPartida(String nombreGanador) {
        try {
            boolean primeraPasadaMaquina=false; //bandera que sirve para configurar fecha y hora de partidas si ha sido su primera partida
            boolean primeraPasadaJugador=false;
            int contador = 0; //numero de victorias
            String conjuntoLineasMaquina=""; //conjunto de lineas que guardan las fechas y horas
            String conjuntoLineasJugador="";
            if (nombreGanador.equals("Máquina")) { //gana la máquina
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoMaquina))) { //lector de archivo
                    String linea;
                    String segundaLinea;
                    boolean salir=false;
                    while (!salir && (linea = reader.readLine()) != null) { //actúa mientras el archivo no sea nulo (a partir del segundo registro, ya que en el primero los archivos comienzan vacíos)
                        linea = reader.readLine(); //lee línea a línea
                        segundaLinea = linea;
                        char primerCaracter = segundaLinea.charAt(0); //obtiene el primer caracter con el objetivo de determinar el número de victorias
                        char segundoCaracter = segundaLinea.charAt(1); //obtiene el segundo caracter con el objetivo de determinar el número de victorias
                        if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) { //si tiene menos de 10 victorias obtiene el primer caracter
                            contador = Character.getNumericValue(primerCaracter); //actualiza las victorias
                        }
                        if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) { //si tiene 10 o más victorias obtiene los dos primeros caracteres
                            int primerDigito = Character.getNumericValue(primerCaracter)*10;
                            contador=primerDigito+Character.getNumericValue(segundoCaracter); //actualiza las victorias
                        }
                        if(segundaLinea.contains("Partidas")) {
                            while(!salir) { //bucle para recopilar todas las fechas y horas
                                linea=reader.readLine();
                                if(linea==null || linea.isEmpty()) { //si está vacío significa que es el primer registro(pasada), por lo que sale y activa la bandera de primera pasada
                                    salir=true;
                                    primeraPasadaMaquina=true;
                                }
                                if(!salir){ //si no es la primera pasada, el bucle va leyendo todas las líneas de fechas y horas y las guarda en una variable
                                    conjuntoLineasMaquina+=linea+"\n";
                                }
                            }
                        }
                    }
                } catch (IOException | NumberFormatException e) {}

                String registro = (contador + 1) + " rondas ganadas, con un porcentaje de " +obtenerPorcentajeVictoriasMaquina()+ "%\n"; //segunda línea que contiene las victorias que lleva y las actualiza + el porcentaje de victorias
                conjuntoLineasMaquina+=getFechaFormateada()+" a las "+getHoraFormateada(); //añade una nueva fecha y hora de la partida recién terminada
                if(!primeraPasadaMaquina){ //en caso de que sea el primer registro, no añade ninguna fecha, solo escribe la primera del archivo (solo se usa en el primer registro)
                    conjuntoLineasMaquina=getFechaFormateada()+" a las "+getHoraFormateada();
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoMaquina))) {
                    writer.write("Máquina\n"+registro+"========================================\nPartidas: \n"+conjuntoLineasMaquina+"\n"); //escribe todoEn el archivo
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoJugador))) { //una vez calculados los porcentajes de la máquina, iniciamos el archivo del jugador en base a esas estadísticas (hace lo mismo pero algunas variables cambian de nombre para que pertenezcan solo al jugador)
                    String linea;
                    String segundaLinea;
                    boolean salir=false;
                    while (!salir && (linea = reader.readLine()) != null) {
                        linea = reader.readLine();
                        segundaLinea = linea;
                        char primerCaracter = segundaLinea.charAt(0);
                        char segundoCaracter = segundaLinea.charAt(1);
                        if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                            contador = Character.getNumericValue(primerCaracter);
                        }
                        if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                            int primerDigito = Character.getNumericValue(primerCaracter)*10;
                            contador=primerDigito+Character.getNumericValue(segundoCaracter);
                        }
                        if(segundaLinea.contains("Partidas")) {
                            while(!salir) {
                                linea=reader.readLine();
                                if(linea==null || linea.isEmpty()) {
                                    salir=true;
                                    primeraPasadaJugador=true;
                                }
                                if(!salir){
                                    conjuntoLineasJugador+=linea+"\n";
                                }
                            }
                        }
                    }
                } catch (IOException | NumberFormatException e) {}
                registro = contador + " rondas ganadas, con un porcentaje de " +obtenerPorcentajeVictoriasJugadorEnRegistroMaquina()+ "%\n";
                conjuntoLineasJugador+=getFechaFormateada()+" a las "+getHoraFormateada();
                if(!primeraPasadaJugador){
                    conjuntoLineasJugador=getFechaFormateada()+" a las "+getHoraFormateada();
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoJugador))) {
                    writer.write("Jugador\n"+registro+"========================================\nPartidas: \n"+conjuntoLineasJugador+"\n");
                }
            }
            else {
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoJugador))) { //ha ganado el jugador (lo mismo que hay comentado anteriormente pero aplicado al archivo del jugador)
                    String linea;
                    String segundaLinea;
                    boolean salir=false;
                    while (!salir && (linea = reader.readLine()) != null) {
                        linea = reader.readLine();
                        segundaLinea = linea;
                        char primerCaracter = segundaLinea.charAt(0);
                        char segundoCaracter = segundaLinea.charAt(1);
                        if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                            contador = Character.getNumericValue(primerCaracter);
                        }
                        if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                            int primerDigito = Character.getNumericValue(primerCaracter)*10;
                            contador=primerDigito+Character.getNumericValue(segundoCaracter);
                        }
                        if(segundaLinea.contains("Partidas")) {
                            while(!salir) {
                                linea=reader.readLine();
                                if(linea==null || linea.isEmpty()) {
                                    salir=true;
                                    primeraPasadaJugador=true;
                                }
                                if(!salir){
                                    conjuntoLineasJugador+=linea+"\n";
                                }
                            }
                        }
                    }
                } catch (IOException | NumberFormatException e) {}
                String registro = (contador + 1) + " rondas ganadas, con un porcentaje de " +obtenerPorcentajeVictoriasJugador()+ "%\n";
                conjuntoLineasJugador+=getFechaFormateada()+" a las "+getHoraFormateada();
                if(!primeraPasadaJugador){
                    conjuntoLineasJugador=getFechaFormateada()+" a las "+getHoraFormateada();
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoJugador))) {
                    writer.write("Jugador\n"+registro+"========================================\nPartidas: \n"+conjuntoLineasJugador+"\n");
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoMaquina))) { //una vez calculados los porcentajes del jugador, iniciamos el archivo de la máquina en base a esas estadísticas (hace lo mismo pero algunas variables cambian de nombre para que pertenezcan solo a la máquina)
                    String linea;
                    String segundaLinea;
                    boolean salir=false;
                    while (!salir && (linea = reader.readLine()) != null) {
                        linea = reader.readLine();
                        segundaLinea = linea;
                        char primerCaracter = segundaLinea.charAt(0);
                        char segundoCaracter = segundaLinea.charAt(1);
                        if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                            contador = Character.getNumericValue(primerCaracter);
                        }
                        if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                            int primerDigito = Character.getNumericValue(primerCaracter)*10;
                            contador=primerDigito+Character.getNumericValue(segundoCaracter);
                        }
                        if(segundaLinea.contains("Partidas")) {
                            while(!salir) {
                                linea=reader.readLine();
                                if(linea==null || linea.isEmpty()) {
                                    salir=true;
                                    primeraPasadaMaquina=true;
                                }
                                if(!salir){
                                    conjuntoLineasMaquina+=linea+"\n";
                                }
                            }
                        }
                    }
                } catch (IOException | NumberFormatException e) {}
                registro = contador + " rondas ganadas, con un porcentaje de " +obtenerPorcentajeVictoriasMaquinaEnRegistroJugador()+ "%\n";
                conjuntoLineasMaquina+=getFechaFormateada()+" a las "+getHoraFormateada();
                if(!primeraPasadaMaquina){
                    conjuntoLineasMaquina=getFechaFormateada()+" a las "+getHoraFormateada();
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoMaquina))) {
                    writer.write("Máquina\n"+registro+"========================================\nPartidas: \n"+conjuntoLineasMaquina+"\n");
                }
            }
        } catch (IOException | NumberFormatException e) {}
    }
    /**
     * obtiene el porcentaje de la máquina si ha ganado ella
     * @return porcentaje
     */
    public double obtenerPorcentajeVictoriasMaquina() {
        double victoriasMaquina=0;
        double victoriasJugador=0;
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoMaquina))) { //lector de número de victorias de la máquina (hace lo mismo que en la función de registrar)
            String linea;
            String segundaLinea;
            boolean salir=false;
            while (!salir && (linea = reader.readLine()) != null) {
                linea = reader.readLine();
                segundaLinea = linea;
                char primerCaracter = segundaLinea.charAt(0);
                char segundoCaracter = segundaLinea.charAt(1);
                if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                    victoriasMaquina=Character.getNumericValue(primerCaracter);
                }
                if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                    int primerDigito = Character.getNumericValue(primerCaracter)*10;
                    victoriasMaquina=primerDigito+Character.getNumericValue(segundoCaracter);
                }
                if(segundaLinea.contains("Partidas")) {
                    salir=true;
                }
            }
        } catch (IOException | NumberFormatException e) {}
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoJugador))) { //lector de número de victorias del jugador
            String linea;
            String segundaLinea;
            boolean salir=false;
            while (!salir && (linea = reader.readLine()) != null) {
                linea = reader.readLine();
                segundaLinea = linea;
                char primerCaracter = segundaLinea.charAt(0);
                char segundoCaracter = segundaLinea.charAt(1);
                if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                    victoriasJugador=Character.getNumericValue(primerCaracter);
                }
                if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                    int primerDigito = Character.getNumericValue(primerCaracter)*10;
                    victoriasJugador=primerDigito+Character.getNumericValue(segundoCaracter);
                }
                if(segundaLinea.contains("Partidas")) {
                    salir=true;
                }
            }
        } catch (IOException | NumberFormatException e) {}
        victoriasMaquina++;
        double partidasJugadas=victoriasMaquina+victoriasJugador;
        double porcentaje=(victoriasMaquina/partidasJugadas)*100; //hace la media
        return porcentaje;
    }
    /**
     * obtiene el porcentaje de la máquina si ha ganado el jugador
     * @return porcentaje
     */
    public double obtenerPorcentajeVictoriasMaquinaEnRegistroJugador() { //es igual que el anterior pero no le suma victoria, solo actualiza su porcentaje tras la victoria del jugador
        double victoriasMaquina=0;
        double victoriasJugador=0;
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoMaquina))) {
            String linea;
            String segundaLinea;
            boolean salir=false;
            while (!salir && (linea = reader.readLine()) != null) {
                linea = reader.readLine();
                segundaLinea = linea;
                char primerCaracter = segundaLinea.charAt(0);
                char segundoCaracter = segundaLinea.charAt(1);
                if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                    victoriasMaquina=Character.getNumericValue(primerCaracter);
                }
                if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                    int primerDigito = Character.getNumericValue(primerCaracter)*10;
                    victoriasMaquina=primerDigito+Character.getNumericValue(segundoCaracter);
                }
                if(segundaLinea.contains("Partidas")) {
                    salir=true;
                }
            }
        } catch (IOException | NumberFormatException e) {}
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoJugador))) {
            String linea;
            String segundaLinea;
            boolean salir=false;
            while (!salir && (linea = reader.readLine()) != null) {
                linea = reader.readLine();
                segundaLinea = linea;
                char primerCaracter = segundaLinea.charAt(0);
                char segundoCaracter = segundaLinea.charAt(1);
                if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                    victoriasJugador=Character.getNumericValue(primerCaracter);
                }
                if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                    int primerDigito = Character.getNumericValue(primerCaracter)*10;
                    victoriasJugador=primerDigito+Character.getNumericValue(segundoCaracter);
                }
                if(segundaLinea.contains("Partidas")) {
                    salir=true;
                }
            }
        } catch (IOException | NumberFormatException e) {}
        double partidasJugadas=victoriasMaquina+victoriasJugador;
        double porcentaje=(victoriasMaquina/partidasJugadas)*100;
        return porcentaje;
    }
    /**
     * obtiene el porcentaje del jugador si ha ganado él
     * @return porcentaje
     */
    public double obtenerPorcentajeVictoriasJugador() { //lo mismo que antes pero para el jugador
        double victoriasMaquina=0;
        double victoriasJugador=0;
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoMaquina))) {
            String linea;
            String segundaLinea;
            boolean salir=false;
            while (!salir && (linea = reader.readLine()) != null) {
                linea = reader.readLine();
                segundaLinea = linea;
                char primerCaracter = segundaLinea.charAt(0);
                char segundoCaracter = segundaLinea.charAt(1);
                if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                    victoriasMaquina=Character.getNumericValue(primerCaracter);
                }
                if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                    int primerDigito = Character.getNumericValue(primerCaracter)*10;
                    victoriasMaquina=primerDigito+Character.getNumericValue(segundoCaracter);
                }
                if(segundaLinea.contains("Partidas")) {
                    salir=true;
                }
            }
        } catch (IOException | NumberFormatException e) {}
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoJugador))) {
            String linea;
            String segundaLinea;
            boolean salir=false;
            while (!salir && (linea = reader.readLine()) != null) {
                linea = reader.readLine();
                segundaLinea = linea;
                char primerCaracter = segundaLinea.charAt(0);
                char segundoCaracter = segundaLinea.charAt(1);
                if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                    victoriasJugador=Character.getNumericValue(primerCaracter);
                }
                if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                    int primerDigito = Character.getNumericValue(primerCaracter)*10;
                    victoriasJugador=primerDigito+Character.getNumericValue(segundoCaracter);
                }
                if(segundaLinea.contains("Partidas")) {
                    salir=true;
                }
            }
        } catch (IOException | NumberFormatException e) {}
        victoriasJugador++;
        double partidasJugadas=victoriasJugador+victoriasMaquina;
        double porcentaje=(victoriasJugador/partidasJugadas)*100;
        return porcentaje;
    }
    /**
     * obtiene el porcentaje del jugador si ha ganado la máquina
     * @return porcentaje
     */
    public double obtenerPorcentajeVictoriasJugadorEnRegistroMaquina() {
        double victoriasMaquina=0;
        double victoriasJugador=0;
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoMaquina))) {
            String linea;
            String segundaLinea;
            boolean salir=false;
            while (!salir && (linea = reader.readLine()) != null) {
                linea = reader.readLine();
                segundaLinea = linea;
                char primerCaracter = segundaLinea.charAt(0);
                char segundoCaracter = segundaLinea.charAt(1);
                if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                    victoriasMaquina=Character.getNumericValue(primerCaracter);
                }
                if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                    int primerDigito = Character.getNumericValue(primerCaracter)*10;
                    victoriasMaquina=primerDigito+Character.getNumericValue(segundoCaracter);
                }
                if(segundaLinea.contains("Partidas")) {
                    salir=true;
                }
            }
        } catch (IOException | NumberFormatException e) {}
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoJugador))) {
            String linea;
            String segundaLinea;
            boolean salir=false;
            while (!salir && (linea = reader.readLine()) != null) {
                linea = reader.readLine();
                segundaLinea = linea;
                char primerCaracter = segundaLinea.charAt(0);
                char segundoCaracter = segundaLinea.charAt(1);
                if (Character.isDigit(primerCaracter) && !Character.isDigit(segundoCaracter)) {
                    victoriasJugador=Character.getNumericValue(primerCaracter);
                }
                if(Character.isDigit(primerCaracter) && Character.isDigit(segundoCaracter)) {
                    int primerDigito = Character.getNumericValue(primerCaracter)*10;
                    victoriasJugador=primerDigito+Character.getNumericValue(segundoCaracter);
                }
                if(segundaLinea.contains("Partidas")) {
                    salir=true;
                }
            }
        } catch (IOException | NumberFormatException e) {}
        double partidasJugadas=victoriasJugador+victoriasMaquina;
        double porcentaje=(victoriasJugador/partidasJugadas)*100;
        return porcentaje;
    }
    /**
     * Formatea la fecha para que salga en formato español
     * @return la fechaw en un formato presentable
     */
    public String getFechaFormateada() {
        Date fechaPartida=new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        return formatoFecha.format(fechaPartida);
    }
    /**
     * Formatea la hora para que salga en formato entendible
     * @return la hora en un formato presentable
     */
    public String getHoraFormateada() {
        Date fechaPartida=new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("HH:mm:ss", new Locale("es", "ES"));
        return formatoFecha.format(fechaPartida);
    }
}
