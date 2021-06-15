package cmir2469.backend.repository;

import cmir2469.backend.domain.Tag;
import cmir2469.backend.domain.validators.TagValidator;
import cmir2469.backend.domain.validators.Validator;
import cmir2469.backend.utils.HibernateSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class TagRepository implements InterfaceRepository<Integer, Tag> {

    SessionFactory sessionFactory;
    TagValidator tagValidator;

    public TagRepository(){
        sessionFactory = HibernateSession.getSessionFactory();
        tagValidator = new TagValidator();
    }


    @Override
    public Tag save(Tag tag) throws Validator.ValidationException {
        if(tag == null)
            throw new IllegalArgumentException();
        try{
            tagValidator.validate(tag);

        } catch (Validator.ValidationException e){
            throw new Validator.ValidationException(e.getMessage());
        }
        if(findOne(tag.getID()) != null)
            return  tag;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(tag);
            session.getTransaction().commit();
            return null;
        }
    }

    @Override
    public Tag delete(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        Tag tag = findOne(id);
        if (tag == null)
            return null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(tag);
            session.getTransaction().commit();
            return tag;
        }
    }

    @Override
    public Tag update(Tag tag) throws Validator.ValidationException {
        if (tag == null)
            throw new IllegalArgumentException();
        if (findOne(tag.getID()) == null)
            return tag;
        try {
            tagValidator.validate(tag);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.update(tag);
                session.getTransaction().commit();
                return null;
            }
        } catch (Validator.ValidationException e) {
            throw new Validator.ValidationException(e.getMessage());
        }
    }

    @Override
    public Tag findOne(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Tag> result = session.createQuery("select a from Tag a where ID=:id")
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
    public List<Tag> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Tag> result = session.createQuery("select a from Tag a").list();
            session.getTransaction().commit();
            return result;
        }
    }

    public Tag findOneByName(String name) {
        if (name == null)
            throw new IllegalArgumentException();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Tag> result = session.createQuery("select a from Tag a where Name=:name")
                    .setParameter("name", name)
                    .list();
            session.getTransaction().commit();
            if (!result.isEmpty())
                return result.get(0);
            else
                return null;
        }
    }
}