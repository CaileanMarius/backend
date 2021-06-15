package cmir2469.backend.repository;

import cmir2469.backend.domain.UserTag;
import cmir2469.backend.domain.validators.UserTagValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserTagRepository implements InterfaceRepository<String, UserTag> {

    SessionFactory sessionFactory;
    UserTagValidator userTagValidator;

    public UserTagRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        userTagValidator = new UserTagValidator();
    }


    @Override
    public UserTag save(UserTag userTag) throws Validator.ValidationException {
        if(userTag == null)
            throw new IllegalArgumentException();
        try{
            userTagValidator.validate(userTag);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(userTag.getID()) != null)
            return  userTag;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(userTag);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public UserTag delete(String id) {
        if (id == null)
            throw new IllegalArgumentException();
        UserTag userTag = findOne(id);
        if (userTag == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(userTag);
            session.getTransaction().commit();
            return userTag;
        }
    }

    @Override
    public UserTag update(UserTag userTag) throws Validator.ValidationException {
        if (userTag == null)
            throw new IllegalArgumentException();
        if (findOne(userTag.getID()) == null)
            return userTag;
        try {
            userTagValidator.validate(userTag);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.update(userTag);
                session.getTransaction().commit();
                return null;
            }
        } catch (Validator.ValidationException e) {
            throw new Validator.ValidationException(e.getMessage());
        }
    }

    @Override
    public UserTag findOne(String id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<UserTag> result = session.createQuery("select a from UserTag a where ID=:id")
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
    public List<UserTag> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<UserTag> result = session.createQuery("select a from UserTag a").list();
            session.getTransaction().commit();
            return result;
        }
    }
}