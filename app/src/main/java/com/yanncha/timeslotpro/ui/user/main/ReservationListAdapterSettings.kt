package com.yanncha.timeslotpro.ui.user.main

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
import com.yanncha.timeslotpro.network.models.ReservationWithCourseDetails
import com.yanncha.timeslotpro.ui.user.book.BookCourseViewModel
import com.yanncha.timeslotpro.util.Level
import com.yanncha.timeslotpro.util.picasso

class ReservationsViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemCourseWithCoachBinding.bind(itemView)
    val context = itemView.context
}

class ReservationListAdapter(private val viewModel: MainUserViewModel)
    : ListAdapter<ReservationWithCourseDetails, ReservationsViewHolder>(MyCourseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_course_with_coach, parent, false)
            .let {
                ReservationsViewHolder(it)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReservationsViewHolder, position: Int) {
        getItem(position).let { reservation ->
            with(holder.binding) {
                tvItemTheme.text = reservation.intitule
                tvItemNiveau.text = reservation.niveau
                picasso(reservation.urlImageCoach, ivItemAvatarCoach)
                tvItemNomCoach.text = reservation.nomCoach
                tvItemPrenomCoach.text = reservation.prenomCoach
                tvItemNbParticipants.text = reservation.limite_participants.toString()
                tvItemDate.text = reservation.date
                tvItemHeureDebut.text = reservation.heure_debut
                tvItemHeureFin.text = reservation.heure_fin

                tvItemTheme.setTextColor(
                    ContextCompat.getColor(
                        holder.context,
                        when (reservation.niveau) {
                            Level.DEBUTANT -> R.color.pink
                            Level.CONFIRME -> R.color.primaryBlue
                            else -> R.color.yellow
                        }
                    )
                )

                tvItemNiveau.setBackgroundResource(
                    when (reservation.niveau) {
                        Level.DEBUTANT -> R.drawable.textview_background_pink
                        Level.CONFIRME -> R.drawable.textview_background_blue
                        else -> R.drawable.textview_background_yellow
                    }
                )

                clItemCourse.setBackgroundResource(
                    when (reservation.niveau) {
                        Level.DEBUTANT -> R.drawable.edit_text_background_pink
                        Level.CONFIRME -> R.drawable.edit_text_background_blue
                        else -> R.drawable.edit_text_background_yellow
                    }
                )

                clItemCourse.setOnClickListener {
                    viewModel.onReservationSelected(reservation)
                }

                val isCourseFull = reservation.nbInscrits >= reservation.limite_participants
                val inscriptionText = if (isCourseFull) {
                    "Complet"
                } else {
                    "${reservation.nbInscrits} participant(s) inscrit(s) / ${reservation.limite_participants}"
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

    class MyCourseDiffUtil : DiffUtil.ItemCallback<ReservationWithCourseDetails>() {
        override fun areItemsTheSame(oldItem: ReservationWithCourseDetails, newItem: ReservationWithCourseDetails) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ReservationWithCourseDetails, newItem: ReservationWithCourseDetails) =
            oldItem == newItem
    }
}