package com.ps.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import jdk.jfr.Description;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@Description("Tests for TransactionApplication")
public class BookingApplicationTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApplication() throws Exception {
    }
}
