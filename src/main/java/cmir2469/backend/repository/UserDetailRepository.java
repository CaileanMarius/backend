package cmir2469.backend.repository;

import cmir2469.backend.domain.UserDetail;
import cmir2469.backend.domain.validators.UserDetailValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDetailRepository  implements InterfaceRepository<Integer, UserDetail> {

    SessionFactory sessionFactory;
    UserDetailValidator userDetailValidator;

    public UserDetailRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        userDetailValidator = new UserDetailValidator();
    }


    @Override
    public UserDetail save(UserDetail userDetail) throws Validator.ValidationException {
        if(userDetail == null)
            throw new IllegalArgumentException();
        try{
            userDetailValidator.validate(userDetail);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(userDetail.getID()) != null)
            return  userDetail;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(userDetail);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public UserDetail delete(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        UserDetail userDetail = findOne(id);
        if (userDetail == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(userDetail);
            session.getTransaction().commit();
            return userDetail;
        }
    }

    @Override
    public UserDetail update(UserDetail entity) throws Validator.ValidationException {
        if (entity == null)
            throw new IllegalArgumentException();
        if (findOne(entity.getID()) == null)
            return entity;
        try {
            userDetailValidator.validate(entity);
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
    public UserDetail findOne(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<UserDetail> result = session.createQuery("select a from UserDetail a where ID=:id")
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
    public List<UserDetail> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<UserDetail> result = session.createQuery("select a from UserDetail a").list();
            session.getTransaction().commit();
            return result;
        }
    }

    public UserDetail findOneByFirstName(String  firstName) {
        if (firstName == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<UserDetail> result = session.createQuery("select a from UserDetail a where FirstName=:firstName")
                    .setParameter("firstName", firstName)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result.get(0);
            else
                return null;
        }
    }
}