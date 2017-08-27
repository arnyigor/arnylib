package com.arny.arnylib.adapters;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
//делаем класс final, чтоб никто, не дай бог, не вздумал от него наследоваться. Указываем, что VH обязательно должен расширять BindableViewHolder
public final class SimpleBindableAdapter<T, VH extends BindableViewHolder> extends RecyclerBindableAdapter<T, VH> {

	//переменная в которой будет храниться layout id нашего элемента
	@LayoutRes
	private final int layoutId;

	//класс ViewHolder эта переменная для того, чтобы можно было создать новый экземпляр ViewHolder
	Class<VH> vhClass;
	//интерфейс для взаимодействия с элементом
	BindableViewHolder.ActionListener actionListener;

	public SimpleBindableAdapter(@LayoutRes int layoutId, Class<VH> vhClass) {
		this.layoutId = layoutId;
		this.vhClass = vhClass;
	}

	public SimpleBindableAdapter(Context context,@LayoutRes int layoutId, Class<VH> vhClass) {
		this.context = context;
		this.layoutId = layoutId;
		this.vhClass = vhClass;
	}

	@Override
	protected void onBindItemViewHolder(BindableViewHolder viewHolder, int position, int type) {
		//вставляем данные во ViewHolder, ради этого метода мы и создавали BindableViewHolder
		viewHolder.bindView(context,position, getItem(position), actionListener);
	}

	@Override
	protected VH viewHolder(View view, int type) {
		//через Java Reflection создаем новый экземпляр ViewHolder
		try {
			return (VH) vhClass.getConstructor(View.class).newInstance(view);
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected int layoutId(int type) {
		return layoutId;
	}

	public void setActionListener(BindableViewHolder.ActionListener actionListener) {
		this.actionListener = actionListener;
	}
}
