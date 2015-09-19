package com.senacor.hackingdays.lmax.lmax.fraud;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public class ListFraudDirectory implements FraudDirectory {

  private float MAXIDENT = 0.6F;

  private List<FraudEntry> list = new ArrayList<>();

  @Override
  public List<FraudEntry> findEntries(Profile profile) {

    List<FraudEntry> entries = new ArrayList<>();

    list.stream().parallel().filter(p -> p.similarness(profile) > MAXIDENT).forEach(p -> entries.add(p));

    long count = entries.stream().parallel().filter(p -> p.similarness(profile) > 0.99).count();
    if (count == 0) {
      FraudEntry entry = new FraudEntry();
      entry.setFrom(profile);
      list.add(entry);
    }

    return entries;
  }

  @Override
  public void addEntry(Profile profile) {

    FraudEntry entry = new FraudEntry();
    entry.setFrom(profile);
    list.add(entry);
  }
}
