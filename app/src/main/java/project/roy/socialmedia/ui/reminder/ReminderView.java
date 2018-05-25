package project.roy.socialmedia.ui.reminder;

import java.util.List;

import project.roy.socialmedia.data.model.Reminder;

public interface ReminderView {
    void onSuccessShowAllReminder(List<Reminder> reminderList);

    void onFailureShowAllReminder(String messages);

    void onSuccessShowDetailReminder(Reminder reminder);

    void onFailureShowDetailReminder(String message);
}
