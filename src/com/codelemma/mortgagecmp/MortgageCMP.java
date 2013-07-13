package com.codelemma.mortgagecmp;

import java.util.HashMap;

import com.codelemma.mortgagecmp.accounting.Account;
import com.codelemma.mortgagecmp.accounting.AccountSaver;
import com.codelemma.mortgagecmp.accounting.AccountSaver.AccountSaverException;
import com.codelemma.mortgagecmp.accounting.AccountStorage;
import com.codelemma.mortgagecmp.accounting.FixedRateFixedPrincipalMortgageFactory;
import com.codelemma.mortgagecmp.accounting.FixedRateVariablePrincipalMortgageFactory;
import com.codelemma.mortgagecmp.accounting.MortgageFactory;
import com.codelemma.mortgagecmp.accounting.MortgageNameConstants;
import com.codelemma.mortgagecmp.accounting.SafeAccountFactory;
import com.codelemma.mortgagecmp.accounting.Storage;
import com.codelemma.mortgagecmp.accounting.UniversalMortgageFactory;

import android.app.Application;
import android.preference.PreferenceManager;

public class MortgageCMP extends Application {

    private int simStartMonth = -1; // not set yet
    private int simStartYear = -1;  // not set yet
    private Account account;  
    private static MortgageCMP appInstance;
    private UniversalMortgageFactory universal_mortgage_factory;
	private SafeAccountFactory accountFactory;
	private AccountSaver accountSaver;

    @Override
    public void onCreate() {        
        super.onCreate();
        appInstance = this;
        HashMap<String, MortgageFactory> mortgage_factories_map = new HashMap<String, MortgageFactory>();
        mortgage_factories_map.put(
        		MortgageNameConstants.FIXED_RATE_VARIABLE_PRINCIPAL, 
        		new FixedRateVariablePrincipalMortgageFactory());
        mortgage_factories_map.put(
        		MortgageNameConstants.FIXED_RATE_FIXED_PRINCIPAL, 
        		new FixedRateFixedPrincipalMortgageFactory());
        universal_mortgage_factory = new UniversalMortgageFactory(mortgage_factories_map);
		Storage storage = StorageFactory.create(
				PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext()));
		AccountStorage accountStorage = new AccountStorage(storage);
		accountSaver = accountStorage;
        accountFactory = new SafeAccountFactory(accountStorage);
    }

    public static MortgageCMP getInstance() {
         return appInstance;
    }

    public UniversalMortgageFactory getUniversalMortgageFactory() {
    	return universal_mortgage_factory;
    }

	public void setSimulationStartYear(int simStartYear){
	    this.simStartYear = simStartYear;
	}		

	public void setSimulationStartMonth(int simStartMonth){
	    this.simStartMonth = simStartMonth;
	}	
	
	public int getSimulationStartYear(){
	    return simStartYear;
	}		
	
	public int getSimulationStartMonth(){
	    return simStartMonth;
	}
	
	public void setAccount() {
		account = accountFactory.loadAccount();
	}

	public Account getAccount(){
	    return account;
	}

	public void saveAccount() {
		try {
			accountSaver.saveAccount(account);
		} catch (AccountSaverException se) {
			// TODO for Ola
			se.printStackTrace();
		}
	}
}