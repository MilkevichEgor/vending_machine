import Exceptions.NotExistingDrinkExpection;

import dao.HibernateCoinsDao;
import entity.SodaMachine;
import entity.Coins;
import org.hibernate.SessionFactory;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int deposit;
        SodaMachine sodaMachine = new SodaMachine();
        Coins coins = new Coins();

        HibernateCoinsDao hibernateCoinsDao = new HibernateCoinsDao();

        while (true) {

            boolean isCanceled = false;

            try {

                while (!isCanceled) {

                    Scanner in = new Scanner(System.in);
                    System.out.println("Введите депозит");
                    deposit = in.nextInt();

                    if (deposit == 0) {
                        isCanceled = true;
                        break;
                    }

                    if (deposit == -1) {
                        coins.refund(sodaMachine.getDepositSum());
                        sodaMachine.setDepositSum(0);
                        continue;
                    }

                    if (!isCanceled && deposit != 1 && deposit != 2 && deposit != 5 &&
                            deposit != 10 && deposit != 50) {
                        System.out.println("Введите купюры номиналом 1, 2, 5, 10, 50 рублей");
                        continue;
                    }

                    coins.addCoins(deposit);
                    coins.saveCoinsToDB(deposit);
                    sodaMachine.increaseDepositSum(deposit);

                    System.out.printf("Внесенные деньги %d \n", sodaMachine.getDepositSum());
                }

            } catch (Exception e) {
                e.getMessage();
            }

            isCanceled = false;
            while (!isCanceled) {
                try {
                    Scanner in2 = new Scanner(System.in);
                    System.out.println("Введите название газировки");
                    String nameSoda = in2.nextLine().toLowerCase();

                    if (nameSoda.equals("exit")) {
                        isCanceled = true;
                        break;
                    }

                    sodaMachine.isDrinkAvailable(nameSoda);

                    if (sodaMachine.getDepositSum() == 0) {
                        System.out.println("Недостаточно денег");
                        break;
                    }

                    if (coins.checkDeposit(sodaMachine.sodaPrice.get(nameSoda), sodaMachine.getDepositSum())) {
                        sodaMachine.buySoda(nameSoda);
                        coins.giveChange(sodaMachine.getChange());
                    } else {
                        System.out.println("Извините, в автомате недостаточно монет для выдачи сдачи");
                    }
                    break;

                } catch (NotExistingDrinkExpection ex) {
                    System.out.println("Ошибка: " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Ошибка ввода: " + ex.getMessage());
                }

            }
        }

    }
}