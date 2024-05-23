package baraja;

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
            if(numero == 11)
            {
                System.out.println("J de PICAS");
            }
            else if(numero == 12)
            {
                System.out.println("Q de PICAS");
            }
            else if(numero == 13)
            {
                System.out.println("K de PICAS");
            }
            else if(numero == 1)
            {
                System.out.println("A de PICAS");
            }
            else
            {
                System.out.println(numero+" de PICAS");
            }

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
            if(numero == 11)
            {
                System.out.println("J de DIAMANTES");
            }
            else if(numero == 12)
            {
                System.out.println("Q de DIAMANTES");
            }
            else if(numero == 13)
            {
                System.out.println("K de DIAMANTES");
            }
            else if(numero == 1)
            {
                System.out.println("A de DIAMANTES");
            }
            else
            {
                System.out.println(numero+" de DIAMANTES");
            }
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
            if(numero == 11)
            {
                System.out.println("J de TRÉBOL");
            }
            else if(numero == 12)
            {
                System.out.println("Q de TRÉBOL");
            }
            else if(numero == 13)
            {
                System.out.println("K de TRÉBOL");
            }
            else if(numero == 1)
            {
                System.out.println("A de TRÉBOL");
            }
            else
            {
                System.out.println(numero+" de TRÉBOL");
            }
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
            if(numero == 11)
            {
                System.out.println("J de CORAZONES");
            }
            else if(numero == 12)
            {
                System.out.println("Q de CORAZONES");
            }
            else if(numero == 13)
            {
                System.out.println("K de CORAZONES");
            }
            else if(numero == 1)
            {
                System.out.println("A de CORAZONES");
            }
            else
            {
                System.out.println(numero+" de CORAZONES");
            }
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
