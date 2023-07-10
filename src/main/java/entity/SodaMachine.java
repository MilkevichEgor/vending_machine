package entity;


import Exceptions.NotExistingDrinkExpection;
import java.util.HashMap;
import java.util.Map;

 public class SodaMachine {
    private int change = 0;
    private int depositSum = 0;

    public Map<String, Integer> sodaPrice = new HashMap<String, Integer>() {{
        put("cola", 5);
        put("7up", 6);
        put("redbull", 9);
    }};

    public boolean isDrinkAvailable(String name) throws NotExistingDrinkExpection {
        if (!sodaPrice.containsKey(name)) {
            throw new NotExistingDrinkExpection("Введите напиток который есть в продаже");
        }
//        if (existing drink not enough) {
//            throw new NotEnoughDrinkExpection("Напитка нет в наличии");
//        }
        return false;
    }

    public void buySoda(String name) {
        change = 0;
        Integer price = sodaPrice.get(name);
        if (price != null) {
            if (depositSum <= 0) {
                System.out.println("Недостаточно денег");
            }
            if (depositSum >= price) {
                change = depositSum - price;
                System.out.printf("Возьмите %s \n", name);
                depositSum = 0;
            } else {
                System.out.println("Напиток не найден");
            }
        }
    }

    public void increaseDepositSum(int deposit) {
        this.depositSum += deposit;
    }

    public int getDepositSum() {
        return depositSum;
    }

    public void setDepositSum(int depositSum) {
        this.depositSum = depositSum;
    }

    public int getChange() {
        return change;
    }
}
