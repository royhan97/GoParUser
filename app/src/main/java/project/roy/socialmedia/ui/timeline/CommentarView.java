package project.roy.socialmedia.ui.timeline;

import java.util.List;

import project.roy.socialmedia.data.model.Commentar;
import project.roy.socialmedia.data.model.Reminder;

public interface CommentarView {
    void onFailedPostCommentar(String message);

    void onSuccessPostCommentar(String message);

    void onSuccessShowAllCommentarByTimelineId(List<Commentar> reminderList);

    void onFailedShowAllCommentarByTimelineId(String messages);

    void onFailedDeleteCommentar(String message);

    void onSuccessDeleteCommentar(String message);
}
