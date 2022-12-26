public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyThread thread1 = new MyThread(0, 0);
        MyThread thread2 = new MyThread(1, 1);
        thread1.start();
        thread2.start();
        Thread.sleep(5000000);
        thread1.flag = false;
        thread1.join(); // ждет завершение потока
    }

    public static final Object KEY = new Object();
    static int bank=0;
    public static void test(int message, int k) {

        synchronized(KEY) {
            try {
                System.out.print("Текущий счет:");
                System.out.println(bank);
                Thread.sleep(2000);
                if (k==0){
                    System.out.print("Вы хотите прибавить к счету:");
                    System.out.println(message);
                    bank+=message;
                }
                else{
                    System.out.print("Вы хотите снять со счета:");
                    System.out.println(message);
                    if ((bank-message)<0){
                        throw new NotEnoughMoneyOnAccountException("У вас было недостаточно средств на счёте, чтобы снять сумму указанную выше");
                    }
                    bank-=message;
                }

                Thread.sleep(2000);
                KEY.notify(); // возобновляем поток, наход. в режиме ожидания
                KEY.wait(); // выставляем в режим ожидания
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NotEnoughMoneyOnAccountException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
class MyThread extends Thread {
    int mess;
    Double bank;
    int K;
    boolean flag = true;
    MyThread(int m, int k){
        m=getRandomDoubleBetweenRange(0, 1000);
        mess = m;
        K=k;
    }
    public static int getRandomDoubleBetweenRange(int min, int max){
        int x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }
    @Override
    public void run() {
        while(flag) {
            Main.test(getRandomDoubleBetweenRange(0, 1000), K);
        }
    }
}
class NotEnoughMoneyOnAccountException extends Exception{
    public NotEnoughMoneyOnAccountException(String message) { super(message); }
}