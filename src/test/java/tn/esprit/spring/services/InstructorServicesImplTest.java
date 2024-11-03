package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InstructorServicesImplTest {

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor(1L, "John", "Doe", LocalDate.now(), new HashSet<>());

        // Creating Course using default constructor and setters
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);
    }

    @Test
    void addInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor result = instructorServices.addInstructor(instructor);

        assertEquals(instructor, result);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void retrieveAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(instructor));

        List<Instructor> result = instructorServices.retrieveAllInstructors();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(instructor, result.get(0));
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void updateInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor result = instructorServices.updateInstructor(instructor);

        assertEquals(instructor, result);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void retrieveInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor result = instructorServices.retrieveInstructor(1L);

        assertNotNull(result);
        assertEquals(instructor, result);
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void addInstructorAndAssignToCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        assertNotNull(result);
        assertEquals(1, result.getCourses().size());
        assertTrue(result.getCourses().contains(course));
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }
}
