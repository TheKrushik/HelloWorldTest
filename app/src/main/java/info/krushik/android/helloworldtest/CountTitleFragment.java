package info.krushik.android.helloworldtest;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class CountTitleFragment extends Fragment {

    private SharedPreferences mSettings;

    private TextView mInfoTextView;
    private TextView txtCountTitle;
    private Button mCrowsCounterButton;
    public ProgressBar m_pbStatistics;
    private int mCountPoint = 0;
    private int mMaxPoint = 1000;
    private int mCountTitle = 1;



    public CountTitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_count_title, container, false);
        mInfoTextView = (TextView) view.findViewById(R.id.infoTextView);
        txtCountTitle = (TextView) view.findViewById(R.id.txtCountTitle);
        mCrowsCounterButton = (Button) view.findViewById(R.id.buttonCrowsCounter);
        m_pbStatistics = (ProgressBar) view.findViewById(R.id.pbStatistics);
        mSettings = view.getContext().getSharedPreferences(Const.APP_SAVE_TITLE, Context.MODE_PRIVATE);

        mCrowsCounterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountPoint = mCountPoint + 50;
                mInfoTextView.setText("Я насчитал " + mCountPoint + " бал");

                if (mCountPoint > 999) {
                    mCountPoint = 0;
                    mCountTitle++;
                }
                m_pbStatistics.setMax(mMaxPoint);
                m_pbStatistics.setProgress(mCountPoint);
                txtCountTitle.setText("сечас " + mCountTitle + "-й титул и картинка");
            }
        });


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(Const.APP_SAVE_COUNTER_POINT, mCountPoint);
        editor.putInt(Const.APP_SAVE_COUNTER_TITLE, mCountTitle);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mSettings.contains(Const.APP_SAVE_COUNTER_POINT) && mSettings.contains(Const.APP_SAVE_COUNTER_TITLE)) {
            // Получаем число из настроек
            mCountPoint = mSettings.getInt(Const.APP_SAVE_COUNTER_POINT, 0);
            mCountTitle = mSettings.getInt(Const.APP_SAVE_COUNTER_TITLE, 0);
            // Выводим на экран данные из настроек
            mInfoTextView.setText("Я насчитал " + mCountPoint + " бал");
            m_pbStatistics.setMax(mMaxPoint);
            m_pbStatistics.setProgress(mCountPoint);
            txtCountTitle.setText("сечас " + mCountTitle + "-й титул и картинка");
        }
    }

}
