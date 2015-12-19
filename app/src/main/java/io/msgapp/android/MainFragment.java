package io.msgapp.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.msgapp.android.model.ChatPreview;

public class MainFragment extends Fragment {

    @Bind(R.id.recyclerView_main)
    protected RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private OnFragmentInteractionListener mListener;

//    public static MainFragment newInstance(String param1, String param2) {
//        MainFragment fragment = new MainFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<ChatPreview> list = new ArrayList<>();
        list.add(new ChatPreview("", "John", "Hey man!!", "20:34", ChatPreview.STATUS_SENT));
        list.add(new ChatPreview("", "Lara", "Good night ^~^", "20:03", ChatPreview.STATUS_READ));
        list.add(new ChatPreview("", "Frank", "I'll check that", "19:22", ChatPreview.STATUS_DELIVERED));
        list.add(new ChatPreview("", "Bob", "Ok, send me an email", "14:46", ChatPreview.STATUS_READ));
        list.add(new ChatPreview("", "Jack", "You too", "12:27", 0));

        MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(list);
        adapter.setClickListener(new MainRecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                TextView userName = (TextView) v.findViewById(R.id.user_name_chat_preview);
                Log.d(BuildVars.LOG_TAG, position + " " + userName.getText().toString());
            }
        });
        recyclerView.setAdapter(adapter);

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
