package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServicesImplTest {

    @InjectMocks
    private CourseServicesImpl courseServices;

    @Mock
    private ICourseRepository courseRepository;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creating Course instance using default constructor and setters
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);
    }

    @Test
    void addCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseServices.addCourse(course);

        assertEquals(course, result);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void retrieveAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));

        List<Course> result = courseServices.retrieveAllCourses();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void updateCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseServices.updateCourse(course);

        assertEquals(course, result);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void retrieveCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseServices.retrieveCourse(1L);

        assertNotNull(result);
        assertEquals(course, result);
        verify(courseRepository, times(1)).findById(1L);
    }
}
