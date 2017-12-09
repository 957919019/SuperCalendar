# SuperCalendar

## 简介



<div style = "float:center">
    <img src="http://img.blog.csdn.net/20171209172937788?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjU1MjI3NQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" width="240">
<div/>


## fork自 [SuperCalendar][Tags]

[Tags]: https://github.com/MagicMashRoom/SuperCalendar
* 为选择日期而改写，可显示有date的日期

## 主要特性
* 日历样式完全自定义，拓展性强
* 左右滑动切换上下周月，上下滑动切换周月模式
* 抽屉式周月切换效果
* 标记指定日期（marker）
* 跳转到指定日期





* 新建CustomDayView实例，并作为参数构建CalendarViewAdapter


```java
	CustomDayView customDayView = new CustomDayView(
        	context , R.layout.custom_day);
	calendarAdapter = new CalendarViewAdapter(
                context ,
                onSelectDateListener ,
                Calendar.MONTH_TYPE ,
                customDayView);
```
#### 初始化View

* 目前来看 相比于Dialog选择日历 我的控件更适合于Activity/Fragment在Activity的`onCreate`   或者Fragment的`onCreateView`  你需要实现这两个方法来启动日历并装填进数据

```java
@Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        initCalendarView();
    }
    
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(
        	context , R.layout.custom_day);
		calendarAdapter = new CalendarViewAdapter(
                context ,
                onSelectDateListener ,
                Calendar.MONTH_TYPE ,
                customDayView);
        initMarkData();
        initMonthPager();
    } 
```

使用此方法回调日历点击事件
```java
private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                //your code
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示上一个月 ， 1表示下一个月
                monthPager.selectOtherMonth(offset);
            }
        };
    }
```
 
 使用此方法初始化日历标记数据
```java
private void initMarkData() {
       HashMa markData = new ArrayList<>();
       markData.add("2017-12-7");
        markData.add("2017-12-8");
        markData.add("2017-12-9");
        markData.add("2017-12-10");
        markData.add("2017-12-14");
        calendarAdapter.setMarkData(markData);
   }
```
 使用此方法给MonthPager添加上相关监听
```java
monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getAllItems();
                if(currentCalendars.get(position % currentCalendars.size()) instanceof Calendar){
                    //you code
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
```

重写onWindowFocusChanged方法，使用此方法得知calendar和day的尺寸

```java
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && !initiated) {
            CalendarDate today = new CalendarDate();
        	calendarAdapter.notifyDataChanged(today);
            initiated = true;
        }
    }
```


