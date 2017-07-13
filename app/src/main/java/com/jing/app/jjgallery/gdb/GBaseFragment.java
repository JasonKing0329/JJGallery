package com.jing.app.jjgallery.gdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/19 14:24
 */
public abstract class GBaseFragment extends Fragment {

    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentHolder) {
            bindFragmentHolder((IFragmentHolder) context);
        }
    }

    protected abstract void bindFragmentHolder(IFragmentHolder holder);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutRes(), container, false);
        initView(view);
        return view;
    }

    protected abstract int getLayoutRes();

    protected abstract void initView(View view);

    public void showProgressCycler() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.loading));
        }
        progressDialog.show();
    }

    public boolean dismissProgressCycler() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            return true;
        }
        return false;
    }

}
