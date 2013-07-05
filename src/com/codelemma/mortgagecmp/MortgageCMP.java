package com.codelemma.mortgagecmp;

import java.util.HashMap;

import com.codelemma.mortgagecmp.accounting.Account;
import com.codelemma.mortgagecmp.accounting.FixedRateFixedPrincipalMortgageFactory;
import com.codelemma.mortgagecmp.accounting.FixedRateVariablePrincipalMortgageFactory;
import com.codelemma.mortgagecmp.accounting.Mortgage;
import com.codelemma.mortgagecmp.accounting.MortgageFactory;
import com.codelemma.mortgagecmp.accounting.UniversalMortgageFactory;

import android.app.Application;

public class MortgageCMP extends Application {

	private static final String FIXED_RATE_VARIABLE_PRINCIPAL = "frvp";
	private static final String FIXED_RATE_FIXED_PRINCIPAL = "frfp";
	
    private int simStartMonth = -1; // not set yet
    private int simStartYear = -1;  // not set yet
    private int simulationLength = 360;// 50*12;
    private Account account;  
    private static MortgageCMP appInstance;
	private boolean needToRecalculate = true;
	private Mortgage currentMortgage;
    private UniversalMortgageFactory universal_mortgage_factory;

    @Override
    public void onCreate() {        
        super.onCreate();
        appInstance = this;

        HashMap<String, MortgageFactory> mortgage_factories_map = new HashMap<String, MortgageFactory>();
        mortgage_factories_map.put(FIXED_RATE_VARIABLE_PRINCIPAL, new FixedRateVariablePrincipalMortgageFactory());
        mortgage_factories_map.put(FIXED_RATE_FIXED_PRINCIPAL, new FixedRateFixedPrincipalMortgageFactory());
        universal_mortgage_factory = new UniversalMortgageFactory(mortgage_factories_map);
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

	public int getSimulationLength() {
		return simulationLength;
	}
	
	public void setAccount() {
		account = new Account();
	}

	public Account getAccount(){
	    return account;
	}

    public Mortgage getCurrentMortgage() {
    	return currentMortgage;
    }
    
    public void setCurrentMortgage(Mortgage mortgage) {
    	currentMortgage = mortgage;
    }
}