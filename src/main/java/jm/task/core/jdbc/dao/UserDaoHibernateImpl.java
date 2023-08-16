package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import javax.persistence.Query;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory factory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Transaction tx = null;
        String sqlCommand = "CREATE TABLE IF NOT EXISTS usersTable(" +
                "id BIGINT NOT NULL AUTO_INCREMENT, name varchar(20) NOT NULL," +
                " lastName varchar(20) NOT NULL, age TINYINT NOT NULL, PRIMARY KEY (id))";
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery(sqlCommand).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction tx = null;
        String sqlCommand = "DROP TABLE IF EXISTS usersTable";
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery(sqlCommand).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Transaction tx = null;
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            User user = (User) session.get(User.class, id);
            session.delete(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list;
        Session session = factory.openSession();
        Query query = session.createQuery("FROM User", User.class);
        list = query.getResultList();
        session.close();
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Transaction tx = null;
        String hql = "delete from User";
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }
}
