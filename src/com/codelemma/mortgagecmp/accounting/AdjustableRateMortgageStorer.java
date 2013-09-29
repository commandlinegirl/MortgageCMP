package com.codelemma.mortgagecmp.accounting;

import java.util.HashMap;

import com.codelemma.mortgagecmp.accounting.Storage.StorageException;

public class AdjustableRateMortgageStorer implements MortgageStorer<AdjustableRateMortgage> {

	// Class tag
	private static final String ADJUSTABLE_RATE_MORTGAGE = "arm";
	private static final String MORTGAGE_ID = "mid";	
	private static final String NAME = "n";
	private static final String PURCHASE_PRICE = "pp";
	private static final String DOWNPAYMENT = "dp";
	private static final String INTEREST_RATE = "ir";
	private static final String ADJUSTMENT_PERIOD = "ap";
	private static final String MONTHS_BETWEEN_ADJUSTMENTS = "mba";
	private static final String MAX_SINGLE_RATE_ADJUSTMENT = "msra";
	private static final String INTEREST_RATE_CAP = "irc";
	private static final String TERM_YEARS = "ty";
	private static final String TERM_MONTHS = "tm";
	private static final String PROPERTY_INSURANCE = "pi";
	private static final String PROPERTY_TAX = "pt";
	private static final String PMI_RATE = "pr";
	private static final String CLOSING_FEES = "cf";
	private static final String EXTRA_PAYMENT = "ep";
	private static final String EXTRA_PAYMENT_FREQUENCY = "epf";
	private static final String ARM_TYPE = "at";

	private Storage storage;
	
