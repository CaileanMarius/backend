package cmir2469.backend.repository;


import cmir2469.backend.domain.User;
import cmir2469.backend.domain.validators.UserValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserRepository  implements InterfaceRepository<Integer, User> {

    SessionFactory sessionFactory;
    UserValidator userValidator;

    public UserRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        userValidator = new UserValidator();
    }


    @Override
    public User save(User user) throws Validator.ValidationException {
        if(user == null)
            throw new IllegalArgumentException();
        try{
            userValidator.validate(user);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(user.getID()) != null)
            return  user;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public User delete(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        User user = findOne(id);
        if (user == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public User update(User entity) throws Validator.ValidationException {
        if (entity == null)
            throw new IllegalArgumentException();
        if (findOne(entity.getID()) == null)
            return entity;
        try {
            userValidator.validate(entity);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.update(entity);
                session.getTransaction().commit();
                return null;
            }
        } catch (Validator.ValidationException e) {
            throw new Validator.ValidationException(e.getMessage());
        }
    }

    @Override
    public User findOne(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> result = session.createQuery("select a from User a where ID=:id")
                    .setParameter("id", id)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result.get(0);
            else
                return null;
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> result = session.createQuery("select a from User a").list();
            session.getTransaction().commit();
            return result;
        }
    }


    public User findOneByUsername(String username) {
        if (username == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> result = session.createQuery("select a from User a where Username=:username")
                    .setParameter("username", username)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result.get(0);
            else
                return null;
        }
    }

}
