package com.yanncha.timeslotpro.ui.coach.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.ItemUserBinding
import com.yanncha.timeslotpro.network.models.User
import com.yanncha.timeslotpro.util.picasso

class UserListViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemUserBinding.bind(itemView)
    val context = itemView.context
}


class UserListAdapter(private val viewModel: DetailsCourseViewModel)
    : ListAdapter<User, UserListViewHolder>(MyCourseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_user, parent, false)
            .let {
                UserListViewHolder(it)
            }
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        getItem(position).let { user ->
            with(holder.binding) {

                picasso(user.url_image, ivItemAvatarUser)
                tvItemNomUser.text = user.nom
                tvItemPrenomUser.text = user.prenom


                clItemUser.setBackgroundResource(R.drawable.edit_text_background_yellow)
            }
        }
    }

    class MyCourseDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.id== newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) =
            oldItem == newItem
    }
}