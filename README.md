[![](https://jitpack.io/v/chuross/recyclerview-adapters.svg)](https://jitpack.io/#chuross/recyclerview-adapters)

# RecyclerViewAdapters
Easy way to build `RecyclerView.Adapter`.
This adapter also has multiple ItemViewType, if needed.

![sample](https://cloud.githubusercontent.com/assets/1422031/24061304/42627b7e-0b9a-11e7-97d1-14a6cabcfd59.gif)

## Download
### Gradle
1. add JitPack repository to your project root `build.gradle`.
```
repositories {
    maven { url "https://jitpack.io" }
}
```

2. add the dependency
```
dependencies {
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters:1.x.x'
}
```

#### RxJava2 support
if you use RxJava2, you can use Rx support extension!
This extension provide

```
dependencies {
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters:1.x.x'
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters-rx:1.x.x'
}
```

## Usage
See sample project.

### Header and footer RecyclerView
Basic sample

```
CompositeRecyclerAdapter compositeAdapter = new CompositeRecyclerAdapter();

// header
ViewItemAdapter header = new ViewItemAdapter(this) {
    @Override
    public int getAdapterId() {
        return R.id.header;
    }
};
header.add(new ViewItem(this, R.layout.item_header_1));
header.add(new ViewItem(this, R.layout.item_header_2));

// footer
ViewItemAdapter footer = new ViewItemAdapter(this) { ... };
footer.add(new ViewItem(this, R.layout.item_footer_1));

ItemAdapter itemAdapter = new ItemAdapter<String, SOMETHING_VIEW_HOLDER>(this) {
    // Override adapterId, onCreateViewHolder, onBindViewHolder
}
itemAdapter.add("hoge");
itemAdapter.add("fuga");
itemAdapter.add("piyo");

// Add all adapters
compositeAdapter.add(header);
compositeAdapter.add(itemAdapter);
compositeAdapter.add(footer);

RecyclerView list = //ex (RecyclerView) findViewById(this, R.id.list);

list.setLayoutManager(new LinearLayoutManager(this));
list.setAdapter(compositeAdapter); // or list.setAdapter(itemAdapter);
```

### get child items
```
LocalAdapterItem localItem = compositeAdapter.getLocalAdapterItem(100);

localItem.getLocalAdapter();
localItem.getLocalAdapterPosition();

// ex)
itemAdapter.get(localItem);
```

### listeners
```
ItemAdapter itemAdapter = new ItemAdapter() { ... }

// item click
itemAdapter.setOnItemClickListener(new OnItemClickListener() {
    void onItemClicked(RecyclerView.ViewHolder holder, int position, String item) { ... }
});

// item double click
itemAdapter.setOnItemDoubleClickListener(new OnItemDoubleClickListener() {
  void onItemDoubleClicked(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull T item) { ... }
});

// item long click
itemAdapter.setOnItemLongPressListener(new OnItemLongPressedListener() {
  void onItemLongPressed(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull T item) { ... }
});
```

### SpanSize Lookup support
```
GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_SIZE);

SpanSizeLookup spanSizeLookup =  new SpanSizeLookupBuilder(this, compositeAdapter)
        .bind(viewItem1, SPAN_SIZE)
        .bind(viewItemAdapter, SPAN_SIZE)
        .bind(itemAdapter1, 1) // specific span size
        .bind(itemAdapter2, SPAN_SIZE)
        .build();

gridLayoutManager.setSpanSizeLookup(spanSizeLookup);

// You must use CompositeRecyclerAdapter!!
recyclerView.setAdapter(compositeRecyclerAdapter);
```
