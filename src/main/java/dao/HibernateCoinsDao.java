package dao;

import entity.Coins;
import entity.SodaMachine;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;
import java.util.List;

public class HibernateCoinsDao implements CoinsDao {

    private SessionFactory sessionFactory;

    @Override
    public void saveCoin(Coins coins) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(coins);
        tx1.commit();
        session.close();
    }

    public int getDepositSumFromDB() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Integer>query = session.createQuery("SELECT depositSum FROM SodaMachine", Integer.class);
        Integer result = query.uniqueResult();
        session.close();
        return result;
    }

    public void updateDepositSum(int depositSum) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("UPDATE SodaMachine SET depositSum = :depositSum");
        query.setParameter("depositSum", depositSum);
        query.executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public void updateAmount(Coins coins) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(coins);
        tx1.commit();
        session.close();
    }

    @Override
    public void deleteCoin(Coins coins) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(coins);
        tx1.commit();
        session.close();
    }

    public List<Coins> getAllCoins(String sortBy) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String queryString = "FROM Coins ORDER BY " + sortBy + " DESC";
        Query query = session.createQuery(queryString, Coins.class);
        List<Coins> coinsList = query.getResultList();
        session.close();
        return coinsList;
    }

}