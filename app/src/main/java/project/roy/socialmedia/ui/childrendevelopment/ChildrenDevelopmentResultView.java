package project.roy.socialmedia.ui.childrendevelopment;

import java.util.List;

import project.roy.socialmedia.data.model.DDTK;

public interface ChildrenDevelopmentResultView {
    void onFailedShowAllDdtk(String messages);

    void onSuccessShowAllDdtk(List<DDTK> ddtkList);
}
