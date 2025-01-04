public class Corredor implements Runnable{
    
    //private Barreira bar;
    private Agreement agreement;
    private int numero;

    public Corredor(Agreement a, int n) {
        this.agreement = a;
        this.numero = n;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.numero * 1000);
            System.out.println("Sou o " + this.numero + " e estou à espera de entrar.");
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(this.agreement.propose(numero));
        //System.out.println("Entrei na corrida! Sou o " + this.numero + ".");
    }
}