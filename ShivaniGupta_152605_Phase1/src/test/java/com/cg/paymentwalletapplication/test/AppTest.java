package com.cg.paymentwalletapplication.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cg.paymentwalletapplication.dto.CustomerBean;
import com.cg.paymentwalletapplication.dto.Wallet;
import com.cg.paymentwalletapplication.exception.PaymentWalletException;
import com.cg.paymentwalletapplication.service.IWalletService;
import com.cg.paymentwalletapplication.service.WalletServiceImpl;

public class AppTest {

	private static IWalletService service = null;
	private static HashMap<String, CustomerBean> custInfo = null;
	private static CustomerBean customer1, customer2, customer3;

	@BeforeClass
	public static void initialData() {
		custInfo = new HashMap<String, CustomerBean>();
		service = new WalletServiceImpl();
		customer1 = new CustomerBean("Amit", 30, "amit@gmail.com", "amit45", "9900112212", "male", new Wallet(3000));
		customer2 = new CustomerBean("Ajay", 45, "ajay23@gmail.com", "ajay12", "9963242422", "male",
				new Wallet(6000.90));
		customer3 = new CustomerBean("Yogini", 23, "yogini@gmail.com", "yogi123", "9922950519", "female",
				new Wallet(7000.50));
		custInfo.put("9900112212", customer1);
		custInfo.put("9963242422", customer2);
		custInfo.put("9922950519", customer3);
	}

	@Test
	public void createCustomerTestPositive() throws PaymentWalletException {
		CustomerBean customer = new CustomerBean("Shivani", 30, "shivi@gmail.com", "shivi123", "9900114698", "female",
				new Wallet(3000));
		String phone = service.createAccount(customer);
		assertEquals("9900114698", phone);
	}

	@Test(expected = PaymentWalletException.class)
	public void enteredInvalidCustomerNameTest() throws PaymentWalletException {
		CustomerBean customer = new CustomerBean("abc", 30, "abc@gmail.com", "abcdef", "9900112212", "male",
				new Wallet(3000));
		boolean result = service.validateDetails(customer);
		assertFalse(result);
	}

	@Test(expected = PaymentWalletException.class)
	public void enteredInvalidNameTest() throws PaymentWalletException {
		CustomerBean customer = new CustomerBean("1suhani", 30, "suh@gmail.com", "suh", "9900177802", "female",
				new Wallet(3600));
		boolean result = service.validateDetails(customer);
		assertFalse(result);
	}

	@Test(expected = PaymentWalletException.class)
	public void enteredInvalidAgeTest() throws PaymentWalletException {
		CustomerBean customer = new CustomerBean("suhani", 130, "suh@gmail.com", "suh", "9900177802", "female",
				new Wallet(3600));
		boolean result = service.validateDetails(customer);
		assertFalse(result);
	}

	@Test(expected = PaymentWalletException.class)
	public void enteredInvalidEmailTest() throws PaymentWalletException {
		CustomerBean customer = new CustomerBean("suhani", 30, "suh*suh@gmail.com", "suh", "9900177802", "female",
				new Wallet(3600));
		boolean result = service.validateDetails(customer);
		assertFalse(result);
	}

	@Test(expected = PaymentWalletException.class)
	public void enteredInvalidGenderTest() throws PaymentWalletException {
		CustomerBean customer = new CustomerBean("suhani", 30, "suh@gmail.com", "suh", "9900177802", "f",
				new Wallet(3600));
		boolean result = service.validateDetails(customer);
		assertFalse(result);
	}

	@Test(expected = PaymentWalletException.class)
	public void enteredInvalidInitAmountTest() throws PaymentWalletException {
		CustomerBean customer = new CustomerBean("suhani", 30, "suh@gmail.com", "suh", "9900177802", "female",
				new Wallet(-390));
		boolean result = service.validateDetails(customer);
		assertFalse(result);
	}

	@Test
	public void validateTestPositive() throws PaymentWalletException {
		CustomerBean customer = new CustomerBean("Shivani", 30, "shivi@gmail.com", "shivi123", "9900114698", "female",
				new Wallet(3000));
		boolean result = service.validateDetails(customer);
		assertTrue(result);
	}

	@Test
	public void checkBalancePresentTest() throws PaymentWalletException {
		CustomerBean customer = custInfo.get(customer1.getMobileNo());
		assertEquals(customer1, customer);
	}

	@Test
	public void checkBalanceIsNull() throws PaymentWalletException {
		CustomerBean customer = custInfo.get("");
		assertNotSame(customer1, customer);
	}

	@Test
	public void checkWithdrawAmountTest() {
		boolean result = service.withdrawAmount(8000, customer2.getMobileNo());
		assertFalse(result);
	}

	@Test
	public void checkMobileNumberForWithdrawalTest() {
		boolean result = service.withdrawAmount(5000, "9000143242");
		assertFalse(result);
	}

	@Test(expected=AssertionError.class)
	public void correctAmountAndMobileTest() {
		customer2.setBalance(10000.00);
		boolean result = service.withdrawAmount(3000.00, custInfo.get(customer2.getMobileNo()).getMobileNo());
		assertTrue(result);
	}

	@Test
	public void checkMobileNumberForDepositTest() {
		boolean result = service.depositAmount(4600, "9000143242");
		assertFalse(result);
	}

	@Test(expected = AssertionError.class)
	public void correctMobileNumberForDepositTest() {
		boolean result = service.depositAmount(9000, "9922950519");
		assertTrue(result);
	}

	@Test(expected=PaymentWalletException.class)
	public void checkSenderMobileNumTest() throws PaymentWalletException {
		boolean result = service.fundTransfer("9023628623", "9963242422", 345.90);
		assertFalse(result);
	}

	@Test(expected=PaymentWalletException.class)
	public void checkReceiverMobileNumTest() throws PaymentWalletException {
		boolean result = service.fundTransfer("9900112212", "9000454333", 890.50);
		assertFalse(result);
	}

	@Test(expected = PaymentWalletException.class)
	public void checkBalanceForSenderMobileNumTest() throws PaymentWalletException {
		boolean result = service.fundTransfer(customer1.getMobileNo(), customer2.getMobileNo(), 4000);
		assertFalse(result);
	}

	@Test(expected=PaymentWalletException.class)
	public void correctDetailsOfSenderReceiverTest() throws PaymentWalletException {
		customer2.setBalance(12000.00);
		boolean result = service.fundTransfer(customer2.getMobileNo(), customer3.getMobileNo(), 2000);
		assertTrue(result);
	}

	@Test
	public void checkCorrectMobileNumberToPrintTest() {
		String mobNum = custInfo.get(customer3.getMobileNo()).getMobileNo();
		assertEquals("9922950519", mobNum);
	}

}
