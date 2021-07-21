package com.example.projectsudoku.ShopAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsudoku.R;
import com.example.projectsudoku.ShopGameBackgroundItem;
import com.example.projectsudoku.ShopProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopGameBackgroundAdapter extends RecyclerView.Adapter<ShopGameBackgroundAdapter.ViewHolder> {

    private static final String TAG = "ShopGameBackgroundAdapt";
    

    //variables used
    private ArrayList<ShopGameBackgroundItem> items_arrayList = new ArrayList<>();
    private Context context;

    private DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private DatabaseReference reffToShop = FirebaseDatabase.getInstance().getReference("Shop");
    
    public ShopGameBackgroundAdapter(Context context, ArrayList<ShopProduct> items_arrayList_demo) {

        for(ShopProduct item : items_arrayList_demo){
            if(item instanceof ShopGameBackgroundItem){
                ShopGameBackgroundItem itemV2 = (ShopGameBackgroundItem) item;
                this.items_arrayList.add(itemV2);
            }
        }
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_background_shop_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.itemImage.setImageResource(this.items_arrayList.get(position).getItemImage());
        holder.itemName.setText(this.items_arrayList.get(position).getName());
        holder.itemDesc.setText(this.items_arrayList.get(position).getDesc());
        holder.itemPrice.setText(String.valueOf(this.items_arrayList.get(position).getPrice()));

        holder.setBackgroundItemClickListener(new BackgroundItemClickListener() {
            @Override
            public void onClick(View view, final int position) {
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
                                final int coins_num = Integer.parseInt(coins);
                                String item_price_string = holder.itemPrice.getText().toString();
                                final int item_price_int = Integer.parseInt(item_price_string);
                                if(coins_num - item_price_int >= 0) {
                                    reffToShop.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int current_index = position+1;
                                            switch (current_index){
                                                case 1:
                                                    if(snapshot.child("Item1").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                                                        Toast.makeText(context, "Item already owned!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        reff.child("coins").setValue(coins_num - item_price_int);
                                                        reffToShop.child("Item1").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        Toast.makeText(context, "Item was bought!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;
                                                case 2:{
                                                    if(snapshot.child("Item2").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                                                        Toast.makeText(context, "Item already owned!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        reff.child("coins").setValue(coins_num - item_price_int);
                                                        reffToShop.child("Item2").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        Toast.makeText(context, "Item was bought!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;
                                                }
                                                case 3:{
                                                    if(snapshot.child("Item3").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                                                        Toast.makeText(context, "Item already owned!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        reff.child("coins").setValue(coins_num - item_price_int);
                                                        reffToShop.child("Item3").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        Toast.makeText(context, "Item was bought!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;
                                                }
                                                case 4:{
                                                    if(snapshot.child("Item4").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                                                        Toast.makeText(context, "Item already owned!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        reff.child("coins").setValue(coins_num - item_price_int);
                                                        reffToShop.child("Item4").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        Toast.makeText(context, "Item was bought!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;
                                                }
                                                case 5:{
                                                    if(snapshot.child("Item5").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                                                        Toast.makeText(context, "Item already owned!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        reff.child("coins").setValue(coins_num - item_price_int);
                                                        reffToShop.child("Item5").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        Toast.makeText(context, "Item was bought!",Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.d(TAG, "onCancelled: Problem Occurred");
                                        }
                                    });
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
        return items_arrayList.size();
    }

    public interface BackgroundItemClickListener{
        void onClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView itemImage;
        TextView itemName;
        TextView itemDesc;
        TextView itemPrice;

        private BackgroundItemClickListener backgroundItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemDesc = itemView.findViewById(R.id.item_desc);
            itemPrice = itemView.findViewById(R.id.item_price);

            itemView.setOnClickListener(this);
        }

        public void setBackgroundItemClickListener(BackgroundItemClickListener backgroundItemClickListener){
            this.backgroundItemClickListener = backgroundItemClickListener;
        }

        @Override
        public void onClick(View view) {
            backgroundItemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
