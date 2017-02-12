package com.sample.myapplication;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sample.myapplication.Fragments.BaseFragment;
import com.sample.myapplication.Fragments.ItemViewAdapter;
import com.sample.myapplication.Utils.LogUtil;
import com.sample.myapplication.Utils.UIUtil;

import java.util.ArrayList;

public class SearchFragment extends BaseFragment {
    private String currentKeyword = "";
    private EditText editText;
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.search_fragment;
    }

    @Override
    protected ItemViewAdapter getItemViewAdapter() {
        return new PhotoItemViewAdapter(FlickrManager.Type.SEARCH,
                                        R.layout.list_item,
                                        R.layout.grid_item);
    }

    @Override
    protected boolean useSwipeRefresh() {
        return false;
    }

    @Override
    protected void setupView(View rootView) {
        editText = setupEditText(rootView);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
    }

    private EditText setupEditText(View rootView) {
        EditText editText = (EditText) rootView.findViewById(R.id.editText);
        editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                LogUtil.traceFunc("keyEvent : " + ((keyEvent != null) ? keyEvent.toString() : "null"),
                                  "action : " + actionId);

                if (keyEvent == null || keyEvent.getAction() != KeyEvent.ACTION_DOWN) { // happens on some keyboard
                    return false;
                }

                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String keyword = textView.getText().toString();
                    if (TextUtils.isEmpty(keyword) || currentKeyword.equals(keyword)) {
                        return true;
                    }

                    search(keyword);
                }

                return false;
            }
        });

        return editText;
    }

    private void search(String keyword) {
        currentKeyword = keyword;
        page = 1;
        loadSearchData(page);

        UIUtil.hideKeyboard(editText);
    }

    @Override
    protected void setData() {
        itemViewAdapter.setData(null);
    }

    @Override
    protected void loadNextData() {
        // note: this implementation is hard to find out whether if it reaches the end.
        page++;
        loadSearchData(page);
    }

    private void loadSearchData(final int pageIndex) {
        startDataLoading();
        FlickrManager.search(currentKeyword, pageIndex, new FlickrManager.PhotosListener() {
            @Override
            public void get(@Nullable ArrayList<FlickrManager.Photo> photos) {
                if (pageIndex == 1) {
                    recyclerView.smoothScrollToPosition(0);
                }
                itemViewAdapter.setData(photos);
                finishDataLoading();
            }
        });
    }
}
