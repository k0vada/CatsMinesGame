package com.example.catsminesgame.screens.game

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.catsminesgame.R
import com.example.catsminesgame.databinding.FragmentGameBinding
import com.example.catsminesgame.db.GameDatabase
import com.example.catsminesgame.model.GameResult
import kotlinx.coroutines.*
import kotlin.random.Random


class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private var miceList = mutableListOf<View>()
    private var totalClicks = 0
    private var mouseHits = 0
    private var gameRunning = true
    private var gameSpeed: Int = 1
    private val animationJobs = mutableListOf<Job>()
    private val mousePositions = mutableMapOf<View, Pair<Float, Float>>()
    private val animatorMap = mutableMapOf<View, Pair<ObjectAnimator, ObjectAnimator>>()
    private var gameStartTime: Long = 0L
    private var pauseStartTime: Long = 0L
    private var totalPauseTime: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val speed = arguments?.getInt("speed", 1) ?: 1
        val quantity = arguments?.getInt("quantity", 1) ?: 1

        binding.gameField.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (binding.gameField.width > 0 && binding.gameField.height > 0) {
                    binding.gameField.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    setupGame(quantity, speed)
                }
            }
        })

        binding.pauseButton.setOnClickListener{
            gameRunning = false
            pauseGame()
        }

        binding.gameField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                totalClicks++
                checkMouseHit(event.x, event.y)
                binding.clickCounter.text = totalClicks.toString()
            }
            true
        }

        parentFragmentManager.setFragmentResultListener("game_pause_result", this) { _, bundle ->
            val action = bundle.getString("action")
            when (action) {
                "resume" -> resumeGame()
                "exit" -> endGame()
            }
        }
    }

    private fun setupGame(quantity: Int, speed: Int) {

        gameStartTime = System.currentTimeMillis()
        totalPauseTime = 0L

        gameSpeed = speed
        for (i in 0 until quantity) {
            addNewMouse()
        }
    }

    private fun addNewMouse() {
        val mouseView = ImageView(requireContext()).apply {
            setImageResource(R.drawable.pic_mouse)
            layoutParams = ConstraintLayout.LayoutParams(350, 350)
        }

        mouseView.x = Random.nextFloat() * (binding.gameField.width - mouseView.width)
        mouseView.y = Random.nextFloat() * (binding.gameField.height - mouseView.height)

        binding.gameField.addView(mouseView)
        miceList.add(mouseView)

        startMouseMovement(mouseView)
    }

    private fun startMouseMovement(mouseView: View) {
        if (!gameRunning) return

        val job = lifecycleScope.launch {
            while (gameRunning) {
                val startX = mousePositions[mouseView]?.first ?: mouseView.x
                val startY = mousePositions[mouseView]?.second ?: mouseView.y

                val newX = Random.nextFloat() * (binding.gameField.width - mouseView.width)
                val newY = Random.nextFloat() * (binding.gameField.height - mouseView.height)

                val duration = (3000 / gameSpeed).toLong()

                withContext(Dispatchers.Main) {
                    val animatorX = ObjectAnimator.ofFloat(mouseView, "translationX", startX, newX)
                    val animatorY = ObjectAnimator.ofFloat(mouseView, "translationY", startY, newY)

                    animatorX.duration = duration
                    animatorY.duration = duration

                    animatorX.start()
                    animatorY.start()

                    animatorMap[mouseView] = Pair(animatorX, animatorY)

                    animatorX.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationEnd(animation: Animator) {
                            if(gameRunning) {
                                startMouseMovement(mouseView)
                            }
                        }
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    })
                }
                delay(duration)
            }
        }
        animationJobs.add(job)
    }

    private fun checkMouseHit(x: Float, y: Float) {
        val iterator = miceList.iterator()
        while (iterator.hasNext()) {
            val mouseView = iterator.next()
            if (x >= mouseView.x && x <= mouseView.x + mouseView.width &&
                y >= mouseView.y && y <= mouseView.y + mouseView.height) {

                mouseHits++
                binding.mouseHits.text = mouseHits.toString()

                binding.gameField.removeView(mouseView)
                iterator.remove()
                addNewMouse()
                break
            }
        }
    }

    private fun pauseGame() {
        gameRunning = false
        pauseStartTime = System.currentTimeMillis()

        for (mouse in miceList) {
            val animators = animatorMap[mouse]
            animators?.first?.pause()
            animators?.second?.pause()
        }

        val dialog = GamePauseDialogFragment()
        dialog.show(parentFragmentManager, "gamePause")
    }


    private fun resumeGame() {
        gameRunning = true

        totalPauseTime += System.currentTimeMillis() - pauseStartTime

        for (mouse in miceList) {
            startMouseMovement(mouse)
        }
    }

    private fun endGame() {
        gameRunning = false
        for (job in animationJobs) {
            job.cancel()
        }
        animationJobs.clear()

        val duration = System.currentTimeMillis() - gameStartTime - totalPauseTime
        val accuracy = if (totalClicks > 0) (mouseHits.toDouble() / totalClicks * 100).toInt() else 0

        val gameResult = GameResult(
            totalClicks = totalClicks,
            mouseHits = mouseHits,
            duration = duration,
            endTime = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            GameDatabase.getDatabase(requireContext()).gameResultDao().insertGameResult(gameResult)

            val bundle = Bundle().apply {
                putInt("totalClicks", totalClicks)
                putInt("mouseHits", mouseHits)
                putLong("duration", duration)
                putInt("accuracy", accuracy)
            }
            findNavController().navigate(R.id.action_gameFragment_to_endGameFragment, bundle)
        }
    }
}