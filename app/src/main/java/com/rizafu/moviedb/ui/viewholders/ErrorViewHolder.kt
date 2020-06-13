package com.rizafu.moviedb.ui.viewholders

import android.view.View
import com.rizafu.moviedb.R
import com.rizafu.moviedb.data.ErrorType
import com.rizafu.moviedb.databinding.ItemErrorBinding
import com.rizafu.moviedb.utils.BaseViewHolder
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.ViewHolderFactory

data class ErrorItemModel(
    val errorType: ErrorType
) : ItemModel {
    override fun layoutId(): Int = R.layout.item_error
}

class ErrorViewHolder(private val binding: ItemErrorBinding) :
    BaseViewHolder<ErrorItemModel>(binding.root) {

    init {
        binding.textTry.addOnItemClick()
    }

    override fun bind(item: ErrorItemModel) {
        when (item.errorType) {
            ErrorType.SOMETHING_WRONG -> {
                binding.textTitle.text = getString(R.string.something_wrong)
                binding.image.setAnimationFromUrl("https://assets3.lottiefiles.com/temp/lf20_0txt7u.json")
            }
            ErrorType.CONNECTION -> {
                binding.textTitle.text = getString(R.string.connection_problem)
                binding.image.setAnimationFromUrl("https://assets9.lottiefiles.com/temp/lf20_6bhHgV.json")
            }
            ErrorType.UNAUTHORIZED -> {
                binding.textTitle.text = getString(R.string.unauthorized)
                binding.image.setAnimationFromUrl("https://assets4.lottiefiles.com/packages/lf20_oSMJuN.json")
            }
            ErrorType.NOT_FOUND -> {
                binding.textTitle.text = getString(R.string.movie_not_found)
                binding.image.setAnimationFromUrl("https://assets3.lottiefiles.com/temp/lf20_0txt7u.json")
            }
        }
        binding.image.playAnimation()
    }
}

class ErrorViewHolderFactory : ViewHolderFactory {
    override fun layoutId(): Int = R.layout.item_error

    override fun createViewHolder(viewItem: View): BaseViewHolder<*> =
        ErrorViewHolder(ItemErrorBinding.bind(viewItem))

    override fun bindViewHolder(viewHolder: BaseViewHolder<*>, itemModel: ItemModel) {
        (viewHolder as ErrorViewHolder).bind(itemModel as ErrorItemModel)
    }
}