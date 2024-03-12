package com.yanncha.timeslotpro.ui.user.book

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
import com.yanncha.timeslotpro.databinding.ItemCourseWithCoachBinding
import com.yanncha.timeslotpro.network.models.CourseWithCoachDetails
import com.yanncha.timeslotpro.util.Level
import com.yanncha.timeslotpro.util.picasso


class CoursesWithCoachViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemCourseWithCoachBinding.bind(itemView)
    val context = itemView.context
}

class CourseListWithCoachAdapter(private val viewModel: BookCourseViewModel)
    : ListAdapter<CourseWithCoachDetails, CoursesWithCoachViewHolder>(MyCourseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesWithCoachViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_course_with_coach, parent, false)
            .let {
                CoursesWithCoachViewHolder(it)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CoursesWithCoachViewHolder, position: Int) {
        getItem(position).let { course ->
            with(holder.binding) {
                tvItemTheme.text = course.intitule
                tvItemNiveau.text = course.niveau
                picasso(course.urlImageCoach, ivItemAvatarCoach)
                tvItemNomCoach.text = course.nomCoach
                tvItemPrenomCoach.text = course.prenomCoach
                //tvItemNbParticipants.text = course.limite_participants.toString()
                tvItemDate.text = course.date
                tvItemHeureDebut.text = course.heure_debut
                tvItemHeureFin.text = course.heure_fin

                tvItemTheme.setTextColor(
                    ContextCompat.getColor(
                        holder.context,
                        when (course.niveau) {
                            Level.DEBUTANT -> R.color.pink
                            Level.CONFIRME -> R.color.primaryBlue
                            else -> R.color.yellow
                        }
                    )
                )

                tvItemNiveau.setBackgroundResource(
                    when (course.niveau) {
                        Level.DEBUTANT -> R.drawable.textview_background_pink
                        Level.CONFIRME -> R.drawable.textview_background_blue
                        else -> R.drawable.textview_background_yellow
                    }
                )

                clItemCourse.setBackgroundResource(
                    when (course.niveau) {
                        Level.DEBUTANT -> R.drawable.edit_text_background_pink
                        Level.CONFIRME -> R.drawable.edit_text_background_blue
                        else -> R.drawable.edit_text_background_yellow
                    }
                )

                clItemCourse.setOnClickListener {
                    viewModel.onCourseSelected(course)
                }

                val isCourseFull = course.nbInscrits >= course.limite_participants
                val inscriptionText = if (isCourseFull) {
                    "Complet: ${course.nbInscrits}/${course.limite_participants}"
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

                clItemCourse.isEnabled = !isCourseFull
            }
        }
    }

    class MyCourseDiffUtil : DiffUtil.ItemCallback<CourseWithCoachDetails>() {
        override fun areItemsTheSame(oldItem: CourseWithCoachDetails, newItem: CourseWithCoachDetails) =
            oldItem.courseId== newItem.courseId

        override fun areContentsTheSame(oldItem: CourseWithCoachDetails, newItem: CourseWithCoachDetails) =
            oldItem == newItem
    }
}