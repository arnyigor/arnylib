package com.arny.arnylib.presenter.base

interface BaseMvpPresenter<in V : BaseMvpView> {
    fun attachView(mvpView: V)
    fun detachView()
}