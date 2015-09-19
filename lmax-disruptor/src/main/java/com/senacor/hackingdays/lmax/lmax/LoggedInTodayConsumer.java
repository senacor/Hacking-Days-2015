package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.Calendar;
import java.util.Date;

public class LoggedInTodayConsumer extends CompletableConsumer {

    private final Calendar cal = Calendar.getInstance();
    private final Calendar today;

    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        today = cal;
    }

    private int loggedInToday;

    public LoggedInTodayConsumer(int expectedMessages, Runnable onComplete) {
        super(expectedMessages, onComplete);
    }

    @Override
    protected void onComplete() {
        System.out.println(loggedInToday + " users logged in today.");
    }

    @Override
    protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {
        cal.setTime(profile.getActivity().getLastLogin());
        boolean sameDay = cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
        if (sameDay) {
            loggedInToday++;
        }
    }
}
