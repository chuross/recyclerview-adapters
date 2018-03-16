package com.github.chuross.recyclerview.sample

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.Toast
import com.github.chuross.recyclerview.sample.databinding.ActivityMainBinding
import com.github.chuross.recyclerviewadapters.*
import io.reactivex.processors.BehaviorProcessor

class MainActivity : AppCompatActivity() {

    private val maxSpanSize = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val compositeRecyclerAdapter = CompositeRecyclerAdapter()

        val headerViewItem = ViewItem(this, R.layout.item_hello_world)
        val headerVisibilityToggleButton = ViewItem(this, R.layout.visible_toggle, View.OnClickListener {
            headerViewItem.isVisible = !headerViewItem.isVisible
        })

        val itemAdapter1 = TextItemAdapter(this).also {
            it.add("itemAdapter1#0")
            it.setOnItemClickListener { _, _, item ->
                Toast.makeText(this, "click! adapter1::$item", Toast.LENGTH_SHORT).show()
            }
            it.setOnItemLongPressListener { _, _, item ->
                Toast.makeText(this, "long press! adapter1::$item", Toast.LENGTH_SHORT).show()
            }
        }
        val itemAdapter1AppendButton = AppendButtonViewItem(this, View.OnClickListener {
            itemAdapter1.add("itemAdapter1#" + itemAdapter1.itemCount)
            itemAdapter1.add("itemAdapter1#" + itemAdapter1.itemCount)
        })

        // RxJava#Flowable binding adapter
        val itemAdapter2RxSource = BehaviorProcessor.createDefault(listOf("itemAdapter2#0"))
        val itemAdapter2 = GridTextItemAdapter(this, itemAdapter2RxSource).also {
            it.setOnItemClickListener { _, _, item ->
                Toast.makeText(this, "click! adapter2::$item", Toast.LENGTH_SHORT).show()
            }
        }
        val itemAdapter2AppendButton = AppendButtonViewItem(this, View.OnClickListener {
            val value = "RxItemAdapter2#" + itemAdapter2.itemCount.toString()
            itemAdapter2RxSource.onNext(itemAdapter2RxSource.value.plus(value))
        })
        val itemAdapter2VisibilityToggleButton = ViewItem(this, R.layout.visible_toggle, View.OnClickListener {
            itemAdapter2.setVisible(!itemAdapter2.isVisible, true)
        })

        val itemAdapter3 = TextItemAdapter(this, Color.MAGENTA).also {
            it.add("itemAdapter3# same as itemAdapter1's layout [draggable] #1")
            it.add("itemAdapter3# same as itemAdapter1's layout [draggable] #2")
            it.add("itemAdapter3# same as itemAdapter1's layout [draggable] #3")
        }

        val nestedTextAdapter = TextItemAdapter(this).also {
            it.add("nested item adapter #1-1")
            it.add("nested item adapter #1-1")
        }
        val deepNestedTextAdapter = TextItemAdapter(this, Color.parseColor("#388E3C")).also {
            it.add("deep nested item [draggable] #2-1")
            it.add("deep nested item [draggable] #2-2")
        }
        val nestedAdapter = CompositeRecyclerAdapter().also {
            it.add(nestedTextAdapter)
            it.add(CompositeRecyclerAdapter().also {
                it.add(deepNestedTextAdapter)
            })
        }

        val viewItemAdapter = ViewItemAdapter(this).also {
            it.add(ViewItem(this, R.layout.item_footer_1))
            it.add(ViewItem(this, R.layout.item_footer_2))
        }
        val viewItemAdapterAppendButton = AppendButtonViewItem(this, View.OnClickListener {
            viewItemAdapter.add(ViewItem(this, R.layout.item_footer_1))
        })

        /**
         * add to CompositeRecyclerAdapter
         * will be merged all LocalAdapter!
         */
        compositeRecyclerAdapter.addAll(
                headerViewItem,
                headerVisibilityToggleButton,
                itemAdapter1,
                itemAdapter1AppendButton,
                itemAdapter2,
                itemAdapter2AppendButton,
                itemAdapter2VisibilityToggleButton,
                itemAdapter3,
                nestedAdapter,
                viewItemAdapter,
                viewItemAdapterAppendButton
        )


        /**
         * setup RecyclerView by CompositeRecyclerAdapter.
         */
        val recyclerView = binding.list
        val gridLayoutManager = GridLayoutManager(this, maxSpanSize)

        recyclerView.also {
            // can use LinearLayoutManager
            it.layoutManager = gridLayoutManager
            it.adapter = compositeRecyclerAdapter
        }


        /**
         * SpanSizeLookup support
         * @warning must be use GridLayoutManager!!
         */
        gridLayoutManager.spanSizeLookup = SpanSizeLookupBuilder(compositeRecyclerAdapter)
                .register(headerViewItem, maxSpanSize)
                .register(headerVisibilityToggleButton, maxSpanSize)
                .register(itemAdapter1, maxSpanSize)
                .register(itemAdapter2VisibilityToggleButton, maxSpanSize)
                .register(itemAdapter3, maxSpanSize)
                .register(nestedTextAdapter, maxSpanSize)
                .register(deepNestedTextAdapter, maxSpanSize)
                .register(viewItemAdapter, maxSpanSize)
                .register(AppendButtonViewItem::class.java, maxSpanSize) //can also register Class
                .build()


        /**
         * GridPadding support
         * @warning must be use GridLayoutManager!!
         */
        val padding = 16
        recyclerView.addItemDecoration(GridPaddingItemDecorationBuilder(compositeRecyclerAdapter, padding, maxSpanSize)
                .paddingType(GridPaddingItemDecorationBuilder.PaddingType.BOTH)
                .register(headerVisibilityToggleButton)
                .register(itemAdapter2)
                .register(AppendButtonViewItem::class.java)
                .build())

        val nestedItemPadding = 64
        recyclerView.addItemDecoration(GridPaddingItemDecorationBuilder(compositeRecyclerAdapter, nestedItemPadding, maxSpanSize)
                .paddingType(GridPaddingItemDecorationBuilder.PaddingType.VERTICAL)
                .register(deepNestedTextAdapter)
                .build())


        /**
         * List item divider support
         */
        val dividerHeight = resources.getDimensionPixelSize(R.dimen.divider_height) // 1dp

        recyclerView.addItemDecoration(DividerItemDecorationBuilder(compositeRecyclerAdapter)
                .dividerHeight(dividerHeight)
                .dividerColor(Color.BLACK)
                .register(headerVisibilityToggleButton)
                .register(itemAdapter1)
                .register(itemAdapter1AppendButton)
                .register(itemAdapter2AppendButton)
                .register(nestedTextAdapter)
                .register(deepNestedTextAdapter)
                .build())

        recyclerView.addItemDecoration(DividerItemDecorationBuilder(compositeRecyclerAdapter)
                .dividerHeight(dividerHeight)
                .dividerColor(Color.MAGENTA)
                .register(itemAdapter3)
                .build())

        recyclerView.addItemDecoration(DividerItemDecorationBuilder(compositeRecyclerAdapter)
                .dividerHeight(dividerHeight)
                .dividerColor(Color.parseColor("#388E3C"))
                .register(deepNestedTextAdapter)
                .build())


        /**
         * Dragging support
         */
        val dragHelper = DragItemTouchHelperBuilder(compositeRecyclerAdapter)
                .dragFlag(ItemTouchHelper.UP)
                .dragFlag(ItemTouchHelper.DOWN)
                .register(itemAdapter3)
                .register(deepNestedTextAdapter)
                .build()
        dragHelper.attachToRecyclerView(recyclerView)
    }

}