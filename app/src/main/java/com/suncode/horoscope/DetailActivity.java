package com.suncode.horoscope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.suncode.horoscope.base.Constant;
import com.suncode.horoscope.helper.ApiClient;
import com.suncode.horoscope.model.Horoscope;
import com.suncode.horoscope.service.ApiService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "CHECKTAG";
    private ApiService mApiService;
    private ShimmerFrameLayout mDetailShimmer;

    private ImageView mBackButtonImageView;

    private Chip mTodayChip;
    private Chip mTommorowChip;
    private Chip mYesterdayChip;

    private ChipGroup mDayChipGroup;

    private TextView mTitleDetail;
    private TextView mIntervalDetail;
    private TextView mDescDetail;
    private TextView mCompatibilityDetail;
    private TextView mMoodDetail;
    private TextView mNumberDetail;
    private TextView mTimeDetail;
    private TextView mColorDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //hide action bar
        getSupportActionBar().hide();
        //transparent status/notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //change color text in status/notification bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mApiService = ApiClient.horoscopeBuild().create(ApiService.class);

        mDetailShimmer = findViewById(R.id.shimmer_detail);

        //chipgroup initilize
        mDayChipGroup = findViewById(R.id.chipgroup_detail_days);

        //chip initialize
        mTodayChip = findViewById(R.id.chip_detail_today);
        mTommorowChip = findViewById(R.id.chip_detail_tommorow);
        mYesterdayChip = findViewById(R.id.chip_detail_yesterday);

        //textview initilize
        mTitleDetail = findViewById(R.id.textView_detail_title);
        mIntervalDetail = findViewById(R.id.textView_detail_interval);
        mDescDetail = findViewById(R.id.textView_detail_content);
        mCompatibilityDetail = findViewById(R.id.textView_detail_compatibility);
        mMoodDetail = findViewById(R.id.textView_detail_mood);
        mNumberDetail = findViewById(R.id.textView_detail_luckynumber);
        mTimeDetail = findViewById(R.id.textView_detail_luckytime);
        mColorDetail = findViewById(R.id.textView_detail_color);

        //get data first load
        getData(Constant.DAY_TODAY);

        chipClicked();

        //for back button
        mBackButtonImageView = findViewById(R.id.imagebutton_detail_back);
        mBackButtonImageView.setOnClickListener(v -> finish());
    }

    private void chipClicked() {
        //function for chip clicked
        mYesterdayChip.setOnClickListener(v -> {
            chipChangeColor(mYesterdayChip, true);
            chipChangeColor(mTodayChip, false);
            chipChangeColor(mTommorowChip, false);

            getData(Constant.DAY_YESTERDAY);
        });

        mTodayChip.setOnClickListener(v -> {
            chipChangeColor(mYesterdayChip, false);
            chipChangeColor(mTodayChip, true);
            chipChangeColor(mTommorowChip, false);

            getData(Constant.DAY_TODAY);
        });

        mTommorowChip.setOnClickListener(v -> {
            chipChangeColor(mYesterdayChip, false);
            chipChangeColor(mTodayChip, false);
            chipChangeColor(mTommorowChip, true);

                getData(Constant.DAY_TOMORROW);
        });
    }

    private void chipChangeColor(Chip chip, Boolean b) {
        if (b) {
            chip.setChipBackgroundColorResource(android.R.color.white);
            chip.setChipStrokeColorResource(R.color.colorThree);
            chip.setChipStrokeWidth(1);
            chip.setTextColor(getResources().getColor(R.color.colorThree));
        } else {
            chip.setChipBackgroundColorResource(R.color.colorThree);
            chip.setChipStrokeWidth(0);
            chip.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    private float dpToFloat(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private String getHoroscopeName() {
        Intent intent = getIntent();
        return intent.getStringExtra("HOROSCOPE_NAME");
    }

//    private void getData(String day) {
//        //clear textview before adding new data
//        mTitleDetail.setText("");
//        mIntervalDetail.setText("");
//        mDescDetail.setText("");
//        mCompatibilityDetail.setText("");
//        mMoodDetail.setText("");
//        mNumberDetail.setText("");
//        mTimeDetail.setText("");
//        mColorDetail.setText("");
//
//        //showing shimmer
//        showShimmer(true);
//
//        Call<Horoscope> horoscopeCall = mApiService.getData(getHoroscopeName(), day);
//
//        horoscopeCall.enqueue(new Callback<Horoscope>() {
//            @Override
//            public void onResponse(@NonNull Call<Horoscope> call, @NonNull Response<Horoscope> response) {
//                Horoscope horoscope = response.body();
//                Log.d(TAG, "horoscopeCall: "+ response.body());
//                Log.d(TAG, "onResponse: "+ response.message());
//                Log.d(TAG, "onResponse: "+ response.code());
//                try {
//                    if (response.errorBody() != null) {
//                        Log.d(TAG, "onResponse: "+ response.errorBody().string());
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                //set textview data
//                mTitleDetail.setText(getHoroscopeName());
//                if (horoscope != null) {
//                    mIntervalDetail.setText(horoscope.getDateRange());
//                    mDescDetail.setText(horoscope.getDescription());
//                    mCompatibilityDetail.setText(horoscope.getCompatibility());
//                    mMoodDetail.setText(horoscope.getMood());
//                    mNumberDetail.setText(horoscope.getLuckyNumber());
//                    mTimeDetail.setText(horoscope.getLuckyTime());
//                    mColorDetail.setText(horoscope.getColor());
//                }
//
//
//                //stopping shimmer
//                showShimmer(false);
//            }
//
//            @Override
//            public void onFailure(Call<Horoscope> call, Throwable t) {
//                //stopping shimmer
//                Log.d(TAG, "onFailure: "+ t.getMessage());
//                showShimmer(false);
//            }
//        });
//    }

    private void getData(String day) {
        //clear textview before adding new data
        mTitleDetail.setText("");
        mIntervalDetail.setText("");
        mDescDetail.setText("");
        mCompatibilityDetail.setText("");
        mMoodDetail.setText("");
        mNumberDetail.setText("");
        mTimeDetail.setText("");
        mColorDetail.setText("");

        //showing shimmer
        showShimmer(true);

        // Commented out API call
        // Call<Horoscope> horoscopeCall = mApiService.getData(getHoroscopeName(), day);
        // This would be your sample data method call instead of API call
        Horoscope horoscope = getSampleHoroscope(getHoroscopeName(), day);

        // Rest of your code remains the same from here
        // set textview data
        mTitleDetail.setText(getHoroscopeName());
        mIntervalDetail.setText(horoscope.getDateRange());
        mDescDetail.setText(horoscope.getDescription());
        mCompatibilityDetail.setText(horoscope.getCompatibility());
        mMoodDetail.setText(horoscope.getMood());
        mNumberDetail.setText(horoscope.getLuckyNumber());
        mTimeDetail.setText(horoscope.getLuckyTime());
        mColorDetail.setText(horoscope.getColor());

        //stopping shimmer
        showShimmer(false);
    }
    private Horoscope getSampleHoroscope(String horoscopeName, String day) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        // Adjust the date based on the day parameter
        if (day.equals(Constant.DAY_TOMORROW)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Add 1 day for tomorrow
            currentDate = dateFormat.format(calendar.getTime());
        } else if (day.equals(Constant.DAY_YESTERDAY)) {
            calendar.add(Calendar.DAY_OF_MONTH, -1); // Subtract 1 day for yesterday
            currentDate = dateFormat.format(calendar.getTime());
        }

        Log.d(TAG, "getSampleHoroscope: "+ currentDate);
        // Generate sample data based on horoscopeName
        String compatibility = "Compatibility for " + horoscopeName;
        return generateSampleHoroscope(day,currentDate, compatibility, horoscopeName);
    }
    private Horoscope getAriesHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Aries: As an Aries, you may feel a surge of energy and determination today, propelling you towards your goals with confidence and enthusiasm. Your adventurous spirit and natural leadership qualities will shine bright, inspiring those around you. Embrace the opportunities that come your way and trust your instincts to guide you towards success. Remember to stay focused and take bold steps towards your dreams.";
                mood = "Relaxed";
                color = "Spring Green";
                luckyNumber = "64";
                luckyTime = "7am";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Aries: Tomorrow promises to be an exciting day for Aries individuals, filled with new possibilities and opportunities. You may feel a strong urge to explore new horizons and embrace challenges with enthusiasm. Trust your intuition and take calculated risks to achieve your goals. Stay optimistic and maintain a positive attitude as you navigate through the day.";
                mood = "Energetic";
                color = "Yellow";
                luckyNumber = "42";
                luckyTime = "8am";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday was a day of reflection and introspection for Aries individuals. You may have felt a strong desire to reassess your goals and priorities, seeking clarity and direction in your life. Take time to recharge your batteries and nurture your inner fire. Use this moment of pause to realign your actions with your passions and dreams";
                mood = "Tense";
                color = "Red";
                luckyNumber = "78";
                luckyTime = "6am";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getTaurusHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Taurus: As a Taurus, you may experience a sense of stability and groundedness today, allowing you to tackle tasks with determination and focus. Your practical approach to life will help you overcome challenges with ease. Trust in your abilities and take the necessary steps towards your long-term goals.";
                mood = "Calm";
                color = "Earth Tones";
                luckyNumber = "55";
                luckyTime = "9am";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Taurus: Tomorrow holds the promise of new beginnings and fresh opportunities for Taurus individuals. You may feel a surge of creativity and inspiration, making it an ideal time to pursue artistic endeavors or creative projects. Embrace your unique talents and let your imagination soar.";
                mood = "Hopeful";
                color = "Pastel Shades";
                luckyNumber = "37";
                luckyTime = "10am";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Taurus: Yesterday, you may have found yourself reflecting on your values and priorities, seeking harmony and balance in your life. Take time to connect with nature and indulge in activities that bring you joy and serenity. Trust in the universe's timing and stay true to yourself.";
                mood = "Reflective";
                color = "Green";
                luckyNumber = "69";
                luckyTime = "8am";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getGeminiHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Gemini: As a Gemini, you may feel a sense of curiosity and intellectual stimulation today, fueling your desire to learn and explore new ideas. Embrace opportunities for communication and networking, as they may lead to exciting collaborations and connections. Stay adaptable and open-minded as you navigate through the day.";
                mood = "Curious";
                color = "Yellow";
                luckyNumber = "42";
                luckyTime = "9am";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Gemini: Tomorrow brings the promise of new adventures and opportunities for Gemini individuals. You may feel a surge of energy and enthusiasm, making it an ideal time to pursue your passions and interests. Trust your instincts and follow your heart as you embark on new journeys.";
                mood = "Optimistic";
                color = "Sky Blue";
                luckyNumber = "33";
                luckyTime = "10am";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Gemini: Yesterday, you may have found yourself reflecting on your communication style and interpersonal relationships, seeking harmony and understanding in your interactions. Take time to listen to others' perspectives and express yourself openly and honestly. Embrace the power of communication to foster deeper connections.";
                mood = "Reflective";
                color = "Silver";
                luckyNumber = "56";
                luckyTime = "8am";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getCancerHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Cancer: As a Cancer, you may experience a deep sense of emotional connection and intuition today, guiding you towards nurturing and supporting those you care about. Embrace your nurturing nature and create a harmonious environment around you. Trust your instincts and listen to your inner voice as you navigate through the day.";
                mood = "Nurturing";
                color = "Silver";
                luckyNumber = "39";
                luckyTime = "10am";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Cancer: Tomorrow holds the promise of new beginnings and emotional growth for Cancer individuals. You may feel inspired to explore your creative side and express your emotions freely. Take time to connect with your loved ones and create meaningful memories together.";
                mood = "Inspired";
                color = "White";
                luckyNumber = "51";
                luckyTime = "11am";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Cancer: Yesterday, you may have found yourself focusing on your home and family life, seeking security and comfort in familiar surroundings. Take time to nurture yourself and your loved ones, creating a sense of warmth and belonging. Trust in the power of love and emotional connections to bring you peace.";
                mood = "Content";
                color = "Cream";
                luckyNumber = "72";
                luckyTime = "9am";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getLeoHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Leo: As a Leo, you may feel a surge of confidence and charisma today, inspiring those around you with your leadership qualities. Embrace your natural charm and shine bright in social situations. Trust your instincts and take bold actions towards your goals, knowing that success is within reach.";
                mood = "Confident";
                color = "Gold";
                luckyNumber = "78";
                luckyTime = "11am";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Leo: Tomorrow brings exciting opportunities for personal growth and self-expression for Leo individuals. You may feel inspired to pursue your passions and showcase your talents to the world. Embrace the spotlight and let your creativity shine.";
                mood = "Optimistic";
                color = "Orange";
                luckyNumber = "88";
                luckyTime = "2pm";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Leo: Yesterday, you may have found yourself reflecting on your goals and ambitions, seeking clarity and direction in your path forward. Take time to reconnect with your inner fire and reignite your passion for life. Trust in your abilities and embrace new opportunities with enthusiasm.";
                mood = "Reflective";
                color = "Purple";
                luckyNumber = "67";
                luckyTime = "10am";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getVirgoHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Virgo: As a Virgo, you may feel a sense of practicality and attention to detail today, making it an excellent time to tackle tasks and organize your life. Embrace your analytical nature and focus on efficiency in your daily activities. Trust your instincts and strive for perfection in everything you do.";
                mood = "Focused";
                color = "Green";
                luckyNumber = "21";
                luckyTime = "12pm";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Virgo: Tomorrow brings opportunities for growth and self-improvement for Virgo individuals. You may feel inspired to pursue new skills or hobbies that align with your interests. Embrace the journey of self-discovery and trust in your ability to adapt to change.";
                mood = "Curious";
                color = "Blue";
                luckyNumber = "33";
                luckyTime = "1pm";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Virgo: Yesterday, you may have found yourself seeking harmony and balance in your surroundings. Take time to nurture your relationships and connect with loved ones on a deeper level. Trust in the power of communication and express your emotions openly.";
                mood = "Harmonious";
                color = "Silver";
                luckyNumber = "39";
                luckyTime = "10am";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getLibraHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Libra: As a Libra, you may find yourself mediating conflicts and bringing harmony to your relationships today. Embrace your diplomatic nature and strive for fairness and balance in all your interactions. Trust your intuition and listen to others' perspectives with an open mind.";
                mood = "Diplomatic";
                color = "Blue";
                luckyNumber = "33";
                luckyTime = "1pm";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Libra: Tomorrow brings opportunities for creative expression and self-expression for Libra individuals. You may feel inspired to pursue artistic endeavors or engage in meaningful conversations with loved ones. Embrace your unique talents and share your ideas with confidence.";
                mood = "Creative";
                color = "Purple";
                luckyNumber = "33";
                luckyTime = "2pm";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Libra: Yesterday, you may have found yourself seeking harmony and peace in your surroundings. Take time to relax and recharge your batteries, focusing on self-care and emotional well-being. Trust in the power of intuition and allow yourself to let go of stress.";
                mood = "Peaceful";
                color = "Pink";
                luckyNumber = "19";
                luckyTime = "11am";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getScorpioHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Scorpio: As a Scorpio, you may feel a surge of intensity and passion today, driving you towards your goals with determination. Embrace your inner strength and use it to overcome challenges that come your way. Trust your instincts and stay focused on your objectives.";
                mood = "Passionate";
                color = "Red";
                luckyNumber = "47";
                luckyTime = "2pm";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Scorpio: Tomorrow brings opportunities for transformation and growth for Scorpio individuals. You may feel inspired to pursue new interests or let go of old habits that no longer serve you. Embrace change with open arms and trust in your ability to adapt.";
                mood = "Adaptive";
                color = "Black";
                luckyNumber = "56";
                luckyTime = "3pm";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Scorpio: Yesterday, you may have experienced a period of self-reflection and introspection. Take time to listen to your inner voice and pay attention to your dreams and desires. Trust in the wisdom of your intuition to guide you towards your true path.";
                mood = "Reflective";
                color = "Purple";
                luckyNumber = "28";
                luckyTime = "10am";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getSagittariusHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Sagittarius: As a Sagittarius, you may feel a strong sense of adventure and optimism today, urging you to explore new horizons and embrace opportunities for growth. Trust in your intuition and follow your heart's desires. Your spontaneous nature may lead you towards exciting experiences and meaningful connections.";
                mood = "Adventurous";
                color = "Purple";
                luckyNumber = "58";
                luckyTime = "3pm";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Sagittarius: Tomorrow brings opportunities for expansion and learning for Sagittarius individuals. You may feel inspired to pursue new interests or embark on a journey of self-discovery. Embrace curiosity and stay open to new experiences that come your way.";
                mood = "Curious";
                color = "Orange";
                luckyNumber = "33";
                luckyTime = "12pm";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Sagittarius: Yesterday, you may have experienced a period of introspection and contemplation. Take time to reflect on your beliefs and values, and consider how they align with your actions and goals. Trust in your inner wisdom to guide you towards personal growth and fulfillment.";
                mood = "Reflective";
                color = "Blue";
                luckyNumber = "21";
                luckyTime = "8am";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getCapricornHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Capricorn: As a Capricorn, you may feel a strong sense of determination and ambition today, propelling you towards your goals with unwavering focus and perseverance. Trust in your abilities and take practical steps towards achieving your long-term aspirations. Your disciplined approach will lead to success.";
                mood = "Ambitious";
                color = "Brown";
                luckyNumber = "70";
                luckyTime = "4pm";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Capricorn: Tomorrow brings opportunities for growth and recognition for Capricorn individuals. You may find yourself in a position of authority or responsibility, requiring you to demonstrate your leadership skills. Embrace challenges with confidence and integrity.";
                mood = "Determined";
                color = "Black";
                luckyNumber = "87";
                luckyTime = "9am";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Capricorn: Yesterday, you may have encountered obstacles or setbacks that tested your resilience and determination. Take time to reflect on the lessons learned from these experiences and use them to fuel your growth. Remember that challenges are opportunities for personal development.";
                mood = "Resilient";
                color = "Gray";
                luckyNumber = "45";
                luckyTime = "1pm";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getAquariusHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Aquarius: As an Aquarius, you may experience a surge of innovative ideas and humanitarian impulses today, inspiring you to make a positive impact on the world around you. Embrace your unique perspective and seek out opportunities to collaborate with like-minded individuals.";
                mood = "Innovative";
                color = "Turquoise";
                luckyNumber = "88";
                luckyTime = "5pm";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Aquarius: Tomorrow holds promises of exciting discoveries and intellectual stimulation for Aquarius individuals. You may find yourself drawn to new ideas and experiences that broaden your horizons. Embrace change with an open mind and trust in your intuition.";
                mood = "Curious";
                color = "Blue";
                luckyNumber = "29";
                luckyTime = "10am";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Aquarius: Yesterday, you may have felt a strong sense of idealism and a desire to bring about positive change in your community. Take time to reflect on your aspirations and find practical ways to turn your dreams into reality. Your unique perspective can inspire others to join your cause.";
                mood = "Idealistic";
                color = "Silver";
                luckyNumber = "17";
                luckyTime = "2pm";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope getPiscesHoroscope(String day, String currentDate, String compatibility) {
        String description, mood, color, luckyNumber, luckyTime;

        switch (day) {
            case Constant.DAY_TODAY:
                description = "Today's prediction for Pisces: As a Pisces, you may feel deeply connected to your emotions and intuition today, guiding you towards meaningful experiences and connections. Embrace your compassionate nature and trust in the universe to lead you towards fulfillment.";
                mood = "Compassionate";
                color = "Ocean Blue";
                luckyNumber = "93";
                luckyTime = "6pm";
                break;
            case Constant.DAY_TOMORROW:
                description = "Tomorrow's prediction for Pisces: Tomorrow holds promises of spiritual growth and artistic inspiration for Pisces individuals. You may find solace in creative pursuits or moments of quiet reflection. Embrace the beauty of the world around you and trust in the power of your dreams.";
                mood = "Dreamy";
                color = "Lavender";
                luckyNumber = "24";
                luckyTime = "11am";
                break;
            case Constant.DAY_YESTERDAY:
                description = "Yesterday's reflection for Pisces: Yesterday, you may have experienced a sense of nostalgia and a longing for deeper connections with loved ones. Take time to cherish cherished memories and express gratitude for the meaningful relationships in your life.";
                mood = "Reflective";
                color = "Pink";
                luckyNumber = "37";
                luckyTime = "3pm";
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }

        // Create and return a new Horoscope object with the assigned values
        return new Horoscope(
                currentDate,
                compatibility,
                luckyTime,
                luckyNumber,
                color,
                currentDate,
                mood,
                description
        );
    }
    private Horoscope generateSampleHoroscope(String day,String currentDate, String compatibility, String horoscopeName) {
        // Sample data generation based on horoscopeName
        switch (horoscopeName) {
            case "aries":
                return getAriesHoroscope(day,currentDate,compatibility);
            case "taurus":
                return getTaurusHoroscope(day,currentDate,compatibility);
            case "gemini":
                return getGeminiHoroscope(day,currentDate,compatibility);
            case "cancer":
                return getCancerHoroscope(day,currentDate,compatibility);
            case "leo":
                return getLeoHoroscope(day,currentDate,compatibility);
            case "virgo":
                return getVirgoHoroscope(day,currentDate,compatibility);
            case "libra":
                return getLibraHoroscope(day,currentDate,compatibility);
            case "scorpio":
                return getScorpioHoroscope(day,currentDate,compatibility);
            case "sagittarius":
                return getSagittariusHoroscope(day,currentDate,compatibility);
            case "capricorn":
                return getCapricornHoroscope(day,currentDate,compatibility);
            case "aquarius":
                return getAquariusHoroscope(day,currentDate,compatibility);
            case "pisces":
                return getPiscesHoroscope(day,currentDate,compatibility);
            default:
                throw new IllegalArgumentException("Invalid horoscopeName: " + horoscopeName);
        }
    }

    private void showShimmer(boolean showShimmer) {
        if (showShimmer) {
            mDetailShimmer.startShimmer();
            mDetailShimmer.showShimmer(true);
        } else {
            mDetailShimmer.stopShimmer();
            mDetailShimmer.hideShimmer();
        }
    }
}