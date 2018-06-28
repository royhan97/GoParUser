package project.roy.socialmedia.ui.childrendevelopment;

import java.util.List;

import project.roy.socialmedia.data.model.DDTK;

public interface ChildrenDevelopmentView {
    void onSuccessShowAllDdtk(List<DDTK> ddtkList);

    void onFailedShowAllDdtk(String messages);

    void onFailedPostDDTK();

    void onSuccessPostDDTK();
}
