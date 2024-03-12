package com.yanncha.timeslotpro.ui.coach.main

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.ItemCourseBinding
import com.yanncha.timeslotpro.network.models.CourseWithCoachDetails
import com.yanncha.timeslotpro.util.Level


class CoursesViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemCourseBinding.bind(itemView)
    val context = itemView.context
}

class CoursesListAdapter(private val viewModel: MainCoachViewModel)
    : ListAdapter<CourseWithCoachDetails, CoursesViewHolder>(MyCourseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_course, parent, false)
            .let {
                CoursesViewHolder(it)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        getItem(position).let { course ->
            with(holder.binding) {
                tvItemTheme.text = course.intitule
                tvItemNiveau.text = course.niveau
                tvItemNbParticipants.text = course.limite_participants.toString()
                tvItemDate.text = course.date
                tvItemHeureDebut.text = course.heure_debut
                tvItemHeureFin.text= course.heure_fin

                tvItemTheme.setTextColor(
                    ContextCompat.getColor(
                        holder.context,
                        when (course.niveau) {
                            Level.DEBUTANT -> R.color.primaryBlue
                            Level.CONFIRME -> R.color.pink
                            else -> R.color.yellow
                        }
                    )
                )

                tvItemNiveau.setBackgroundResource(
                    when (course.niveau) {
                        Level.DEBUTANT -> R.drawable.textview_background_blue
                        Level.CONFIRME -> R.drawable.textview_background_pink
                        else -> R.drawable.textview_background_yellow
                    }
                )

                clItemCourse.setBackgroundResource(
                    when (course.niveau) {
                        Level.DEBUTANT -> R.drawable.edit_text_background_blue
                        Level.CONFIRME -> R.drawable.edit_text_background_pink
                        else -> R.drawable.edit_text_background_yellow
                    }
                )

                clItemCourse.setOnClickListener{
                    viewModel.onCourseSelected(course)
                }

                val isCourseFull = course.nbInscrits >= course.limite_participants
                val inscriptionText = if (isCourseFull) {
                    "Complet : ${course.nbInscrits}/${course.limite_participants}"
                } else {
                    "${course.nbInscrits} participant(s) inscrit(s) / ${course.limite_participants}"
                }
                tvItemNbParticipants.text = inscriptionText

                val textColor = if (isCourseFull) {
                    ContextCompat.getColor(holder.context, R.color.pink)
                } else {
                    ContextCompat.getColor(holder.context, R.color.black)
                }
                tvItemNbParticipants.setTextColor(textColor)

            }
        }
    }

    class MyCourseDiffUtil : DiffUtil.ItemCallback<CourseWithCoachDetails>() {
        override fun areItemsTheSame(oldItem: CourseWithCoachDetails, newItem: CourseWithCoachDetails) =
            oldItem.courseId == newItem.courseId

        override fun areContentsTheSame(oldItem: CourseWithCoachDetails, newItem: CourseWithCoachDetails) =
            oldItem == newItem
    }
}