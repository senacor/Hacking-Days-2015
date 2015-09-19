package com.senacor.hackingdays.lmax.lmax.fraud;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class FraudDirectoryWithMap<T extends FraudDirectory> implements FraudDirectory {
  private Map<String, T> map = new HashMap<>();

  private Function<Profile, String> getKey;
  Class<T> clazz;

  public FraudDirectoryWithMap(Class<T> clazz, Function<Profile, String> getKey) {
    this.clazz = clazz;
    this.getKey = getKey;
  }

  @Override
  public void addEntry(Profile profile) {
    String key = getKey.apply(profile);

    T entry = map.get(key);
    if (entry == null) {
      try {
        entry = clazz.newInstance();
        map.put(key, entry);
      } catch (InstantiationException|IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    entry.addEntry(profile);
  }

  @Override
  public List<FraudEntry> findEntries(Profile profile) {
    String key = getKey.apply(profile);

    T entry = map.get(key);
    if (entry == null) {
      try {
        entry = clazz.newInstance();
        map.put(key, entry);

        return entry.findEntries(profile);
      } catch (InstantiationException|IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    return entry.findEntries(profile);
  }
}
