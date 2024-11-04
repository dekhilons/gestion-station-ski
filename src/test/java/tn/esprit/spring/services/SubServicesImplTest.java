package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubscriptionServicesImplTest {

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    private Subscription subscription;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setStartDate(LocalDate.now());
        subscription.setPrice(100.0f);
        subscription.setTypeSub(TypeSubscription.ANNUAL);
    }

    @Test
    void addSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionServices.addSubscription(subscription);

        assertEquals(subscription, result);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void updateSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionServices.updateSubscription(subscription);

        assertEquals(subscription, result);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void retrieveSubscriptionById() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);

        assertNotNull(result);
        assertEquals(subscription, result);
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void getSubscriptionByType() {
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL)).thenReturn(new HashSet<>(Arrays.asList(subscription)));

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.ANNUAL);

        assertFalse(result.isEmpty());
        assertTrue(result.contains(subscription));
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL);
    }

    @Test
    void retrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(Arrays.asList(subscription));

        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(subscription, result.get(0));
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }
}
