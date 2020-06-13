package com.rizafu.moviedb.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*

/**
 * this is my concept for rendering views in recyclerview
 * https://gist.github.com/rizafu/bfb2aca68132faa7b948c967d3411058
 */
interface ItemModel {
    /**
     * this function for rendering layout id to view holder
     */
    @LayoutRes
    fun layoutId(): Int

    /**
     * this function for stable id in recyclerView
     */
    fun id(): Long = hashCode().toLong()

    /**
     * this function for checking data item to rendering new data.
     * for simple comparison just implement data class and use toString() function
     */
    fun contentsTheSame(newItem: ItemModel): Boolean = toString() == newItem.toString()
}

abstract class BaseViewHolder<T : ItemModel>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal var onAfterTextChanged: ((view: View, itemModel: ItemModel, text: Editable?) -> Unit)? =
        null

    internal var onItemClick: ((view: View, itemModel: ItemModel) -> Unit)? = null

    internal var sharedViewPool: RecyclerView.RecycledViewPool? = null

    /**
     * call this on init view holder, do not call this function on [bind] to avoid UI lag or memory leak
     */
    protected fun View.addOnItemClick(localOnItemClick: ((view: View, item: T) -> T)? = null) {
        setOnClickListener {
            if (itemView.tag is ItemModel) {
                onItemClick?.invoke(
                    it,
                    localOnItemClick?.invoke(it, getItemModel()) ?: getItemModel()
                )
                    ?: localOnItemClick?.invoke(it, getItemModel())
            }
        }
    }

    /**
     * set [View.setTag] on [bind] to make unique tag/key for each editText.
     * call this on init view holder, do not call this function on [bind] to avoid UI lag or memory leak
     * @param localOnAfterTextChanged implement this for manipulate data on local view holder
     */
    protected fun EditText.addOnAfterTextChanged(localOnAfterTextChanged: ((view: View, item: T, text: Editable?) -> Editable?)? = null) {

        this.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                this@addOnAfterTextChanged.removeTextChangedListener(this)

                if (itemView.tag is ItemModel) {
                    onAfterTextChanged?.invoke(
                        this@addOnAfterTextChanged, getItemModel(),
                        localOnAfterTextChanged?.invoke(
                            this@addOnAfterTextChanged,
                            getItemModel(),
                            s
                        ) ?: s
                    ) ?: localOnAfterTextChanged?.invoke(
                        this@addOnAfterTextChanged,
                        getItemModel(),
                        s
                    )
                }

                this@addOnAfterTextChanged.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * call this on init view holder, do not call this function on [bind] to avoid UI lag or memory leak
     */
    protected fun RecyclerViewListAdapter.addOnNestedItemClick(localOnItemClick: ((view: View, item: ItemModel) -> ItemModel)? = null) {
        setOnItemClick { view, item ->
            onItemClick?.invoke(view, localOnItemClick?.invoke(view, item) ?: item)
                ?: localOnItemClick?.invoke(view, item)
        }
    }

    /**
     * use shared view pool for performance reason to share view holder on nested recyclerView
     * call this on init view holder after set [RecyclerView.LayoutManager] base on layout behavior
     * do not call this function on [bind] to avoid UI lag or memory leak
     * @param maxRecycledViews value of pair is Pair<ViewType, Max>
     * @param initialPrefetchItemCount minimum number of view visible per item
     */
    protected fun RecyclerView.setSharedViewPool(
        initialPrefetchItemCount: Int = 5,
        vararg maxRecycledViews: Pair<Int, Int>
    ) {
        sharedViewPool?.let {

            (layoutManager as LinearLayoutManager).apply {
                this.recycleChildrenOnDetach = true
                this.initialPrefetchItemCount = initialPrefetchItemCount
            }

            maxRecycledViews.forEach { pair ->
                val (viewType, max) = pair
                it.setMaxRecycledViews(viewType, max)
            }

            setRecycledViewPool(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getItemModel(): T = itemView.tag as T

    fun getString(resId: Int): String = itemView.context.getString(resId)

    abstract fun bind(item: T)
}

interface ViewHolderFactory {
    @LayoutRes
    fun layoutId(): Int
    fun createViewHolder(viewItem: View): BaseViewHolder<*>
    fun bindViewHolder(viewHolder: BaseViewHolder<*>, itemModel: ItemModel)
}

object DiffItemModel : DiffUtil.ItemCallback<ItemModel>() {
    override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean =
        oldItem.id() == newItem.id()

    override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean =
        oldItem.contentsTheSame(newItem)
}

class RecyclerViewListAdapter : ListAdapter<ItemModel, BaseViewHolder<*>>(
    AsyncDifferConfig.Builder(
        DiffItemModel
    ).build()
) {

    private var isLoading: Boolean = false
    private var stopLoadMore: Boolean = false
    private val visibleThreshold = 3

    init {
        setHasStableIds(true)
    }

    private val factories: MutableList<ViewHolderFactory> = mutableListOf()

    private var nestedViewPool: RecyclerView.RecycledViewPool? = null

    private var onItemClickListener: ((view: View, item: ItemModel) -> Unit)? = null

    private var onAfterTextChanged: ((view: View, item: ItemModel, text: Editable?) -> Unit)? = null

    fun registerViewHolderFactory(viewHolderFactory: ViewHolderFactory) = apply {
        factories.add(viewHolderFactory)
        factories.distinctBy { it.layoutId() }
    }

    fun setOnItemClick(onItemClick: (view: View, item: ItemModel) -> Unit) = apply {
        this.onItemClickListener = onItemClick
    }

    fun setOnAfterTextChanged(onAfterTextChanged: (view: View, item: ItemModel, text: Editable?) -> Unit) =
        apply {
            this.onAfterTextChanged = onAfterTextChanged
        }

    fun setNestedViewPool(nestedViewPool: RecyclerView.RecycledViewPool) = apply {
        this.nestedViewPool = nestedViewPool
    }

    override fun getItemId(position: Int): Long = getItem(position).id()

    override fun getItemViewType(position: Int): Int = getItem(position).layoutId()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return factories.firstOrNull { it.layoutId() == viewType }
            ?.createViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            .also {
                it?.onAfterTextChanged = this.onAfterTextChanged
                it?.onItemClick = this.onItemClickListener
                it?.sharedViewPool = this.nestedViewPool
            } ?: throw Throwable(
            "ViewHolder of ${parent.context.resources.getResourceEntryName(
                viewType
            )} not registered yet"
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        holder.itemView.tag = getItem(position)
        factories.firstOrNull { it.layoutId() == getItemViewType(position) }
            ?.bindViewHolder(holder, getItem(position))
    }

    fun setLoadMore(recyclerView: RecyclerView, onLoadMoreListener: () -> Unit) {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = linearLayoutManager.itemCount
                val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                if (!isLoading && !stopLoadMore && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    isLoading = true
                    onLoadMoreListener.invoke()
                }
            }
        })
    }

    fun isLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun isStopLoadMore(stopLoadMore: Boolean) {
        this.stopLoadMore = stopLoadMore
    }
}