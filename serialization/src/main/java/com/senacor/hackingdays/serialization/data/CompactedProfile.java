package com.senacor.hackingdays.serialization.data;

import java.util.function.Predicate;

public class CompactedProfile extends Profile {
    private byte[] data;

    public CompactedProfile(String name, Location location) {
        super(null, null);

        int byteArrayLength = 4+4+name.length()+location.getState().length()+location.getZip().length()+location.getCity().length();
        data = new byte[byteArrayLength];

        data[4] = (byte)name.length();
        data[5] = (byte)location.getState().length();
        data[6] = (byte)location.getZip().length();
        data[7] = (byte)location.getCity().length();

        int startIndex = 8;
        string2bytes(startIndex, name);
        startIndex += name.length();
        string2bytes(startIndex, location.getState());
        startIndex += location.getState().length();
        string2bytes(startIndex, location.getZip());
        startIndex += location.getZip().length();
        string2bytes(startIndex, location.getCity());

        // System.out.println("profile size = " + byteArrayLength);
    }

    public int getSize() {
        return data.length;
    }

    private void string2bytes(int startIndex, String string) {
        for (int i=0; i<string.length(); i++) {
            data[startIndex+i] = (byte)string.charAt(i);
        }
    }

    public String getName() {
        return new String(data, 8, data[4]);
    }

    public void setGender(Gender gender) {
        byte g = 0;
        if (gender == Gender.Female) g = 1;
        else if (gender == Gender.Male) g = 2;

        // clear
        data[0] &= 0b11100111;
        // set
        data[0] |= g << 3;
    }

    public Gender getGender() {
        if ((data[0] & 0b00001000) != 0) return Gender.Female;
        else if ((data[0] & 0b00010000) != 0) return Gender.Male;
        else return Gender.Disambiguous;
    }

    public void setRelationShipStatus(RelationShipStatus relationShipStatus) {
        byte r = 1;
        if (relationShipStatus == RelationShipStatus.Maried) r <<= 1;
        else if (relationShipStatus == RelationShipStatus.Divorced) r <<= 2;

        // clear
        data[0] &= 0b11111001;
        // set
        data[0] |= r << 1;
    }

    public RelationShipStatus getRelationShipStatus() {
        if ((data[0] & 0b00000010) != 0) return RelationShipStatus.Single;
        else if ((data[0] & 0b00000100) != 0) return RelationShipStatus.Maried;
        else return RelationShipStatus.Divorced;
    }

    public void setSmoker(boolean smoker) {
        // clear
        data[0] &= 0b11111110;
        // set
        if (smoker) data[0] |= 0b00000001;
    }

    public boolean isSmoker() {
        return (data[0] & 0b00000001) != 0;
    }

    public void setAge(int age) {
        byte a = (byte)age;

        // clear
        data[1] &= 0b10000000;
        // set
        data[1] |= a;
    }

    public int getAge() {
        return data[1] & 0b01111111;
    }

    public CompactedSeeking getCompactedSeeking() {
        return new CompactedSeeking(this);
    }

    public void setSeekingGender(Gender gender) {
        byte g = 0;
        if (gender == Gender.Female) g = 1;
        else if (gender == Gender.Male) g = 2;

        // clear
        data[0] &= 0b10011111;
        // set
        data[0] |= g << 5;
    }

    public Gender getSeekingGender() {
        if ((data[0] & 0b00100000) != 0) return Gender.Female;
        else if ((data[0] & 0b01000000) != 0) return Gender.Male;
        else return Gender.Disambiguous;
    }

    public void setSeekingAgeLower(int age) {
        byte a = (byte)age;

        // clear
        data[2] &= 0b10000000;
        // set
        data[2] |= a;
    }

    public int getSeekingAgeLower() {
        return data[2] & 0b01111111;
    }

    public void setSeekingAgeUpper(int age) {
        byte a = (byte)age;

        // clear
        data[3] &= 0b10000000;
        // set
        data[3] |= a;
    }

    public int getSeekingAgeUpper() {
        return data[3] & 0b01111111;
    }

    public CompactedActivity getCompactedActivity() {
        return new CompactedActivity(this);
    }

    public int getActivityLoginCount() {
        byte lc = 0;
        lc |= (data[0] & 0b10000000) >> 4;
        lc |= (data[1] & 0b10000000) >> 5;
        lc |= (data[2] & 0b10000000) >> 6;
        lc |= (data[3] & 0b10000000) >> 7;

        return lc;
    }

    public void setActivityLoginCount(int loginCount) {
        byte lc = (byte)loginCount;

        // clear
        data[0] &= 0b01111111;
        data[1] &= 0b01111111;
        data[2] &= 0b01111111;
        data[3] &= 0b01111111;
        // set
        lc <<= 4;
        data[0] |= (lc & 0b10000000);
        lc <<= 1;
        data[1] |= (lc & 0b10000000);
        lc <<= 1;
        data[2] |= (lc & 0b10000000);
        lc <<= 1;
        data[3] |= (lc & 0b10000000);
    }

    public CompactedLocation getCompactedLocation() {
        return new CompactedLocation(this);
    }

    public String getLocationState() {
        return new String(data, 8+data[4], data[5]);
    }

    public String getLocationZip() {
        return new String(data, 8+data[4]+data[5], data[6]);
    }

    public String getLocationCity() {
        return new String(data, 8+data[4]+data[5]+data[6], data[7]);
    }

    /**
     * Erzeugt ein Predicate, das genau dann true liefert, wenn das übergebene Profil zu den eigenen Vorstellungen
     * passt.
     *
     * @return
     */
    public Predicate<Profile> matcher() {
        final Gender seeking = this.getSeekingGender();
        final int ageLow = this.getSeekingAgeLower();
        final int ageHigh = this.getSeekingAgeUpper();
        return (Profile t) -> t.getGender() == seeking
                && t.getAge() <= ageHigh
                && t.getAge() >= ageLow;
    }

    /**
     * Prüft, ob ein gegebenes anderes Profil zu den eignen Suchkriterien passt;
     *
     * @return
     */
    public boolean match(Profile other) {
        System.out.println(dumpSeeking() + " -> " + ((CompactedProfile) other).dumpSelf());
        return other.getGender() == this.getSeekingGender()
                && other.getAge() <= this.getSeekingAgeUpper()
                && other.getAge() >= getSeekingAgeLower();
    }

    /**
     * Erzeugt ein Predicate, das genau dann true liefert, wenn beide Profile zueinander passen.
     *
     * @return
     */
    public Predicate<CompactedProfile> perfectCompactMatcher() {
        final Gender seeking = this.getSeekingGender();
        final int ageLow = this.getSeekingAgeLower();
        final int ageHigh = this.getSeekingAgeUpper();
        return (CompactedProfile t) -> t.getGender() == seeking
                && t.getAge() <= ageHigh
                && t.getAge() >= ageLow
                && t.match(this);
    }

    /**
     * Prüft, ob beide Profile zueinander passen;
     *
     * @return
     */
    public boolean perfectMatch(CompactedProfile other) {
        return other.getGender() == this.getSeekingGender()
                && other.getAge() <= getSeekingAgeUpper()
                && other.getAge() >= getSeekingAgeLower()
                && other.match(this);
    }

    String dumpSeeking() {
        return getSeekingGender() + ", " + getSeekingAgeLower() + "-" + getSeekingAgeUpper();
    }

    String dumpSelf() {
        return getGender() + ", " + getAge();
    }

}
