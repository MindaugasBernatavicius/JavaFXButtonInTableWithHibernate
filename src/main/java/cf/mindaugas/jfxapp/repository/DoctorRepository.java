package cf.mindaugas.jfxapp.repository;

import cf.mindaugas.jfxapp.model.Doctor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.stream.Collectors;

public class DoctorRepository {
    private SessionFactory sessionFactory;

    public DoctorRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Doctor> getAll(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Doctor";
        Query query = session.createQuery(hql);
        List results = query.list();
        transaction.commit();
        session.close();
        return results;
    }

    public void create(Doctor doctor) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(doctor);
        transaction.commit();
        session.close();
    }
}
