package com.sample.myapplication.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.myapplication.Utils.LogUtil;
import com.sample.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class ListFragment extends Fragment {

    MyListAdapter myListAdapter;

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myListAdapter = new MyListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);
        RecyclerView recyclerView = setupRecyclerView(rootView);

        return rootView;
    }

    private RecyclerView setupRecyclerView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(myListAdapter);

        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myListAdapter.setActivity(this.getActivity());
        setData();
    }

    private void setData() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                LogUtil.traceFunc();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    LogUtil.error(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                LogUtil.traceFunc();
                myListAdapter.setData(Arrays.asList("one", "two", "three", "four", "five"));
            }
        }.execute();
    }

    class MyListAdapter extends RecyclerViewAdapter<ItemViewHolder, String> {
        Context context;

        @Override
        protected boolean isDataEqual(String lhs, String rhs) {
            return lhs.equalsIgnoreCase(rhs);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();

            View itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.description.setText(data.get(position));
            holder.titleName.setText(data.get(position));
            holder.issueName.setText(data.get(position));
            holder.description.setText(data.get(position));
            holder.issueDate.setText(data.get(position));

            Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.coverImage);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView coverImage;
        public TextView rankingItemOrder;
        public TextView titleName;
        public TextView issueName;
        public TextView description;
        public TextView issueDate;

        public ItemViewHolder(View itemView) {
            super(itemView);

            coverImage = (ImageView) itemView.findViewById(R.id.cover_image);
            rankingItemOrder = (TextView) itemView.findViewById(R.id.ranking_item_order);
            titleName = (TextView) itemView.findViewById(R.id.title_name);
            issueName = (TextView) itemView.findViewById(R.id.issue_name);
            description = (TextView) itemView.findViewById(R.id.description);
            issueDate = (TextView) itemView.findViewById(R.id.issue_date);
        }
    }

}
