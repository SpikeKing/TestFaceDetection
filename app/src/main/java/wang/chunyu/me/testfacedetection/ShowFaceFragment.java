package wang.chunyu.me.testfacedetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 显示人脸的界面
 * <p/>
 * Created by wangchenlong on 15/12/15.
 */
public class ShowFaceFragment extends Fragment {
    private static final String ARG_SELECTION_NUM = "arg_selection_num";

    @Bind(R.id.main_fdv_face_detector) FacesDisplayView mFdvFaceDetector;
    @RawRes ArrayList<Integer> mPhotos; // 图片集合

    public ShowFaceFragment() {
        mPhotos = new ArrayList<>();
        mPhotos.add(R.raw.total_large_poster);
        mPhotos.add(R.raw.jessicajung_large_poster);
        mPhotos.add(R.raw.seohyun_large_poster);
        mPhotos.add(R.raw.sooyoung_large_poster);
        mPhotos.add(R.raw.sunny_large_poster);
        mPhotos.add(R.raw.taeyeon_large_poster);
        mPhotos.add(R.raw.tiffany_large_poster);
        mPhotos.add(R.raw.yoona_large_poster);
        mPhotos.add(R.raw.yuri_large_poster);
    }

    public static ShowFaceFragment newInstance(int selectionNum) {
        ShowFaceFragment simpleFragment = new ShowFaceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SELECTION_NUM, selectionNum);
        simpleFragment.setArguments(args);
        return simpleFragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_face, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        @RawRes int image = mPhotos.get(getArguments().getInt(ARG_SELECTION_NUM));
        InputStream stream = getResources().openRawResource(image);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        mFdvFaceDetector.setBitmap(bitmap);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
