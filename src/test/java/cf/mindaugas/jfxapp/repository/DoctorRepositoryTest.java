package cf.mindaugas.jfxapp.repository;

import cf.mindaugas.jfxapp.repository.DoctorRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;


// TODO :: we can mock hibernate classes
// TODO :: we can mock / spy same object to control methods in the same classes as the method under test
//  ... https://stackoverflow.com/questions/41583346/mocking-hibernate-session

public class DoctorRepositoryTest {

    @Mock
    private SessionFactory sessionFactory;
    DoctorRepository dr;

    @BeforeEach
    public void reset(){
        dr = new DoctorRepository(sessionFactory);
    }
}
