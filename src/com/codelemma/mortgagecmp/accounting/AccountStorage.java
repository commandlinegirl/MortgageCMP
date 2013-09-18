package com.codelemma.mortgagecmp.accounting;

import java.util.HashMap;
import java.util.Map;

import com.codelemma.mortgagecmp.accounting.Storage.OpenState;
import com.codelemma.mortgagecmp.accounting.Storage.StorageException;

/**
 * Factory(dp) which instantiates Account objects based on data read from storage.
 * This factory may throw any exceptions related to reading from storage and
 * parsing the data read.
 */
public class AccountStorage implements AccountFactory, AccountSaver {
	// Account Storage property keys.
	private static final String CURRENT_MORTGAGE = "cm";
	private static final String MORTGAGES_ID_LIST = "mil";
	private static final String MORTGAGE_CLASS_TAG = "mct";	
	
	private final Storage storage;
	private final Map<String, MortgageStorer<?>> storerByTag;
	private final Map<Class<?>, MortgageStorer<?>> storerByClass;

    public AccountStorage(Storage storage) {
    	this.storage = storage;
    	storerByTag = new HashMap<String, MortgageStorer<?>>();
    	storerByClass = new HashMap<Class<?>, MortgageStorer<?>>();
    	registerStorer(FixedRateFixedPrincipalMortgage.class, new FixedRateFixedPrincipalMortgageStorer(storage));
    	registerStorer(FixedRateVariablePrincipalMortgage.class, new FixedRateVariablePrincipalMortgageStorer(storage));
    	registerStorer(AdjustableRateMortgage.class, new AdjustableRateMortgageStorer(storage));    	
    }

	@Override
    public Account loadAccount() throws AccountFactoryException {
    	Account account = new Account();
    	try {
        	storage.open(OpenState.READ);
    		readMortgages(account);
    		readAccountData(account);    		
    	} catch (StorageException se) {
    		throw new AccountFactoryException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
    	return account;
    }

    @Override
    public void saveAccount(Account account) throws AccountSaverException {
    	try {
    		storage.open(OpenState.WRITE);
    		storage.clear();
    		writeMortgages(account);
    		writeAccountData(account);
    	} catch (StorageException se) {
    		throw new AccountSaverException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
    }

    @Override
	public void saveMortgage(Mortgage mortgage) throws AccountSaverException {
    	try {
    		storage.open(OpenState.WRITE);
    		writeMortgage(mortgage);
    	} catch (StorageException se) {
    		throw new AccountSaverException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
	}
	
    @Override
	public void clear() throws AccountSaverException {
    	try {
    		storage.open(OpenState.WRITE);
        	storage.clear();
    	} catch (StorageException se) {
    		throw new AccountSaverException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
	}

    @Override
    public void removeMortgage(Mortgage mortgage) throws AccountSaverException {
    	try {
    		storage.open(OpenState.WRITE);
    		deleteMortgage(mortgage);
    	} catch (StorageException se) {
    		throw new AccountSaverException("Storage exception occured", se);
    	} finally {
    		storage.close();
    	}
    }

    private int[] readIds() throws StorageException  {
    	String idsAsString = "";
		try {
			idsAsString = storage.getString("", MORTGAGES_ID_LIST);
		} catch (StorageException e) {
			return new int[]{};
		}
    	if (idsAsString.equals("")) {
    		return new int[]{};
    	}
    	String[] idsAsStrings = idsAsString.split(",");
    	int[] ids = new int[idsAsStrings.length];   	    	
    	try {
    		for (int i = 0; i < ids.length; i++) {
    			ids[i] = Integer.parseInt(idsAsStrings[i]);
    		}
    	} catch (NumberFormatException nfe) {
    		throw new StorageException("Could not parse ID", nfe);
    	}
    	return ids;
    }

    private void writeIds(Account account) throws StorageException {
    	StringBuilder idsBuilder = new StringBuilder();
    	for (Mortgage mortgage : account.getMortgages()) {
    		idsBuilder.append(Integer.toString(mortgage.getId()));
    		idsBuilder.append(',');
    	}
    	if (idsBuilder.length() > 0) {
    		idsBuilder.deleteCharAt(idsBuilder.length() - 1);
    	}
    	storage.putString("", MORTGAGES_ID_LIST, idsBuilder.toString());
    }

    private void readAccountData(Account account) {
		try {
			int previous_current_mortgage_id = storage.getInt("", CURRENT_MORTGAGE);
			for (Mortgage m : account.getMortgages()) {
	    		if (m.getPreviousId() == previous_current_mortgage_id) {
	    			account.setCurrentMortgage(m);
	    		}
	    	}	
		} catch (StorageException e) {
			//e.printStackTrace(); //TODO: good solution???
			return;
		}
    }

    private void writeAccountData(Account account) throws StorageException {
    	if (account.getCurrentMortgage() != null) { 
    	    storage.putInt("", CURRENT_MORTGAGE, account.getCurrentMortgage().getId());
    	}
    }

    private void readMortgages(Account account) throws StorageException {
    	for (int id : readIds()) {
    		Mortgage mortgage = readMortgage(id);
    		account.addMortgage(mortgage);
    	}
    }

    private void writeMortgages(Account account) throws StorageException {
    	writeIds(account);
    	for (Mortgage mortgage : account.getMortgages()) {
    		writeMortgage(mortgage);
    	}
    }

    private Mortgage readMortgage(int id) throws StorageException {
    	String prefix = Integer.toString(id);
    	String tag = storage.getString(prefix, MORTGAGE_CLASS_TAG);
    	MortgageStorer<? extends Mortgage> storer = storerByTag.get(tag);
    	return storer.load(id);
    }

    /**
     * Safety of this method is guaranteed by registerStorer(): under key class K storerByClass
     * holds a storer which accepts instances of K to save().
     */
    @SuppressWarnings("unchecked")
	private void writeMortgage(Mortgage mortgage) throws StorageException {
    	MortgageStorer<? super Mortgage> storer = (MortgageStorer<? super Mortgage>) storerByClass.get(mortgage.getClass());
    	String prefix = Integer.toString(mortgage.getId());
    	storage.putString(prefix, MORTGAGE_CLASS_TAG, storer.getTag());
    	storer.save(mortgage);
    }

    /**
     * Safety of this method is guaranteed by registerStorer(): under key class K storerByClass
     * holds a storer which accepts instances of K to save().
     */
    @SuppressWarnings("unchecked")
	private void deleteMortgage(Mortgage mortgage) throws StorageException {
    	MortgageStorer<? super Mortgage> storer = (MortgageStorer<? super Mortgage>) storerByClass.get(mortgage.getClass());
    	String prefix = Integer.toString(mortgage.getId());
    	storage.remove(prefix, MORTGAGE_CLASS_TAG);
    	storer.remove(mortgage);
    }

    private <T extends Mortgage> void registerStorer(Class<T> mortgageClass, MortgageStorer<T> storer) {
    	storerByTag.put(storer.getTag(), storer);
    	storerByClass.put(mortgageClass, storer);
    }
}