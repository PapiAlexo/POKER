import java.util.ArrayList;
public class Mesa {
    protected ArrayList<Carta> mano;
    protected int dinero;
    public Mesa(int dinero) {
        this.mano = new ArrayList<Carta>();
        this.dinero = dinero;
    }
    /**
     * imprime el dineroa ctual de la mesa
     */
    public void imprimirDinero() {
        System.out.println(dinero);
    }
    /**
     * imprime las cartas que hay en la mesa segun la ronda en la que esté
     */
    public void imprimirMano(int fase) {
        if(fase==1)
        {
            System.out.println("No se puede mostrar ya que es la CIEGA. No hay cartas aun en al mesa.");
        }
        if(fase==2) {
            for(int i=0; i<2; i++) {
                Carta cartaActual = mano.get(i);
                cartaActual.imprimir();
            }
        }
        if(fase==3) {
            for(int i=0; i<3; i++) {
                Carta cartaActual = mano.get(i);
                cartaActual.imprimir();
            }
        }
        if(fase==4) {
            for(int i=0; i<mano.size(); i++) {
                Carta cartaActual = mano.get(i);
                cartaActual.imprimir();
            }
        }
    }

    /**
     * añade carta a la mano
     * @param carta
     */
    public boolean addCarta(Carta carta){
        mano.add(carta);
        return  true;
    }
    /**
     * limpia las cartas que haya en la mesa al terminar la ronda
     */
    public void cleanMano(){
        for (int i=0;i<mano.size();i++){
            mano.remove(0);
        }
    }
    /**
     * obtiene el dinero actual que hay en la mesa (se tendrá que usar para darselo al ganador)
     * @return
     */
    public int getDinero() {
        return this.dinero;
    }


    /**
     * Metodo para inseertar apuesta en la mesa
     * @param apuesta
     */
    public void añadirDineroApuestas(int apuesta) {
        this.dinero+=apuesta;
    }


    /**
     * obtiene las cartas que hay en la mesa (necesario para la máquina)
     * @return
     */
    public ArrayList<Carta> getMano() {
        return mano;
    }
    /**
     * una vez terminada la ronda, vacía el dinero que hay en la mesa (después de haberselo dado al ganador)
     */
    public void limpiarDineroMesa() {
        this.dinero=0;
    }
}

