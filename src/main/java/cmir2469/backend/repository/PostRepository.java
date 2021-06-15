package cmir2469.backend.repository;

import cmir2469.backend.domain.Post;
import cmir2469.backend.domain.validators.PostValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class PostRepository  implements InterfaceRepository<Integer, Post> {

    SessionFactory sessionFactory;
    PostValidator postValidator;

    public PostRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        postValidator = new PostValidator();
    }


    @Override
    public Post save(Post post) throws Validator.ValidationException {
        if(post == null)
            throw new IllegalArgumentException();
        try{
            postValidator.validate(post);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(post.getID()) != null)
            return  post;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(post);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public Post delete(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        Post post = findOne(id);
        if (post == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(post);
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public Post update(Post entity) throws Validator.ValidationException {
        if (entity == null)
            throw new IllegalArgumentException();
        if (findOne(entity.getID()) == null)
            return entity;
        try {
            postValidator.validate(entity);
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
    public Post findOne(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Post> result = session.createQuery("select a from Post a where ID=:id")
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
    public List<Post> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Post> result = session.createQuery("select a from Post a").list();
            session.getTransaction().commit();
            return result;
        }
    }

    public List<Post> findAllPostByOneUser(Integer idUser) {

        if (idUser == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Post> result = session.createQuery("select a from Post a where UserID=:idUser")
                    .setParameter("idUser", idUser)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result;
            else
                return null;
        }


    }
}
