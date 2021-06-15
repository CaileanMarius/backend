package cmir2469.backend.repository;

import cmir2469.backend.domain.Comment;
import cmir2469.backend.domain.validators.CommentValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.util.List;

public class CommentRepository implements InterfaceRepository<Integer, Comment> {

    SessionFactory sessionFactory;
    CommentValidator commentValidator;

    public CommentRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        commentValidator = new CommentValidator();
    }


    @Override
    public Comment save(Comment comment) throws Validator.ValidationException {
        if(comment == null)
            throw new IllegalArgumentException();
        try{
            commentValidator.validate(comment);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(comment.getID()) != null)
            return  comment;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(comment);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public Comment delete(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        Comment comment = findOne(id);
        if (comment == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(comment);
            session.getTransaction().commit();
            return comment;
        }
    }

    @Override
    public Comment update(Comment comment) throws Validator.ValidationException {
        if (comment == null)
            throw new IllegalArgumentException();
        if (findOne(comment.getID()) == null)
            return comment;
        try {
            commentValidator.validate(comment);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.update(comment);
                session.getTransaction().commit();
                return null;
            }
        } catch (Validator.ValidationException e) {
            throw new Validator.ValidationException(e.getMessage());
        }
    }

    @Override
    public Comment findOne(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Comment> result = session.createQuery("select a from Comment a where ID=:id")
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
    public List<Comment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Comment> result = session.createQuery("select a from Comment a").list();
            session.getTransaction().commit();
            return result;
        }
    }


    public List<Comment> findAllCommentOfOnePost(Integer idPost) {
        if (idPost == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Comment> result = session.createQuery("select a from Comment a where PostID=:idPost")
                    .setParameter("idPost", idPost)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result;
            else
                return null;
        }
    }


    public Comment findOneByAllFields(Integer userId, Integer postId, String description, LocalDateTime createdDate) {
        if (userId == null || postId == null || description == null || createdDate == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Comment> result = session.createQuery("select a from Comment a where UserID=:userId and PostID=:postId and Description=:description and CreatedDate=:createdDate ")
                    .setParameter("userId", userId)
                    .setParameter("postId", postId)
                    .setParameter("description", description)
                    .setParameter("createdDate", createdDate)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result.get(0);
            else
                return null;
        }
    }
}