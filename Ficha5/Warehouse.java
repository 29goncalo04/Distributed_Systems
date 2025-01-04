import java.util.*;
import java.util.concurrent.locks.*;

// class Warehouse {
//     Lock lock = new ReentrantLock();
//     private Map<String, Product> map =  new HashMap<String, Product>();

//     private class Product { 
//         Condition condition = lock.newCondition();
//         int quantity = 0; 
//     }

//     private Product get(String item) {
//         lock.lock();
//         try{
//             Product p = map.get(item);
//             if (p != null){
//                 return p;
//             }
//             p = new Product();
//             map.put(item, p);
//             return p;
//         } finally{
//             lock.unlock();
//         }
//     }

//     public void supply(String item, int quantity) {
//         lock.lock();
//         try{
//             Product p = get(item);
//             p.quantity += quantity;
//             p.condition.signalAll();
//         } finally{
//             lock.unlock();
//         }
//     }

//     // Errado se faltar algum produto...
//     public void consume(Set<String> items) throws InterruptedException {
//         lock.lock();
//         try{
//             for (String s : items){
//                 Product p = get(s);
//                 while(p.quantity==0) p.condition.await();
//                 p.quantity--;
//             }
//         } finally{
//             lock.unlock();
//         }
//     }

// }







public class Warehouse {
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    private Map<String, Product> map =  new HashMap<String, Product>();

    private class Product {
        int quantity = 0;
    }

    private Product get(String item) {
        lock.lock();
        try{
            Product p = map.get(item);
            if (p != null) return p;
            p = new Product();
            map.put(item, p);
            return p;
        } finally{
            lock.unlock();
        }
    }

    public void supply(String item, int quantity) {
        lock.lock();
        try{
            Product p = get(item);
            p.quantity += quantity;
            condition.signalAll();
        } finally{
            lock.unlock();
        }
    }

    // Errado se faltar algum produto...
    public void consume(Set<String> items) {
        lock.lock();
        try{
            for (String s : items){
                if(get(s).quantity==0){
                    while(get(s).quantity==0) condition.await();
                    get(s).quantity--;
                }
            }
        } catch(Exception e){} 
        finally{
            lock.unlock();
        }
    }

}