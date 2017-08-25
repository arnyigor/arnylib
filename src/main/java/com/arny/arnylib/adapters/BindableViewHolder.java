package com.arny.arnylib.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.View;
public abstract class BindableViewHolder<T> extends RecyclerView.ViewHolder {

    public BindableViewHolder(View itemView) {
        super(itemView);
    }

    //в этом методе будет происходить обработка данных, сейчас вешается OnItemClickListener
    public void bindView(final int position, final T item, final ActionListener actionListener) {
        if (actionListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.OnItemClickListener(position, item);
                }
            });
        }
    }

    //это интерфейс который мы будем в дальнейшем расширять
    public interface ActionListener {
        void OnItemClickListener(int position, Object Item);
    }
}