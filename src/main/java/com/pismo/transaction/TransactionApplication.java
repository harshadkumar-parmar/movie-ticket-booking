package com.pismo.transaction;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.pismo.transaction.entity.OperationType;
import com.pismo.transaction.repository.OperationTypeRepository;

import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class TransactionApplication {

	private final OperationTypeRepository operationTypeRepository;

	public static void main(String[] args) {
		SpringApplication.run(TransactionApplication.class, args);
	}

	/**
	 * Initializes the database with the possible operation types.
	 * 
	 * @return An InitializingBean that will be called after the application context
	 *         has been loaded.
	 */
	@Bean
	InitializingBean seedOperationType() {
		return () -> {
			OperationType purchase = new OperationType();
			purchase.setOperationTypeId((short) 1);
			purchase.setDescription("Normal Purchase");
			purchase.setNegative(true);
			operationTypeRepository.save(purchase);

			OperationType installments = new OperationType();
			installments.setOperationTypeId((short) 2);
			installments.setDescription("Purchase with installments");
			purchase.setNegative(true);
			operationTypeRepository.save(installments);

			OperationType withdrawal = new OperationType();
			withdrawal.setOperationTypeId((short) 3);
			withdrawal.setDescription("Withdrawal");
			purchase.setNegative(true);
			operationTypeRepository.save(withdrawal);

			OperationType voucher = new OperationType();
			voucher.setOperationTypeId((short) 4);
			purchase.setNegative(false);
			voucher.setDescription("Credit Voucher");
			operationTypeRepository.save(voucher);
		};
	}
}
