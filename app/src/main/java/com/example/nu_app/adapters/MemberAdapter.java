package com.example.nu_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nu_app.OnDeleteListener;
import com.example.nu_app.OnEditListener;
import com.example.nu_app.R;
import com.example.nu_app.models.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Member> listPerson;
    private OnDeleteListener deleteListener;
    private OnEditListener editListener;

    public MemberAdapter(List<Member> listPerson, OnDeleteListener deleteListener, OnEditListener
            editListener){
        this.listPerson = listPerson;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_member, parent,
                false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MemberViewHolder personViewHolder = (MemberViewHolder)holder;
        personViewHolder.bindData(listPerson.get(position));
        personViewHolder.getView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View e) {
                deleteListener.deleteItem(listPerson.get(position));
                return true;
            }
        });
        personViewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                editListener.editItem(listPerson.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPerson.size();
    }
}

class MemberViewHolder extends RecyclerView.ViewHolder{

    private View view;
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewOccupation;

    public MemberViewHolder(View view) {
        super(view);
        this.view = view;
        textViewFirstName = view.findViewById(R.id.textViewFirstName);
        textViewLastName = view.findViewById(R.id.textViewLastName);
        textViewOccupation = view.findViewById(R.id.textViewOccupation);
    }

    public View getView(){
        return  view;
    }

    public void bindData(Member person){
        textViewFirstName.setText(person.getFirstName());
        textViewLastName.setText(person.getLastName());
        textViewOccupation.setText(person.getOccupation());

    }
}