package com.example.projectsudoku.ShopAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsudoku.R;
import com.example.projectsudoku.ShopHintsPackItem;
import com.example.projectsudoku.ShopProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



//That's the adapter for the second row of items, the hints packs which the user can purchase for certain amount of money


public class ShopHintsPackAdapter extends RecyclerView.Adapter<ShopHintsPackAdapter.ViewHolder> {

    private static final String TAG = "ShopGameBackgroundAdap2";

    //variables used
    private ArrayList<ShopHintsPackItem> items_arrayList2 = new ArrayList<>();
    private Context context;

    private DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    public ShopHintsPackAdapter(Context context, ArrayList<ShopProduct> items_arrayList2_demo) {

        for(ShopProduct item : items_arrayList2_demo){
            if(item instanceof ShopHintsPackItem){
                ShopHintsPackItem itemV2 = (ShopHintsPackItem) item;
                this.items_arrayList2.add(itemV2);
            }
        }
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hints_pack_shop_row2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.itemName2.setText(this.items_arrayList2.get(position).getHintsNumber()+" "+this.items_arrayList2.get(position).getName());
        holder.itemDesc2.setText(this.items_arrayList2.get(position).getDesc());
        holder.itemPrice2.setText(String.valueOf("Buy: "+this.items_arrayList2.get(position).getPrice()));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Are you sure you want to buy it?");
                alertDialog.setCancelable(true);

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reff.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String coins = snapshot.child("coins").getValue().toString();
                                int coins_num = Integer.parseInt(coins);
                                String item_price_string = holder.itemPrice2.getText().toString();
                                item_price_string = item_price_string.substring(item_price_string.indexOf(" ")+1);
                                int item_price_int = Integer.parseInt(item_price_string);
                                String hints_num_string = holder.itemName2.getText().toString();
                                int index = hints_num_string.indexOf(" ");
                                hints_num_string = hints_num_string.substring(0,index);
                                int hints_num = Integer.parseInt(hints_num_string);
                                String current_hints_num_string = snapshot.child("hintsNumber").getValue().toString();
                                int current_hints_num = Integer.parseInt(current_hints_num_string);
                                if(coins_num - item_price_int >= 0) {
                                    reff.child("coins").setValue(coins_num - item_price_int);
                                    reff.child("hintsNumber").setValue(current_hints_num + hints_num);
                                    Toast.makeText(context, "Item was bought!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(context, "Not enough coins :(", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d(TAG, "onCancelled: Problem Occurred");
                            }
                        });
                    }
                });
                alertDialog.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items_arrayList2.size();
    }

    public interface ItemClickListener{
        void onClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView itemName2;
        TextView itemDesc2;
        TextView itemPrice2;

        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName2 = itemView.findViewById(R.id.item_name2);
            itemDesc2 = itemView.findViewById(R.id.item_desc2);
            itemPrice2 = itemView.findViewById(R.id.item_price2);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
