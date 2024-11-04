package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @InjectMocks
    private PisteServicesImpl pisteServices;

    @Mock
    private IPisteRepository pisteRepository;

    private Piste piste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Blue Slope");
        piste.setColor(Color.BLUE);
        piste.setLength(1500);
        piste.setSlope(30);
    }

    @Test
    void addPiste() {
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        Piste result = pisteServices.addPiste(piste);

        assertEquals(piste, result);
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void retrieveAllPistes() {
        when(pisteRepository.findAll()).thenReturn(Arrays.asList(piste));

        List<Piste> result = pisteServices.retrieveAllPistes();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(piste, result.get(0));
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void retrievePiste() {
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));

        Piste result = pisteServices.retrievePiste(1L);

        assertNotNull(result);
        assertEquals(piste, result);
        verify(pisteRepository, times(1)).findById(1L);
    }

    @Test
    void removePiste() {
        doNothing().when(pisteRepository).deleteById(1L);

        pisteServices.removePiste(1L);

        verify(pisteRepository, times(1)).deleteById(1L);
    }
}
