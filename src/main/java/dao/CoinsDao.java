package dao;

import entity.Coins;

public interface CoinsDao {
    void saveCoin(Coins coins);

    void updateAmount(Coins coins);

    void deleteCoin(Coins coins);
}
