package project.roy.socialmedia.ui.timeline;

import java.util.List;

import project.roy.socialmedia.data.model.Timeline;

public interface TimelineView {
    void onSuccessShowTimeline(List<Timeline> timelineList);

    void onFailureShowTimeline(String messages);

    void onSuccessPostTimeline(String messages);

    void onFailurePostTimeline(String messages);
}
