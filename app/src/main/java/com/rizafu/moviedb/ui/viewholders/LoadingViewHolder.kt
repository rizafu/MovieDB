package com.rizafu.moviedb.ui.viewholders

import android.view.View
import com.rizafu.moviedb.R
import com.rizafu.moviedb.databinding.ItemLoadingBinding
import com.rizafu.moviedb.utils.BaseViewHolder
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.ViewHolderFactory

class LoadingItemModel : ItemModel {
    override fun layoutId(): Int = R.layout.item_loading
}

class LoadingViewHolder(binding: ItemLoadingBinding) :
    BaseViewHolder<LoadingItemModel>(binding.root) {

    override fun bind(item: LoadingItemModel) {
        //no-op
    }
}

class LoadingViewHolderFactory : ViewHolderFactory {
    override fun layoutId(): Int = R.layout.item_loading

    override fun createViewHolder(viewItem: View): BaseViewHolder<*> =
        LoadingViewHolder(ItemLoadingBinding.bind(viewItem))

    override fun bindViewHolder(viewHolder: BaseViewHolder<*>, itemModel: ItemModel) {
        (viewHolder as LoadingViewHolder).bind(itemModel as LoadingItemModel)
    }
}