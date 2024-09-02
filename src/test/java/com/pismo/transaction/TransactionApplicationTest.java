package com.pismo.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.test.context.ActiveProfiles;

import com.pismo.transaction.repository.OperationTypeRepository;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TransactionApplicationTest {

    @Mock
    private OperationTypeRepository operationTypeRepository;

    @InjectMocks
    private TransactionApplication demoApplication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSeedOperationType() throws Exception {
        InitializingBean initializingBean = demoApplication.seedOperationType();
        initializingBean.afterPropertiesSet();
    }
}
