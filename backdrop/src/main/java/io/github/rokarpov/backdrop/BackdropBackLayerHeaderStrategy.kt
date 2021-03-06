package io.github.rokarpov.backdrop

import android.animation.*
import android.view.View

internal enum class BackdropBackLayerHeaderStrategy {
    HIDE_HEADER {
        override fun getContentViewVerticalOffset(defaultBackView: View): Int {
            return 0;
        }

        override fun onLayoutBackView(
                left: Int, top: Int, right: Int, bottom: Int,
                contentView: View, headerView: View) {
            contentView.layout(left, top, left + contentView.measuredWidth, top + contentView.measuredHeight)
        }

        override fun addOnRevealHeaderViewAnimator(
                animatorSet: AnimatorSet, headerView: View,
                prevViewDuration: Long, duration: Long,
                prevStrategy: BackdropBackLayerHeaderStrategy
        ): Long {
            if (this == prevStrategy) return 0
            addHideAnimator(animatorSet, headerView, 0, prevViewDuration)
            return prevViewDuration
        }

        override fun addOnRevealHeaderViewAnimator(
                animatorSet: AnimatorSet, headerView: View,
                duration: Long
        ): Long {
            addHideAnimator(animatorSet, headerView, 0, duration)
            return duration
        }

        override fun addOnConcealHeaderViewAnimator(
                animatorSet: AnimatorSet, headerView: View,
                delay: Long, duration: Long
        ): Long {
            addShowAnimator(animatorSet, headerView, delay, duration)
            return duration
        }

        override fun updateHeaderOnReveal(headerView: View) {
            hideView(headerView)
        }

        override fun updateHeaderOnConceal(headerView: View) {
            showView(headerView)
        }
    },
    DEFAULT {
        override fun getContentViewVerticalOffset(defaultBackView: View): Int {
            return defaultBackView.measuredHeight
        }

        override fun onLayoutBackView(
                left: Int, top: Int, right: Int, bottom: Int,
                contentView: View, headerView: View) {
            val viewRight = left + contentView.measuredWidth
            val viewTop = top + headerView.measuredHeight
            val viewBottom = viewTop + contentView.measuredHeight
            contentView.layout(left, viewTop, viewRight, viewBottom)
        }

        override fun addOnRevealHeaderViewAnimator(
                animatorSet: AnimatorSet, headerView: View,
                prevViewDuration: Long, duration: Long,
                prevStrategy: BackdropBackLayerHeaderStrategy
        ): Long {
            if (this == prevStrategy) return 0
            addShowAnimator(animatorSet, headerView, prevViewDuration, duration)
            return prevViewDuration
        }

        override fun addOnRevealHeaderViewAnimator(
                animatorSet: AnimatorSet, headerView: View,
                duration: Long
        ): Long = 0

        override fun addOnConcealHeaderViewAnimator(
                animatorSet: AnimatorSet, headerView: View,
                delay: Long, duration: Long
        ): Long = 0
    };

    abstract fun getContentViewVerticalOffset(defaultBackView: View): Int
    abstract fun onLayoutBackView(
            left: Int, top: Int, right: Int, bottom: Int,
            contentView: View, headerView: View)


    abstract fun addOnRevealHeaderViewAnimator(
            animatorSet: AnimatorSet, headerView: View,
            prevViewDuration: Long, duration: Long,
            prevStrategy: BackdropBackLayerHeaderStrategy
    ): Long

    abstract fun addOnRevealHeaderViewAnimator(
            animatorSet: AnimatorSet, headerView: View,
            duration: Long
    ): Long

    abstract fun addOnConcealHeaderViewAnimator(
            animatorSet: AnimatorSet, headerView: View,
            delay: Long, duration: Long
    ): Long

    open fun updateHeaderOnReveal(headerView: View) {}
    open fun updateHeaderOnConceal(headerView: View) {}

}