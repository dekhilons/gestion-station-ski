package tn.esprit.spring.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
///
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Skier skier;
    private Course course;
    private Registration registration;

    @BeforeEach
    void setUp() {
        skier = new Skier(1L, "John", "Doe", LocalDate.of(2010, 1, 1), "CityName", null, null, null);
        course = new Course(1L, 1, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 100.0f, 5, null); // Set max capacity for tests
        registration = new Registration(1L, 1, skier, course);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        when(skierRepository.findById(skier.getNumSkier())).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, skier.getNumSkier());

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        verify(skierRepository, times(1)).findById(skier.getNumSkier());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testAssignRegistrationToCourse() {
        when(registrationRepository.findById(registration.getNumRegistration())).thenReturn(Optional.of(registration));
        when(courseRepository.findById(course.getNumCourse())).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.assignRegistrationToCourse(registration.getNumRegistration(), course.getNumCourse());

        assertNotNull(result);
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).findById(registration.getNumRegistration());
        verify(courseRepository, times(1)).findById(course.getNumCourse());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_ChildCourse() {
        skier.setDateOfBirth(LocalDate.now().minusYears(10));  // Skier is 14 years old
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);

        when(skierRepository.findById(skier.getNumSkier())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(course.getNumCourse())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, skier.getNumSkier(), course.getNumCourse())).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(course, 1)).thenReturn(3L); // Less than max capacity

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, skier.getNumSkier(), course.getNumCourse());

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_FullCourse() {
        skier.setDateOfBirth(LocalDate.now().minusYears(10));  // Skier is eligible for child course
        course.setMaxParticipants(5); // Assume max capacity is 5

        when(skierRepository.findById(skier.getNumSkier())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(course.getNumCourse())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, skier.getNumSkier(), course.getNumCourse())).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(course, 1)).thenReturn(5L); // At max capacity

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, skier.getNumSkier(), course.getNumCourse());

        assertNull(result); // Registration should not proceed if course is full
        verify(registrationRepository, never()).save(registration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_AdultInChildCourse() {
        skier.setDateOfBirth(LocalDate.now().minusYears(20));  // Skier is 20 years old (adult)
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN); // Child course

        when(skierRepository.findById(skier.getNumSkier())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(course.getNumCourse())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, skier.getNumSkier(), course.getNumCourse())).thenReturn(0L);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, skier.getNumSkier(), course.getNumCourse());

        assertNull(result); // Registration should not proceed if adult tries to join a child course
        verify(registrationRepository, never()).save(registration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_IndividualCourse() {
        course.setTypeCourse(TypeCourse.INDIVIDUAL); // Set as individual course

        when(skierRepository.findById(skier.getNumSkier())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(course.getNumCourse())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, skier.getNumSkier(), course.getNumCourse())).thenReturn(0L);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, skier.getNumSkier(), course.getNumCourse());

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).save(registration);
    }
}
