package lesson05.multitrading;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
     CyclicBarrier cb ;
     CountDownLatch cdl;
     CountDownLatch cdl2;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    public Car(Race race, int speed,CyclicBarrier cb, CountDownLatch cdl, CountDownLatch cdl2){
        this(race, speed);
        this.cb = cb;
        this.cdl = cdl;
        this.cdl2=cdl2;

    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");

        } catch (Exception e) {
            e.printStackTrace();
        }
        cdl2.countDown();
            try {
                cb.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if(!MainClass.ab.getAndSet(true)) {
            System.out.println(this.name + " WIN!");
        }
        cdl.countDown();
    }
}
