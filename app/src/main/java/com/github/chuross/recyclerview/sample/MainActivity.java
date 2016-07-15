package com.github.chuross.recyclerview.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chuross.recyclerviewadapters.CompositeRecyclerAdapter;
import com.github.chuross.recyclerviewadapters.ItemAdapter;
import com.github.chuross.recyclerviewadapters.OnItemClickListener;
import com.github.chuross.recyclerviewadapters.OnItemDoubleClickListener;
import com.github.chuross.recyclerviewadapters.OnItemLongPressedListener;
import com.github.chuross.recyclerviewadapters.SpanSizeLookupBuilder;
import com.github.chuross.recyclerviewadapters.ViewItem;
import com.github.chuross.recyclerviewadapters.ViewItemAdapter;


public class MainActivity extends AppCompatActivity {

    private static final int SPAN_SIZE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CompositeRecyclerAdapter compositeAdapter = new CompositeRecyclerAdapter();

        final ItemAdapter<String, RecyclerView.ViewHolder> itemAdapter1 = new ItemAdapter<String, RecyclerView.ViewHolder>(this) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_adapter_1, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(R.id.text)).setText(get(position));
            }

            @Override
            public int getAdapterId() {
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
        itemAdapter1.setOnItemDoubleClickListener(new OnItemDoubleClickListener<String>() {
            @Override
            public void onItemDoubleClicked(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull String item) {
                Toast.makeText(holder.itemView.getContext(), "double click! adapter1:: " + item, Toast.LENGTH_SHORT).show();
            }
        });
        itemAdapter1.setOnItemLongPressListener(new OnItemLongPressedListener<String>() {
            @Override
            public void onItemLongPressed(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull String item) {
                Toast.makeText(holder.itemView.getContext(), "long press! adapter1:: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        final ItemAdapter<String, RecyclerView.ViewHolder> itemAdapter2 = new ItemAdapter<String, RecyclerView.ViewHolder>(this) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_adapter_2, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(R.id.text)).setText(get(position));
            }

            @Override
            public int getAdapterId() {
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
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_adapter_1, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(R.id.text)).setText(get(position));
            }

            // Replace adapterId, if you want to same layout ItemAdapter.
            @Override
            public int getAdapterId() {
                return R.id.extra_item_adapter;
            }
        };
        itemAdapter3.add("itemAdapter3# same layout as itemAdapter1");
        itemAdapter3.add("itemAdapter3# same layout as itemAdapter1");
        itemAdapter3.add("itemAdapter3# same layout as itemAdapter1");

        ViewItem viewItem1 = new ViewItem(this, R.layout.item_hello_world);
        ViewItem viewItem2 = new ViewItem(this, R.layout.item_append_buttom, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAdapter1.add("itemAdapter1#" + String.valueOf(itemAdapter1.getItemCount()));
                itemAdapter1.add("itemAdapter1#" + String.valueOf(itemAdapter1.getItemCount()));
            }
        });
        ViewItem viewItem3 = new ViewItem(this, R.layout.item_append_buttom, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAdapter2.add("itemAdapter2#" + String.valueOf(itemAdapter2.getItemCount()));
            }
        });

        final ViewItemAdapter viewItemAdapter = new ViewItemAdapter(this, R.id.recyclerviewadapters_itemview_adapter);

        final ViewItem viewItem4 = new ViewItem(this, R.layout.item_footer_1);
        final ViewItem viewItem5 = new ViewItem(this, R.layout.item_footer_2);
        final ViewItem viewItem6 = new ViewItem(this, R.layout.item_append_buttom, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewItemAdapter.addAll(
                        viewItem4.clone(),
                        viewItem5.clone()
                );
            }
        });

        viewItemAdapter.add(viewItem4);
        viewItemAdapter.add(viewItem5);

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
        compositeAdapter.add(viewItemAdapter);
        compositeAdapter.add(viewItem6);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);

        /*
         * simple LinearLayoutManager example
         */
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));


        /*
         * Use SpanSizeLookupBuilder, if you want to use GridLayoutManager with SpanSizeLookup.
         */
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_SIZE);
        recyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new SpanSizeLookupBuilder(this, compositeAdapter)
                .bind(viewItem1, SPAN_SIZE)
                .bind(viewItem2, SPAN_SIZE)
                .bind(viewItem3, SPAN_SIZE)
                .bind(viewItem6, SPAN_SIZE)
                .bind(viewItemAdapter, SPAN_SIZE)
                .bind(itemAdapter1, 2, 1) //can set spanSize separately.
                .bind(itemAdapter3, SPAN_SIZE)
                .build());

        recyclerView.setAdapter(compositeAdapter);
    }
}
