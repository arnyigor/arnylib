package com.arny.arnylib.presenter.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.arny.arnylib.utils.ToastMaker

abstract class BaseMvpFragment<in V : BaseMvpView, T : BaseMvpPresenter<V>> : Fragment(), BaseMvpView {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this as V)
    }

    protected abstract val mPresenter: T

    override fun showError(error: String?) {
        ToastMaker.toastError(context, error)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.detachView()
    }
}