package entity;

import Exceptions.NotExistingDrinkExpection;
import dao.HibernateCoinsDao;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table (name = "soda_machine")
 public class SodaMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "deposit_sum")
    private int depositSum = 0;

    @Transient
    private int change = 0;
    public SodaMachine(int deposit) {
        this.depositSum = deposit;
    }

    @ElementCollection
    @Transient
    public Map<String, Integer> sodaPrice = new HashMap<String, Integer>() {{
        put("cola", 5);
        put("7up", 6);
        put("redbull", 9);
    }};

    @Transient
    HibernateCoinsDao hibernateCoinsDao = new HibernateCoinsDao();

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
//                depositSum = 0;
                this.flushDepositSum();
            } else {
                System.out.println("Напиток не найден");
            }
        }
    }

    public void increaseDepositSum(int deposit) {
        // call db update
        this.depositSum += deposit;
        hibernateCoinsDao.updateDepositSum(depositSum);
    }

    public void flushDepositSum() {
        // call db update
        this.depositSum = 0;
        hibernateCoinsDao.updateDepositSum(depositSum);
    }

    public int getDepositSum() {
        return depositSum;
    }

    public void setDepositSum(int depositSum) {
        // call db update
        this.depositSum = depositSum;
        hibernateCoinsDao.updateDepositSum(depositSum);
    }

    public int getChange() {
        return change;
    }
}
