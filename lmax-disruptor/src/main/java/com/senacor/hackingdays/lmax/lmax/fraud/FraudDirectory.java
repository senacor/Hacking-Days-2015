package com.senacor.hackingdays.lmax.lmax.fraud;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FraudDirectory {

  public List<FraudEntry> findEntries(Profile profile);
  public void addEntry(Profile profile);

}
