package com.arny.arnylib.presenter.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arny.arnylib.utils.ToastMaker

abstract class BaseMvpActivity<in V : BaseMvpView, T : BaseMvpPresenter<V>> : AppCompatActivity(), BaseMvpView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this as V)
    }

    protected abstract val mPresenter: T

    override fun toastError(error: String?) {
        ToastMaker.toastError(this, error)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}