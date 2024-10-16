public class Corredor implements Runnable{
    
    private Barreira bar;
    private int numero;

    public Corredor(Barreira b, int n) {
        this.bar = b;
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
        this.bar.cheguei();
        System.out.println("Entrei na corrida! Sou o " + this.numero + ".");
    }
}