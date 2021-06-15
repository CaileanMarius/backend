package cmir2469.backend.repository;

import cmir2469.backend.domain.Reaction;
import cmir2469.backend.domain.validators.ReactionValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ReactionRepository implements InterfaceRepository<String, Reaction> {

    SessionFactory sessionFactory;
    ReactionValidator reactionValidator;

    public ReactionRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        reactionValidator = new ReactionValidator();
    }


    @Override
    public Reaction save(Reaction reaction) throws Validator.ValidationException {
        if(reaction == null)
            throw new IllegalArgumentException();
        try{
            reactionValidator.validate(reaction);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(reaction.getID()) != null)
            return  reaction;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(reaction);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public Reaction delete(String id) {
        if (id == null)
            throw new IllegalArgumentException();
        Reaction reaction = findOne(id);
        if (reaction == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(reaction);
            session.getTransaction().commit();
            return reaction;
        }
    }

    @Override
    public Reaction update(Reaction entity) throws Validator.ValidationException {
        if (entity == null)
            throw new IllegalArgumentException();
        if (findOne(entity.getID()) == null)
            return entity;
        try {
            reactionValidator.validate(entity);
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
    public Reaction findOne(String id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Reaction> result = session.createQuery("select a from Reaction a where ID=:id")
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
    public List<Reaction> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Reaction> result = session.createQuery("select a from Reaction a").list();
            session.getTransaction().commit();
            return result;
        }
    }

    public Integer findAllLikesToOnePost(Integer idPost) {
        Integer like=1;
        if (idPost == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Reaction> result = session.createQuery("select a from Reaction a where PostID=:idPost and Status=:like ")
                    .setParameter("idPost", idPost)
                    .setParameter("like", like)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result.size();
            else
                return null;
        }
    }

    public Integer findAllDislikesToOnePost(Integer idPost) {
        Integer dislike = -1;
        if (idPost == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Reaction> result = session.createQuery("select a from Reaction a where PostID=:idPost and Status=:dislike ")
                    .setParameter("idPost", idPost)
                    .setParameter("dislike", dislike)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result.size();
            else
                return null;
        }
    }
}