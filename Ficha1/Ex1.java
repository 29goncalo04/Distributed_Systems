import java.util.ArrayList;

// public class Ex1 {
//     public static void main(String[] args){
//         int n = 10;
//         ArrayList<Thread> list = new ArrayList<>();

//         for(int i=0; i<n; i++){
//             Thread t = new Thread(new Increment());
//             t.start();
//             list.add(t);
//         }

//         for(Thread t : list){
//             try {
//                 t.join();
//             } catch (Exception e) {
//                 throw new RuntimeException(e);
//             }
//         }

//         System.out.println("Fim");
//     }
// }






public class Ex1{
    public static void main(String[] args){
        int N = 10;
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for(int i = 0; i<N; i++){
            Thread t = new Thread (new Increment());
            threads.add(t);
            t.start();
        }
        for(Thread t : threads){
            try{
                t.join();
            } catch (Exception e){}
        }
        System.out.println("Fim");
    }
}