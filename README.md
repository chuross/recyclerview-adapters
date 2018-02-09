[![](https://jitpack.io/v/chuross/recyclerview-adapters.svg)](https://jitpack.io/#chuross/recyclerview-adapters)　[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-recyclerview--adapters-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5666)

# RecyclerViewAdapters
Easy way to build `RecyclerView.Adapter`.
This adapter also has multiple ItemViewType, if needed.

![sample](https://cloud.githubusercontent.com/assets/1422031/24061304/42627b7e-0b9a-11e7-97d1-14a6cabcfd59.gif)

## Download
### Gradle
1. add JitPack repository to your project root `build.gradle`.
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```

2. add the dependency
[![](https://jitpack.io/v/chuross/recyclerview-adapters.svg)](https://jitpack.io/#chuross/recyclerview-adapters)

```groovy
dependencies {
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters:1.x.x'
}
```

#### RxJava2 support
if you use RxJava2, you can use Rx support extension!

This extension provide databinding via Flowable.

```groovy
dependencies {
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters:1.x.x'
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters-rx:1.x.x'
}
```

#### DataBinding support
if you use Android DataBinding, you can use binding support extension!

This extension provide databinding via ObservableList.

```groovy
dependencies {
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters:1.x.x'
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters-databinding:1.x.x'
}
```

## Usage
See sample project.

### Header and footer RecyclerView
Basic sample

```java
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
```java
LocalAdapterItem localItem = compositeAdapter.getLocalAdapterItem(100);

localItem.getLocalAdapter();
localItem.getLocalAdapterPosition();

// ex)
itemAdapter.get(localItem);
```

### listeners
```java
ItemAdapter itemAdapter = new ItemAdapter() { ... }

// item click
itemAdapter.setOnItemClickListener(new OnItemClickListener() {
    void onItemClicked(RecyclerView.ViewHolder holder, int position, String item) { ... }
});

// item long click
itemAdapter.setOnItemLongPressListener(new OnItemLongPressedListener() {
  void onItemLongPressed(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull T item) { ... }
});
```
## Helpers
### SpanSize Lookup support
```java
GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_SIZE);

SpanSizeLookup spanSizeLookup =  new SpanSizeLookupBuilder(compositeAdapter)
        .register(viewItem1, SPAN_SIZE)
        .register(viewItemAdapter, SPAN_SIZE)
        .register(itemAdapter2, SPAN_SIZE)
        .build();

gridLayoutManager.setSpanSizeLookup(spanSizeLookup);

// You must use CompositeRecyclerAdapter!!
recyclerView.setAdapter(compositeRecyclerAdapter);
```

### Divider item decoration support
```java
int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height);

recyclerView.addItemDecoration(new DividerItemDecorationBuilder(compositeAdapter)
        .dividerHeight(dividerHeight)
        .dividerColor(Color.BLACK)
        .register(itemAdapter1)
        .register(AppendButtonViewItem.class)
        .build());
```

### Grid padding support
```java
int padding = getResources().getDimensionPixelSize(R.dimen.padding);

recyclerView.addItemDecoration(new GridPaddingItemDecorationBuilder(compositeAdapter, padding, SPAN_SIZE)
        .paddingType(GridPaddingItemDecorationBuilder.PaddingType.BOTH)
        .register(visibleChangeButton)
        .register(itemAdapter2)
        .register(AppendButtonViewItem.class)
        .build());
```

### Item drag support
```java
ItemTouchHelper dragHelper = new DragItemTouchHelperBuilder(compositeAdapter)
        .dragFlag(ItemTouchHelper.UP)
        .dragFlag(ItemTouchHelper.DOWN)
        .register(ItemAdapter3.class)
        .build();
        
dragHelper.attachToRecyclerView(recyclerView);
```

## License
```
Copyright 2017 chuross

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
