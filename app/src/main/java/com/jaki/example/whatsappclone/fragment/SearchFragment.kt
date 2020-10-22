package com.jaki.example.whatsappclone.fragment

import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jaki.example.whatsappclone.R
import com.jaki.example.whatsappclone.adapter.UserSearchItemAdapter
import com.jaki.example.whatsappclone.model.User
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search.view.search_view


class SearchFragment : Fragment() {

    private lateinit var adapterRv: UserSearchItemAdapter
    private lateinit var userId: String
    private lateinit var dbRef: DatabaseReference
    private lateinit var allUsersListener: ValueEventListener
    private var mUsers = arrayListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        adapterRv = UserSearchItemAdapter()


        allUsersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount > 0) {
                    mUsers.clear()
                    snapshot.children.forEach {
                        val user = it.getValue(User::class.java) as User
                        if (user.uid != userId) mUsers.add(user)
                    }
                    adapterRv.addUser(mUsers)
                    Log.e("Jumlah", mUsers.size.toString())
                    adapterRv.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        view.rv_serach.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = adapterRv
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbRef = FirebaseDatabase.getInstance().reference.child("User")
        dbRef.addValueEventListener(allUsersListener)
    }

    override fun onDestroyView() {
        dbRef.removeEventListener(allUsersListener)
        super.onDestroyView()
    }

}
