package com.hqyxjy.ldf.supercalendar;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * ksc
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener
{
    TextView tv_year, tv_mouth;
    MonthPager mp_pager;
    Button bt_priv, bt_today, bt_next;

    private AppCompatActivity mActivity;
    private CalendarDate currentDate;
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage;
    private ArrayList<Calendar> currentCalendars;
    private List<String> markData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        mActivity = TestActivity.this;
        initWidget();
    }

    private void initWidget()
    {
        currentDate = new CalendarDate();

        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_mouth = (TextView) findViewById(R.id.tv_mouth);
        mp_pager = (MonthPager) findViewById(R.id.mp_pager);
        bt_priv = (Button) findViewById(R.id.bt_priv);
        bt_today = (Button) findViewById(R.id.bt_today);
        bt_next = (Button) findViewById(R.id.bt_next);
        bt_priv.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        bt_today.setOnClickListener(this);
        tv_year.setText(currentDate.getYear() + "年");
        tv_mouth.setText(currentDate.getMonth() + "");
        mp_pager.setViewheight(Utils.dpi2px(mActivity, 270));

        onSelectDateListener = new OnSelectDateListener()
        {
            @Override
            public void onSelectDate(CalendarDate date)
            {
                refreshClickDate(date);
                if (!markData.contains(date.toString()))
                {
                    Toast.makeText(mActivity, "这一天很安静，什么也没发生！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSelectOtherMonth(int offset)
            {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                mp_pager.selectOtherMonth(offset);
            }
        };

        CustomDayView customDayView = new CustomDayView(mActivity, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                mActivity,
                onSelectDateListener,
                CalendarAttr.CalendayType.MONTH,
                customDayView);

        markData = new ArrayList<>();
        markData.add("2017-12-7");
        markData.add("2017-12-8");
        markData.add("2017-12-9");
        markData.add("2017-12-10");
        markData.add("2017-12-14");
        calendarAdapter.setMarkData(markData);

        mp_pager.setAdapter(calendarAdapter);
        mp_pager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);

        mp_pager.setPageTransformer(false, new ViewPager.PageTransformer()
        {
            @Override
            public void transformPage(View page, float position)
            {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        mp_pager.addOnPageChangeListener(new MonthPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) instanceof Calendar)
                {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    tv_year.setText(date.getYear() + "年");
                    tv_mouth.setText(date.getMonth() + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    private void refreshClickDate(CalendarDate date)
    {
        currentDate = date;
        tv_year.setText(date.getYear() + "年");
        tv_mouth.setText(date.getMonth() + "");
    }

    private void refreshMonthPager()
    {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        tv_year.setText(today.getYear() + "年");
        tv_mouth.setText(today.getMonth() + "");
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.bt_priv:
                mp_pager.setCurrentItem(mp_pager.getCurrentPosition() - 1);
                break;
            case R.id.bt_today:
                refreshMonthPager();
                break;
            case R.id.bt_next:
                mp_pager.setCurrentItem(mp_pager.getCurrentPosition() + 1);
                break;
        }
    }
}
