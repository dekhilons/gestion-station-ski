package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistrationServicesImplTest {

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    private Registration registration;
    private Skier skier;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Skier
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("Jane");
        skier.setLastName("Doe");
        skier.setDateOfBirth(LocalDate.of(2005, 1, 1));
        skier.setCity("Paris");
        skier.setRegistrations(new HashSet<>());

        // Initialize Course
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);
        course.setRegistrations(new HashSet<>());

        // Initialize Registration
        registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setNumWeek(1);
        registration.setSkier(skier);
        registration.setCourse(course);
    }

    @Test
    void addRegistrationAndAssignToSkier() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, 1L);

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(registration.getNumRegistration(), result.getNumRegistration());
        verify(skierRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void assignRegistrationToCourse() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        assertNotNull(result);
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void addRegistrationAndAssignToSkierAndCourse() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(0L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
        verify(skierRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void numWeeksCourseOfInstructorBySupport() {
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class))).thenReturn(Arrays.asList(1, 2, 3));

        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(1L, Support.SKI);
    }
}
