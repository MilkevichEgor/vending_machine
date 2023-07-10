package entity;

import dao.HibernateCoinsDao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table (name = "coins")
 public class Coins {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "denomination")
    private int denomination;

    @Column(name = "amount")
    private int amount;

    public int getId() {
        return id;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCoinsAmountByNominal(int denomination) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query = session.createQuery("SELECT amount FROM Coins WHERE denomination = :denomination");
        query.setParameter("denomination", denomination);
        Integer result = (Integer) query.uniqueResult();
        System.out.println(result);
        return result;
    }

    public void updateAmount(int denomination, int amount) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = null;

        try {
            tx1 = session.beginTransaction();
            Query query = session.createQuery("UPDATE Coins SET amount = :amount WHERE denomination = :denomination");
            query.setParameter("amount", amount + 1);
            query.setParameter("denomination", denomination);
            query.executeUpdate();

            tx1.commit();
        } catch (HibernateException e) {
            if (tx1 != null) {
                tx1.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Transient
    private int oneRubles = 0;
    @Transient
    private int twoRubles = 0;
    @Transient
    private int fiveRubles = 0;
    @Transient
    private int tenRubles = 0;
    @Transient
    private int fiftyRubles = 0;

    @Transient
    int[] coinsValues = {50, 10, 5, 2, 1};
    @Transient
    int[] numberOfCoins = {this.fiftyRubles, this.tenRubles, this.fiveRubles, this.twoRubles, this.oneRubles};
    @Transient
    int[] changeCoins = new int[coinsValues.length];

    public Coins() {}
    public void addCoins(int coinDenomination) {

        for (int i = 0; i < coinsValues.length; i++) {
            if (coinDenomination == coinsValues[i]) {
                numberOfCoins[i]++;
//                saveCoinsToDB(coinDenomination);
                break;
            }
        }
    }

    public void saveCoinsToDB(int denomination) {
        int amount = getCoinsAmountByNominal(denomination);
        updateAmount(denomination, amount);
    }

    public boolean checkDeposit(int price, int sum) {
        int[] checkCoins = new int[coinsValues.length];
        int change = sum - price;

        if (change < 0) {
            return false;
        }

        for (int i = 0; i < numberOfCoins.length; i++) {
            while (change >= coinsValues[i] && numberOfCoins[i] > checkCoins[i]) {

                change -= coinsValues[i];
                checkCoins[i]++;
            }
        }
        return change == 0;
    }

    public void refund(int sum) {
        this.giveChange(sum);
    }

    public void giveChange(int change) {
        int remainingChange = change;

        if (change == 0) {
            System.out.println("Сдача не требуется");
            return;
        }

        for (int i = 0; i < coinsValues.length; i++) {
            while (remainingChange >= coinsValues[i]) {
                if (numberOfCoins[i] == 0) {
                    break;
                }
                remainingChange -= coinsValues[i];
                numberOfCoins[i]--;
                changeCoins[i]++;
            }
        }

        if (remainingChange == 0) {
            System.out.println("Сдача выдана:");
            for (int i = 0; i < changeCoins.length; i++) {
                if (changeCoins[i] > 0) {
                    System.out.printf("%d монета(ы) номиналом %d руб.\n", changeCoins[i], coinsValues[i]);
                }
            }

            for (int i = 0; i < numberOfCoins.length; i++) {
                if (numberOfCoins[i] > 0) {
                    System.out.printf("В автомате осталось %d монет(ы) номиналом %d руб.\n", numberOfCoins[i], coinsValues[i]);
                }
            }
            Arrays.fill(changeCoins, 0);

        } else {
            System.out.println("Извините, в автомате недостаточно монет для выдачи сдачи");
        }
    }
}
