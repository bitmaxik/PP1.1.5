package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory factory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Transaction tx = null;

        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            String sqlCommand = "CREATE TABLE IF NOT EXISTS usersTable(" +
                    "id BIGINT NOT NULL AUTO_INCREMENT, name varchar(20) NOT NULL," +
                    " lastName varchar(20) NOT NULL, age TINYINT NOT NULL, PRIMARY KEY (id))";
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
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            String sqlCommand = "DROP TABLE IF EXISTS usersTable";
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
        Transaction tx = null;
        List<User> list = new ArrayList<>();
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            list =  session.createSQLQuery("SELECT * FROM usersTable").addEntity(User.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Transaction tx = null;
        try (Session session = factory.getCurrentSession()) {
            tx = session.beginTransaction();
            String sqlCommand = "TRUNCATE TABLE usersTable";
            session.createSQLQuery(sqlCommand).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }
}
