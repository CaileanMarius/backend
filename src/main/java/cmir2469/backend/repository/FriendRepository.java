package cmir2469.backend.repository;

import cmir2469.backend.domain.Friend;
import cmir2469.backend.domain.Friend;
import cmir2469.backend.domain.validators.FriendValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class FriendRepository implements InterfaceRepository<String, Friend> {

    SessionFactory sessionFactory;
    FriendValidator friendValidator;

    public FriendRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        friendValidator = new FriendValidator();
    }


    @Override
    public Friend save(Friend friend) throws Validator.ValidationException {
        if(friend == null)
            throw new IllegalArgumentException();
        try{
            friendValidator.validate(friend);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(friend.getID()) != null)
            return  friend;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(friend);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public Friend delete(String id) {
        if (id == null)
            throw new IllegalArgumentException();
        Friend friend = findOne(id);
        if (friend == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(friend);
            session.getTransaction().commit();
            return friend;
        }
    }

    @Override
    public Friend update(Friend entity) throws Validator.ValidationException {
        if (entity == null)
            throw new IllegalArgumentException();
        if (findOne(entity.getID()) == null)
            return entity;
        try {
            friendValidator.validate(entity);
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
    public Friend findOne(String id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Friend> result = session.createQuery("select a from Friend a where ID=:id")
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
    public List<Friend> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Friend> result = session.createQuery("select a from Friend a").list();
            session.getTransaction().commit();
            return result;
        }
    }

    public List<Friend> findAllFriendsForOneUser(Integer idUser) {
        if (idUser == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Friend> result = session.createQuery("select a from Friend a where UserID=:idUser")
                    .setParameter("idUser", idUser)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result;
            else
                return null;
        }
    }


    public List<Friend> findAllFriendRequests(Integer idUser) {
        Integer request = 0;
        if (idUser == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Friend> result = session.createQuery("select a from Friend a where UserID=:idUser and FriendStatusId:=request")
                    .setParameter("idUser", idUser)
                    .setParameter("request", request)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result;
            else
                return null;
        }
    }

}
