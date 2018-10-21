package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.AppContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecentScrobblesPresenter(from: Long, to: Long): ScrobblesPresenter(from, to) {

    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library"
    }

    override fun performOperation() {
        compositeDisposable.add(
                repository.getScrobbles(mPage, from, to)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            onLoadingSuccessful(it)
                        },
                        {
                            onException(it)
                        }
                )
        )
    }
}
