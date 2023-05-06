package dev.kuylar.lighttube.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import dev.kuylar.lighttube.api.LightTubeApi
import dev.kuylar.lighttube.api.models.LightTubeVideo
import dev.kuylar.lighttube.databinding.FragmentVideoInfoBinding
import dev.kuylar.lighttube.ui.activity.MainActivity
import kotlin.concurrent.thread

class VideoInfoFragment : Fragment() {
	private lateinit var id: String
	private var playlistId: String? = null
	private lateinit var binding: FragmentVideoInfoBinding
	private lateinit var api: LightTubeApi

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		api = (requireActivity() as MainActivity).api
		arguments?.let {
			id = it.getString("id", "")
			playlistId = it.getString("playlistId", "")
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentVideoInfoBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		thread {
			val video = api.getVideo(id, playlistId)
			activity?.runOnUiThread {
				fillData(video.data!!)
			}
		}
	}

	private fun fillData(video: LightTubeVideo) {
		binding.videoTitle.text = video.title
		binding.channelTitle.text = video.channel.title
		binding.videoViews.text = video.viewCount
		binding.videoUploaded.text = video.dateText
		binding.buttonLike.text = video.likeCount
		Glide
			.with(this)
			.load(video.channel.avatar)
			.into(binding.channelAvatar);
	}
}