package com.sample.myapplication.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class GridFragment extends Fragment {
    enum TYPE {
        NUMBER,
        ALPHABET
    }
    private static final String KEY_TYPE = "key_type";

    GridAdapter gridAdapter;
    TYPE type;

    public static GridFragment newInstance(TYPE type) {
        GridFragment fragment = new GridFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_TYPE, type);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (TYPE) getArguments().get(KEY_TYPE);
            LogUtil.debug("type : " + this.type);
        }

        gridAdapter = new GridAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid, container, false);
        RecyclerView recyclerView = setupRecyclerView(rootView);

        return rootView;
    }


    private RecyclerView setupRecyclerView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(gridAdapter);

        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gridAdapter.setActivity(this.getActivity());
        setData();
    }

    private void setData() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                LogUtil.traceFunc();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    LogUtil.error(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                LogUtil.traceFunc();
                switch (type) {
                    case NUMBER:
                        gridAdapter.setData(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"));
                        break;
                    case ALPHABET:
                        gridAdapter.setData(Arrays.asList("A", "B", "C", "D", "E"));
                        break;
                    default:
                        LogUtil.error("No such type " + type);
                        break;
                }
            }
        }.execute();
    }

    class GridAdapter extends RecyclerViewAdapter<ItemViewHolder, String> {
        Context context;

        @Override
        protected boolean isDataEqual(String lhs, String rhs) {
            return lhs.equalsIgnoreCase(rhs);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();

            View itemView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.image);
            holder.index.setText(data.get(position));
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView index;

        public ItemViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_image);
            index = (TextView) itemView.findViewById(R.id.item_index);
        }
    }
}
