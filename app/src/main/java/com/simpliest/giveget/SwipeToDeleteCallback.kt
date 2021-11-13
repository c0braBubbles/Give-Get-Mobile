package com.simpliest.giveget

import android.content.Context
import android.graphics.*
import androidx.recyclerview.widget.ItemTouchHelper
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

//Denne klassen er en tilpasset versjon av en klasse funnet på nettet
abstract class SwipeToDeleteCallback(context: Context?) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.baseline_delete_white_24dp) }
    private val intrinsicWidth = deleteIcon?.intrinsicWidth
    private val intrinsicHeight = deleteIcon?.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")
    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // Rød bakgrunn
        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        if (c != null) {
            background.draw(c)
        }

        // Posisjon til slett ikon
        val iconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - intrinsicHeight
        val iconRight = itemView.right - iconMargin
        val iconBottom = iconTop + intrinsicHeight

        // Slett ikon
        if (deleteIcon != null) {
            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        }
        if (deleteIcon != null) {
            if (c != null) {
                deleteIcon.draw(c)
            }
        }

        if (c != null) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
}