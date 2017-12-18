package by.d1makrat.library_fm;

import by.d1makrat.library_fm.model.User;

public interface GetUserInfoAsynctaskCallback {
    void onUserInfoReceived(User user);
    void onException(Exception exception);
}