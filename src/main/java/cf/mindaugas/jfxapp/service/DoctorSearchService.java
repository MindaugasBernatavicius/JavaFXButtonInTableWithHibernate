package cf.mindaugas.jfxapp.service;

import cf.mindaugas.jfxapp.model.Doctor;
import cf.mindaugas.jfxapp.repository.DoctorRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DoctorSearchService {

    private DoctorRepository doctorRepository;

    // dependency injection via constructor
    public DoctorSearchService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> findByName(String name){
        return doctorRepository.getAll().stream()
                .filter(doctor -> doctor.getName().contains(name))
                .collect(Collectors.toList());
    }
}
