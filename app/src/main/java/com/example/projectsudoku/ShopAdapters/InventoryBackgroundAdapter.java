package com.example.projectsudoku.ShopAdapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsudoku.InventoryBackgroundItem;
import com.example.projectsudoku.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class InventoryBackgroundAdapter extends RecyclerView.Adapter<InventoryBackgroundAdapter.ViewHolder> {


    private DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


    //variables used
    private ArrayList<InventoryBackgroundItem> items_arrayList = new ArrayList<>();
    private Context context;


    public InventoryBackgroundAdapter(Context context, ArrayList<InventoryBackgroundItem> items_arrayList) {
        this.items_arrayList = items_arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.itemImage.setImageResource(this.items_arrayList.get(position).getItemImage());
        holder.itemName.setText(this.items_arrayList.get(position).getItemName());
        holder.itemDesc.setText(this.items_arrayList.get(position).getItemDesc());
        holder.itemEquip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Do you want to equip the "+holder.itemName.getText().toString()+" background?");
                alertDialog.setCancelable(true);

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String name = holder.itemName.getText().toString();
                        reff.child("currentBackground").setValue(name);
                        Toast.makeText(context, "The background was changed!",Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.create().show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return items_arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImage;
        TextView itemName;
        TextView itemDesc;
        Button itemEquip;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.background_item_image);
            itemName = itemView.findViewById(R.id.background_item_name);
            itemDesc = itemView.findViewById(R.id.background_item_desc);
            itemEquip = itemView.findViewById(R.id.background_item_equip);
        }
    }
}

