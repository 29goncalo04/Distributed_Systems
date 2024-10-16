public class Main {
    public static void main(String[] args) {
        int ncorredores = 10;
        Barreira bar = new Barreira(ncorredores);

        Thread[] tv = new Thread[ncorredores];

        for (int i=0; i<ncorredores; i++) {
            tv[i] = new Thread(new Corredor(bar, i));
        }

        for (int i=0; i<ncorredores; i++) {
            tv[i].start();
        }
        try {
            for (int i=0; i<ncorredores; i++) {
                tv[i].join();
            }
        }
        catch (InterruptedException e) {}
        
        System.out.println("Fim da corrida.");
    }
}