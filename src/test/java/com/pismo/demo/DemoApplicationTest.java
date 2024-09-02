package com.pismo.demo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.test.context.ActiveProfiles;

import com.pismo.demo.entity.OperationType;
import com.pismo.demo.repository.OperationTypeRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DemoApplicationTest {

    @Mock
    private OperationTypeRepository operationTypeRepository;

    @InjectMocks
    private DemoApplication demoApplication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSeedOperationType() throws Exception {
        InitializingBean initializingBean = demoApplication.seedOperationType();
        initializingBean.afterPropertiesSet();

        OperationType purchase = new OperationType();
			purchase.setOperationTypeId((short) 1);
			purchase.setDescription("Normal Purchase");
			purchase.setNegative(true);

			OperationType installments = new OperationType();
			installments.setOperationTypeId((short) 2);
			installments.setDescription("Purchase with installments");
			purchase.setNegative(true);

			OperationType withdrawal = new OperationType();
			withdrawal.setOperationTypeId((short) 3);
			withdrawal.setDescription("Withdrawal");
			purchase.setNegative(true);

			OperationType voucher = new OperationType();
			voucher.setOperationTypeId((short) 4);
			purchase.setNegative(false);
			voucher.setDescription("Credit Voucher");

        verify(operationTypeRepository, times(1)).save(purchase);
        verify(operationTypeRepository, times(1)).save(installments);
        verify(operationTypeRepository, times(1)).save(withdrawal);
        verify(operationTypeRepository, times(1)).save(voucher);
    }
}
