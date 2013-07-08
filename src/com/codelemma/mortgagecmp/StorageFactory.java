package com.codelemma.mortgagecmp;

import com.codelemma.mortgagecmp.accounting.Storage;

import android.content.SharedPreferences;

public class StorageFactory {
	public static Storage create(SharedPreferences sharedPreferences) {
		return new SharedPreferencesStorage(sharedPreferences);
	}
}
