package cmir2469.backend.repository;

import cmir2469.backend.domain.PostTag;
import cmir2469.backend.domain.validators.PostTagValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class PostTagRepository implements InterfaceRepository<Integer, PostTag> {

    SessionFactory sessionFactory;
    PostTagValidator postTagValidator;

    public PostTagRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        postTagValidator = new PostTagValidator();
    }


    @Override
    public PostTag save(PostTag postTag) throws Validator.ValidationException {
        if(postTag == null)
            throw new IllegalArgumentException();
        try{
            postTagValidator.validate(postTag);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(postTag.getPostID()) != null)
            return  postTag;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(postTag);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public PostTag delete(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        PostTag postTag = findOne(id);
        if (postTag == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(postTag);
            session.getTransaction().commit();
            return postTag;
        }
    }

    @Override
    public PostTag update(PostTag postTag) throws Validator.ValidationException {
        if (postTag == null)
            throw new IllegalArgumentException();
        if (findOne(postTag.getPostID()) == null)
            return postTag;
        try {
            postTagValidator.validate(postTag);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.update(postTag);
                session.getTransaction().commit();
                return null;
            }
        } catch (Validator.ValidationException e) {
            throw new Validator.ValidationException(e.getMessage());
        }
    }

    @Override
    public PostTag findOne(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<PostTag> result = session.createQuery("select a from PostTag a where PostID=:id")
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
    public List<PostTag> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<PostTag> result = session.createQuery("select a from PostTag a").list();
            session.getTransaction().commit();
            return result;
        }
    }
}