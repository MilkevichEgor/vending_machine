package dao;

import entity.Coins;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;

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

}