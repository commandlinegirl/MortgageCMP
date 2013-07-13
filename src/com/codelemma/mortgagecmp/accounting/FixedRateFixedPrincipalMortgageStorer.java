package com.codelemma.mortgagecmp.accounting;

import java.util.HashMap;

import com.codelemma.mortgagecmp.accounting.Storage.StorageException;

public class FixedRateFixedPrincipalMortgageStorer implements MortgageStorer<FixedRateFixedPrincipalMortgage> {
	// Class tag
	private static final String FIXED_RATE_FIXED_PRINCIPAL = "frfp";
	private static final String MORTGAGE_ID = "mid";	
	private static final String NAME = "n";
	private static final String PURCHASE_PRICE = "pp";
	private static final String DOWNPAYMENT = "dp";
	private static final String INTEREST_RATE = "ir";
	private static final String TERM_YEARS = "ty";
	private static final String TERM_MONTHS = "tm";
	private static final String PROPERTY_INSURANCE = "pi";
	private static final String PROPERTY_TAX = "pt";
	private static final String PMI_RATE = "pr";
	private static final String CLOSING_FEES = "cf";
	private static final String EXTRA_PAYMENT = "ep";
	private static final String EXTRA_PAYMENT_FREQUENCY = "epf";

	private Storage storage;
	
	public FixedRateFixedPrincipalMortgageStorer(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String getTag() {
		return FIXED_RATE_FIXED_PRINCIPAL;
	}

	/**
	 * Instantiates a mortgage using data read from storage
	 * keyed with a given prefix. Assumes storage has been opened for reading.
	 */
	@Override
	public Mortgage load(int id) throws StorageException {
		String prefix = Integer.toString(id);
		HashMap<String, String> mortgage_data = new HashMap<String, String>();
		FixedRateFixedPrincipalMortgageFactory mfactory = new FixedRateFixedPrincipalMortgageFactory();
		mortgage_data.put("name", storage.getString(prefix, NAME));
		mortgage_data.put("purchase_price", storage.getBigDecimal(prefix, PURCHASE_PRICE).toString());
		mortgage_data.put("downpayment", storage.getBigDecimal(prefix, DOWNPAYMENT).toString());
		mortgage_data.put("interest_rate", storage.getBigDecimal(prefix, INTEREST_RATE).toString());
		mortgage_data.put("term_years", String.valueOf(storage.getInt(prefix, TERM_YEARS)));
		mortgage_data.put("term_months", String.valueOf(storage.getInt(prefix, TERM_MONTHS)));
		mortgage_data.put("property_insurance", storage.getBigDecimal(prefix, PROPERTY_INSURANCE).toString());
		mortgage_data.put("property_tax", storage.getBigDecimal(prefix, PROPERTY_TAX).toString());
		mortgage_data.put("pmi_rate", storage.getBigDecimal(prefix, PMI_RATE).toString());
		mortgage_data.put("closing_fees", storage.getBigDecimal(prefix, CLOSING_FEES).toString());
		mortgage_data.put("extra_payment", storage.getBigDecimal(prefix, EXTRA_PAYMENT).toString());
		mortgage_data.put("extra_payment_frequency", String.valueOf(storage.getInt(prefix, EXTRA_PAYMENT_FREQUENCY)));
		Mortgage mortgage =  mfactory.createMortgage(mortgage_data);
		mortgage.setPreviousId(storage.getInt(prefix, MORTGAGE_ID));
		return mortgage;
	}

	/**
	 * Saves a mortgage by writing its data to storage under
	 * keys with a given prefix. Assumes storage has been opened for writing.
	 */
	@Override
	public void save(FixedRateFixedPrincipalMortgage mortgage) throws StorageException {
		String prefix = Integer.toString(mortgage.getId());
		storage.putInt(prefix, MORTGAGE_ID, mortgage.getId());		
		storage.putString(prefix, NAME, mortgage.getName());
		storage.putBigDecimal(prefix, PURCHASE_PRICE, mortgage.getPurchasePrice());
		storage.putBigDecimal(prefix, DOWNPAYMENT, mortgage.getDownpayment());
		storage.putBigDecimal(prefix, INTEREST_RATE, mortgage.getInterestRate());
		storage.putInt(prefix, TERM_YEARS, mortgage.getTermYears());
		storage.putInt(prefix, TERM_MONTHS, mortgage.getTermMonths());
		storage.putBigDecimal(prefix, PROPERTY_INSURANCE, mortgage.getYearlyPropertyInsurance());
		storage.putBigDecimal(prefix, PROPERTY_TAX, mortgage.getYearlyPropertyTax());
		storage.putBigDecimal(prefix, PMI_RATE, mortgage.getPMI());
		storage.putBigDecimal(prefix, CLOSING_FEES, mortgage.getClosingFees());
		storage.putBigDecimal(prefix, EXTRA_PAYMENT, mortgage.getExtraPayment());
		storage.putInt(prefix, EXTRA_PAYMENT_FREQUENCY, mortgage.getExtraPaymentFrequency());	
	}

	/**
	 * Removes a debt representing a mortgage and keyed with a given prefix from
	 * storage. Assumes storage has been opened for writing.
	 */
	@Override
	public void remove(FixedRateFixedPrincipalMortgage mortgage) throws StorageException {
		String prefix = Integer.toString(mortgage.getId());
		storage.remove(prefix, MORTGAGE_ID);		
		storage.remove(prefix, NAME);
		storage.remove(prefix, PURCHASE_PRICE);
		storage.remove(prefix, DOWNPAYMENT);
		storage.remove(prefix, INTEREST_RATE);
		storage.remove(prefix, TERM_YEARS);
		storage.remove(prefix, TERM_MONTHS);
		storage.remove(prefix, PROPERTY_INSURANCE);
		storage.remove(prefix, PROPERTY_TAX);
		storage.remove(prefix, PMI_RATE);
		storage.remove(prefix, CLOSING_FEES);
		storage.remove(prefix, EXTRA_PAYMENT);
		storage.remove(prefix, EXTRA_PAYMENT_FREQUENCY);			
	}
}