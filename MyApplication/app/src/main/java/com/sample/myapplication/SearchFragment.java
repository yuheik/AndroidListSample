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

import com.sample.myapplication.Fragments.GridFragment;
import com.sample.myapplication.Fragments.RecyclerViewAdapter;
import com.sample.myapplication.Utils.LogUtil;
import com.sample.myapplication.Utils.UIUtil;

public class SearchFragment extends GridFragment {
    private String currentKeyword = "";
    private EditText editText;

    @Override
    protected int getLayoutId() {
        return R.layout.search_fragment;
    }

    @Override
    protected RecyclerViewAdapter getRecyclerViewAdapter() {
        return new GridItemAdapter(FlickrManager.Type.SEARCH);
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
        FlickrManager.search(keyword, new FlickrManager.PhotosListener() {
            @Override
            public void get(@Nullable FlickrManager.Photos photos) {
                recyclerView.smoothScrollToPosition(0);
                recyclerViewAdapter.setData(photos.getPhotos());
            }
        });

        UIUtil.hideKeyboard(editText);
    }

    @Override
    protected void setData() {
        recyclerViewAdapter.setData(null);
    }
}
