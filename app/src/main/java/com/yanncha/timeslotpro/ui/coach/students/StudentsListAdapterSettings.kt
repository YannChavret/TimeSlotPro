package com.yanncha.timeslotpro.ui.coach.students

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.ItemStudentBinding
import com.yanncha.timeslotpro.network.models.User
import com.yanncha.timeslotpro.util.picasso

class StudentsListViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemStudentBinding.bind(itemView)
    val context = itemView.context
}

class StudentsListAdapter(private val viewModel: StudentsViewModel)
    : ListAdapter<User, StudentsListViewHolder>(MyCourseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsListViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_student, parent, false)
            .let {
                StudentsListViewHolder(it)
            }
    }

    override fun onBindViewHolder(holder: StudentsListViewHolder, position: Int) {
        getItem(position).let { user ->
            with(holder.binding) {

                picasso(user.url_image, ivItemAvatarUser)
                tvItemNomUser.text = user.nom
                tvItemPrenomUser.text = user.prenom
                tvItemNiveau.text = user.niveau
                tvItemCredits.text = "${user.credit} crédits"

                clItemUser.setOnClickListener {
                    it.findNavController().navigate(StudentsFragmentDirections.actionStudentsFragmentToUserEditDialogFragment(user.id, user.prenom, user.nom))
                }
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