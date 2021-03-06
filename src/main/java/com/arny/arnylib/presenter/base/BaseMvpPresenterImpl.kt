package com.arny.arnylib.presenter.base


open class BaseMvpPresenterImpl<V : BaseMvpView>() : BaseMvpPresenter<V> {
    protected var mView: V? = null

    override fun attachView(mvpView: V) {
        mView = mvpView
    }

    override fun detachView() {
        mView = null
    }

}