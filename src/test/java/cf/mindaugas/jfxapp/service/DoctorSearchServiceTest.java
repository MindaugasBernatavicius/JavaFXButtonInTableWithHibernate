package cf.mindaugas.jfxapp.service;

import cf.mindaugas.jfxapp.model.Doctor;
import cf.mindaugas.jfxapp.repository.DoctorRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DoctorSearchServiceTest {

    DoctorSearchService dss;

    @Test
    public void findByName__givenNameWhenOneExists__returnsOneDoctor() {
        // given
        DoctorRepository dr = mock(DoctorRepository.class);
        when(dr.getAll()).thenReturn(new ArrayList<Doctor>(){{
            add(new Doctor("Jonas", "Jonaitis"));
            add(new Doctor("Mindaugas", "Mindaugaitis"));
        }});

        // ... injecting a mocked object
        dss = new DoctorSearchService(dr);

        // when
        List<Doctor> res = dss.findByName("Mindaugas");

        // then
        assertEquals(1, res.size(), "Only returns one result");
        assertEquals(new Doctor("Mindaugas", "Mindaugaitis"), res.get(0), "The result is Doctor Mindaugas");
    }
}
