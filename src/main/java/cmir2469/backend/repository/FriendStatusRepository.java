package cmir2469.backend.repository;

import cmir2469.backend.domain.FriendStatus;
import cmir2469.backend.domain.User;
import cmir2469.backend.domain.validators.FriendStatusValidator;
import cmir2469.backend.domain.validators.UserValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class FriendStatusRepository implements InterfaceRepository<Integer, FriendStatus> {

    SessionFactory sessionFactory;
    FriendStatusValidator friendStatusValidator;

    public FriendStatusRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        friendStatusValidator = new FriendStatusValidator();
    }


    @Override
    public FriendStatus save(FriendStatus friendStatus) throws Validator.ValidationException {
        if(friendStatus == null)
            throw new IllegalArgumentException();
        try{
            friendStatusValidator.validate(friendStatus);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(friendStatus.getID()) != null)
            return  friendStatus;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(friendStatus);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public FriendStatus delete(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        FriendStatus friendStatus = findOne(id);
        if (friendStatus == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(friendStatus);
            session.getTransaction().commit();
            return friendStatus;
        }
    }

    @Override
    public FriendStatus update(FriendStatus friendStatus) throws Validator.ValidationException {
        if (friendStatus == null)
            throw new IllegalArgumentException();
        if (findOne(friendStatus.getID()) == null)
            return friendStatus;
        try {
            friendStatusValidator.validate(friendStatus);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.update(friendStatus);
                session.getTransaction().commit();
                return null;
            }
        } catch (Validator.ValidationException e) {
            throw new Validator.ValidationException(e.getMessage());
        }
    }

    @Override
    public FriendStatus findOne(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<FriendStatus> result = session.createQuery("select a from FriendStatus a where ID=:id")
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
    public List<FriendStatus> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<FriendStatus> result = session.createQuery("select a from FriendStatus a").list();
            session.getTransaction().commit();
            return result;
        }
    }
}
