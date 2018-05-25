package project.roy.socialmedia.ui.tips;

import java.util.List;

import project.roy.socialmedia.data.model.Tips;

public interface TipsView {
    void onSuccessShowAllTips(List<Tips> tipsList);

    void onFailureShowAllTips(String message);

    void onSuccessShowDetailTips(Tips tips);

    void onFailureShowDetailTips(String message);
}