	public AdjustableRateMortgageStorer(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String getTag() {
		return ADJUSTABLE_RATE_MORTGAGE;
	}

	/**
	 * Instantiates a mortgage using data read from storage
	 * keyed with a given prefix. Assumes storage has been opened for reading.
	 */
	@Override
	public Mortgage load(int id) throws StorageException {
		String prefix = Integer.toString(id);
		HashMap<String, String> mortgage_data = new HashMap<String, String>();
		AdjustableRateMortgageFactory mfactory = new AdjustableRateMortgageFactory();
		mortgage_data.put("name", storage.getString(prefix, NAME));
		mortgage_data.put("purchase_price", storage.getBigDecimal(prefix, PURCHASE_PRICE).toString());
		mortgage_data.put("downpayment", storage.getBigDecimal(prefix, DOWNPAYMENT).toString());
		mortgage_data.put("interest_rate", storage.getBigDecimal(prefix, INTEREST_RATE).toString());
		mortgage_data.put("adjustment_period", String.valueOf(storage.getInt(prefix, ADJUSTMENT_PERIOD)));
		mortgage_data.put("months_between_adjustments", String.valueOf(storage.getInt(prefix, MONTHS_BETWEEN_ADJUSTMENTS)));
		mortgage_data.put("max_single_rate_adjustment", storage.getBigDecimal(prefix, MAX_SINGLE_RATE_ADJUSTMENT).toString());
		mortgage_data.put("interest_rate_cap", storage.getBigDecimal(prefix, INTEREST_RATE_CAP).toString());		
		mortgage_data.put("term_years", String.valueOf(storage.getInt(prefix, TERM_YEARS)));
		mortgage_data.put("term_months", String.valueOf(storage.getInt(prefix, TERM_MONTHS)));
		mortgage_data.put("property_insurance", storage.getBigDecimal(prefix, PROPERTY_INSURANCE).toString());
		mortgage_data.put("property_tax", storage.getBigDecimal(prefix, PROPERTY_TAX).toString());
		mortgage_data.put("pmi_rate", storage.getBigDecimal(prefix, PMI_RATE).toString());
		mortgage_data.put("closing_fees", storage.getBigDecimal(prefix, CLOSING_FEES).toString());
		mortgage_data.put("extra_payment", storage.getBigDecimal(prefix, EXTRA_PAYMENT).toString());
		mortgage_data.put("extra_payment_frequency", String.valueOf(storage.getInt(prefix, EXTRA_PAYMENT_FREQUENCY)));
		mortgage_data.put("arm_type", String.valueOf(storage.getInt(prefix, ARM_TYPE)));		
		Mortgage mortgage =  mfactory.createMortgage(mortgage_data);
		mortgage.setPreviousId(storage.getInt(prefix, MORTGAGE_ID));
		return mortgage;
	}

	/**
	 * Saves a mortgage by writing its data to storage under
	 * keys with a given prefix. Assumes storage has been opened for writing.
	 */
	@Override
	public void save(AdjustableRateMortgage mortgage) throws StorageException {
		String prefix = Integer.toString(mortgage.getId());
		storage.putInt(prefix, MORTGAGE_ID, mortgage.getId());		
		storage.putString(prefix, NAME, mortgage.getName());
		storage.putBigDecimal(prefix, PURCHASE_PRICE, mortgage.getPurchasePrice());
		storage.putBigDecimal(prefix, DOWNPAYMENT, mortgage.getDownpayment());
		storage.putBigDecimal(prefix, INTEREST_RATE, mortgage.getInterestRate());
		storage.putInt(prefix, ADJUSTMENT_PERIOD, mortgage.getAdjustmentPeriod());
		storage.putInt(prefix, MONTHS_BETWEEN_ADJUSTMENTS, mortgage.getMonthsBetweenAdjustments());
		storage.putBigDecimal(prefix, MAX_SINGLE_RATE_ADJUSTMENT, mortgage.getMaxSingleRateAdjustment());
		storage.putBigDecimal(prefix, INTEREST_RATE_CAP, mortgage.getInterestRateCap());		
		storage.putInt(prefix, TERM_YEARS, mortgage.getTermYears());
		storage.putInt(prefix, TERM_MONTHS, mortgage.getTermMonths());
		storage.putBigDecimal(prefix, PROPERTY_INSURANCE, mortgage.getYearlyPropertyInsurance());
		storage.putBigDecimal(prefix, PROPERTY_TAX, mortgage.getYearlyPropertyTax());
		storage.putBigDecimal(prefix, PMI_RATE, mortgage.getPMI());
		storage.putBigDecimal(prefix, CLOSING_FEES, mortgage.getClosingFees());
		storage.putBigDecimal(prefix, EXTRA_PAYMENT, mortgage.getExtraPayment());
		storage.putInt(prefix, EXTRA_PAYMENT_FREQUENCY, mortgage.getExtraPaymentFrequency());
		storage.putInt(prefix, ARM_TYPE, mortgage.getARMType());	
	}

	/**
	 * Removes a debt representing a mortgage and keyed with a given prefix from
	 * storage. Assumes storage has been opened for writing.
	 */
	@Override
	public void remove(AdjustableRateMortgage mortgage) throws StorageException {
		String prefix = Integer.toString(mortgage.getId());
		storage.remove(prefix, MORTGAGE_ID);		
		storage.remove(prefix, NAME);
		storage.remove(prefix, PURCHASE_PRICE);
		storage.remove(prefix, DOWNPAYMENT);
		storage.remove(prefix, INTEREST_RATE);
		storage.remove(prefix, ADJUSTMENT_PERIOD);
		storage.remove(prefix, MONTHS_BETWEEN_ADJUSTMENTS);
		storage.remove(prefix, MAX_SINGLE_RATE_ADJUSTMENT);
		storage.remove(prefix, INTEREST_RATE_CAP);
		storage.remove(prefix, TERM_YEARS);
		storage.remove(prefix, TERM_MONTHS);
		storage.remove(prefix, PROPERTY_INSURANCE);
		storage.remove(prefix, PROPERTY_TAX);
		storage.remove(prefix, PMI_RATE);
		storage.remove(prefix, CLOSING_FEES);
		storage.remove(prefix, EXTRA_PAYMENT);
		storage.remove(prefix, EXTRA_PAYMENT_FREQUENCY);
		storage.remove(prefix, ARM_TYPE);					
	}
	
}
