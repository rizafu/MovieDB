package com.rizafu.moviedb.ui.viewholders

import android.view.View
import androidx.core.view.setPadding
import com.rizafu.moviedb.R
import com.rizafu.moviedb.data.EmptyType
import com.rizafu.moviedb.databinding.ItemEmptyBinding
import com.rizafu.moviedb.utils.BaseViewHolder
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.ViewHolderFactory
import com.rizafu.moviedb.utils.dp

data class EmptyItemModel(val emptyType: EmptyType) : ItemModel {
    override fun layoutId(): Int = R.layout.item_empty
}

class EmptyViewHolder(private val binding: ItemEmptyBinding) :
    BaseViewHolder<EmptyItemModel>(binding.root) {

    override fun bind(item: EmptyItemModel) {
        when (item.emptyType) {
            EmptyType.NO_MORE -> {
                binding.textTitle.text = ""
                binding.image.setImageResource(R.drawable.ic_tmdb)
                binding.image.setPadding(24.dp)
            }
            EmptyType.NO_FAVORITE -> {
                binding.textTitle.text = getString(R.string.you_have_not_favorite_movies)
                binding.image.setImageDrawable(null)
                binding.image.setPadding(0.dp)
                binding.image.setAnimationFromUrl("https://assets7.lottiefiles.com/packages/lf20_0c6GLL.json")
            }
        }
        binding.image.playAnimation()
    }
}

class EmptyViewHolderFactory : ViewHolderFactory {
    override fun layoutId(): Int = R.layout.item_empty

    override fun createViewHolder(viewItem: View): BaseViewHolder<*> =
        EmptyViewHolder(ItemEmptyBinding.bind(viewItem))

    override fun bindViewHolder(viewHolder: BaseViewHolder<*>, itemModel: ItemModel) {
        (viewHolder as EmptyViewHolder).bind(itemModel as EmptyItemModel)
    }
}