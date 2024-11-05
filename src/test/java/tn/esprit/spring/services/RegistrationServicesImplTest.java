// src/test/java/tn/esprit/spring/services/RegistrationServicesImplTest.java

package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Registration registration;
    private Skier skier;
    private Course course;

    @BeforeEach
    public void setup() {
        registration = new Registration(1L, 2, skier, course);
        skier = new Skier();
        course = new Course();
    }

    @Test
    public void testAddRegistrationAndAssignToSkierAndCourse() {
        // Arrange
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(registration.getNumRegistration(), result.getNumRegistration());
        assertEquals(registration.getNumWeek(), result.getNumWeek());
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
    }

    @Test
    public void testAddRegistrationAndAssignToSkierAndCourse_SkierNotFound() {
        // Arrange
        when(skierRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAddRegistrationAndAssignToSkierAndCourse_CourseNotFound() {
        // Arrange
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetRegistrationById_RegistrationFound() {
        // Arrange
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        // Act
        Registration result = registrationServices.getRegistrationById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(registration.getNumRegistration(), result.getNumRegistration());
        assertEquals(registration.getNumWeek(), result.getNumWeek());
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
    }

    @Test
    public void testGetRegistrationById_RegistrationNotFound() {
        // Arrange
        when(registrationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Registration result = registrationServices.getRegistrationById(1L);

        // Assert
        assertNull(result);
    }
}