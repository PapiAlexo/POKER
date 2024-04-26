public class Carta {
    protected int numero;
    protected Palo palo;

    /**
     * Constructor que recibe sus datos por param
     * @param numero
     * @param palo
     */
    public Carta(int numero, Palo palo) {
        this.numero = numero;
        this.palo = palo;
    }


    public void imprimir()
    {
        if(palo == Palo.PICAS)
        {
            System.out.println(" "+numero+" de PICAS");
            System.out.println("┌─────────────┐");
            if(numero>9)
            {
                System.out.println("│ " + numero + "          │");
            }
            else {
                System.out.println("│ " + numero + "           │");
            }
            System.out.println("│             │");
            System.out.println("│      ♠      │");
            System.out.println("│             │");
            if(numero>9)
            {
                System.out.println("│          " + numero + " │");
            }
            else {
                System.out.println("│           " + numero + " │");
            }
            System.out.println("└─────────────┘");
        }
        if(palo == Palo.DIAMANTES)
        {
            System.out.println(" "+numero+" de DIAMANTES");
            System.out.println("┌─────────────┐");
            if(numero>9)
            {
                System.out.println("│ " + numero + "          │");
            }
            else {
                System.out.println("│ " + numero + "           │");
            }
            System.out.println("│             │");
            System.out.println("│      ♦      │");
            System.out.println("│             │");
            if(numero>9)
            {
                System.out.println("│          " + numero + " │");
            }
            else {
                System.out.println("│           " + numero + " │");
            }
            System.out.println("└─────────────┘");

        }
        if(palo == Palo.TREBOL)
        {
            System.out.println(" "+numero+" de TREBOL");
            System.out.println("┌─────────────┐");
            if(numero>9)
            {
                System.out.println("│ " + numero + "          │");
            }
            else {
                System.out.println("│ " + numero + "           │");
            }
            System.out.println("│             │");
            System.out.println("│      ♣      │");
            System.out.println("│             │");
            if(numero>9)
            {
                System.out.println("│          " + numero + " │");
            }
            else {
                System.out.println("│           " + numero + " │");
            }
            System.out.println("└─────────────┘");
        }
        if(palo == Palo.CORAZONES)
        {
            System.out.println(" "+numero+" de CORAZONES");
            System.out.println("┌─────────────┐");
            if(numero>9)
            {
                System.out.println("│ " + numero + "          │");
            }
            else {
                System.out.println("│ " + numero + "           │");
            }
            System.out.println("│             │");
            System.out.println("│      ♥      │");
            System.out.println("│             │");
            if(numero>9)
            {
                System.out.println("│          " + numero + " │");
            }
            else {
                System.out.println("│           " + numero + " │");
            }
            System.out.println("└─────────────┘");

        }

    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Palo getPalo() {
        return palo;
    }

    public void setPalo(Palo palo) {
        this.palo = palo;
    }
}
