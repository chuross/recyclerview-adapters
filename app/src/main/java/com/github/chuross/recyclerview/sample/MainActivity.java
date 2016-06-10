package com.github.chuross.recyclerview.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chuross.recyclerviewadapters.CompositeRecyclerAdapter;
import com.github.chuross.recyclerviewadapters.ItemAdapter;
import com.github.chuross.recyclerviewadapters.OnItemClickListener;
import com.github.chuross.recyclerviewadapters.ViewItem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CompositeRecyclerAdapter compositeAdapter = new CompositeRecyclerAdapter();

        final ItemAdapter<String, RecyclerView.ViewHolder> itemAdapter1 = new ItemAdapter<String, RecyclerView.ViewHolder>(this) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_adapter_1, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(R.id.text)).setText(get(position));
            }

            @Override
            public int getAdapterType() {
                return R.layout.item_adapter_1;
            }
        };
        itemAdapter1.add("itemAdapter1#default");
        itemAdapter1.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onItemClicked(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull String item) {
                Toast.makeText(holder.itemView.getContext(), "click! adapter1:: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        final ItemAdapter<String, RecyclerView.ViewHolder> itemAdapter2 = new ItemAdapter<String, RecyclerView.ViewHolder>(this) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_adapter_2, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(R.id.text)).setText(get(position));
            }

            @Override
            public int getAdapterType() {
                return R.layout.item_adapter_2;
            }
        };
        itemAdapter2.add("itemAdapter2#default");
        itemAdapter2.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onItemClicked(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull String item) {
                Toast.makeText(holder.itemView.getContext(), "click! adapter2:: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        // same as itemAdapter1 layout resource
        final ItemAdapter<String, RecyclerView.ViewHolder> itemAdapter3 = new ItemAdapter<String, RecyclerView.ViewHolder>(this) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_adapter_1, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(R.id.text)).setText(get(position));
            }

            @Override
            public int getAdapterType() {
                return R.layout.item_adapter_1;
            }
        };
        itemAdapter3.add("itemAdapter3# same layout as itemAdapter1");
        itemAdapter3.add("itemAdapter3# same layout as itemAdapter1");
        itemAdapter3.add("itemAdapter3# same layout as itemAdapter1");

        ViewItem viewItem1 = new ViewItem(this, R.layout.item_hello_world);
        ViewItem viewItem2 = new ViewItem(this, R.layout.item_append_buttom) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                holder.itemView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemAdapter1.add("itemAdapter1#" + String.valueOf(itemAdapter1.getItemCount()));
                        itemAdapter1.add("itemAdapter1#" + String.valueOf(itemAdapter1.getItemCount()));
                    }
                });
            }
        };
        ViewItem viewItem3 = new ViewItem(this, R.layout.item_append_buttom) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                holder.itemView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemAdapter2.add("itemAdapter2#" + String.valueOf(itemAdapter2.getItemCount()));
                    }
                });
            }
        };
        ViewItem viewItem4 = new ViewItem(this, R.layout.item_footer_1);
        ViewItem viewItem5 = new ViewItem(this, R.layout.item_footer_2);

        /*
        compositeAdapter.addAll(
            viewItem1,
            itemAdapter1,
            viewItem2,
            ...
        );
         */
        compositeAdapter.add(viewItem1);
        compositeAdapter.add(itemAdapter1);
        compositeAdapter.add(viewItem2);
        compositeAdapter.add(itemAdapter2);
        compositeAdapter.add(viewItem3);
        compositeAdapter.add(itemAdapter3);
        compositeAdapter.add(viewItem4);
        compositeAdapter.add(viewItem5);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(compositeAdapter);
    }
}
