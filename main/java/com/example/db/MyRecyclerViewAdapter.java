package com.example.db;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Note> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // данните се предават в конструктора
    MyRecyclerViewAdapter(Context context, List<Note> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.content_main2, parent, false);
        view.getLayoutParams().height = parent.getMeasuredWidth() / 6;
        return new ViewHolder(view);
    }

    // биндва данните в TextView на всеки ред
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Note note = mData.get(position);
        holder.myTextView.setText(note.getTitle());
    }

    // броя на всичко редове
    @Override
    public int getItemCount()
    {
        return mData.size();
    }


    // сторва вютата когато са скролнати(скрити) извън скрийна
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView myTextView;

        ViewHolder(View itemView)
        {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // метод за взимане на данните при клик (на позицията)
    Note getItem(int id)
    {
        return mData.get(id);
    }

    // позволява клик евента да бъде прихванат
    void setClickListener(ItemClickListener itemClickListener)
    {
        this.mClickListener = itemClickListener;
    }

    // главното активити ще може да имплементира метода
    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }



    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(String item, int position)
    {
        //mData.add(position, item);
        //notifyItemInserted(position);
    }

    //private List<Note> notesList = new ArrayList<>(); // лист от бележки
    /*public ArrayList<Note> getData() {
        return mData;
    }*/
}