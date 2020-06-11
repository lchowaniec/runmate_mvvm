package com.lchowaniec.runmate_me.ui.profile.friends

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator

import com.lchowaniec.runmate_me.R
import kotlinx.android.synthetic.main.fragment_base_friend_fragment.*


class BaseFriendFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_base_friend_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG","onViewCreated ${findNavController().currentDestination}" )
        initViewPager()
    }

    private fun initViewPager(){
        val adapter = FriendStateAdapter(childFragmentManager,lifecycle)
        val names = arrayListOf<String>("ADD FRIEND","REQUESTS","FRIENDS LIST" )
        val pager = view?.findViewById<ViewPager2>(R.id.pager)
        pager?.adapter = adapter
        pager?.let {
            TabLayoutMediator(tab_layout, it){ tab, position->
                tab.text = names.get(position)
            }.attach()
        }

    }

}
