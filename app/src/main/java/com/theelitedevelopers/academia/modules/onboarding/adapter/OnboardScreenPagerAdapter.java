package com.theelitedevelopers.academia.modules.onboarding.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.theelitedevelopers.academia.R;

public class OnboardScreenPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;

    public OnboardScreenPagerAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
        R.drawable.academia_assignment,
        R.drawable.academia_chat,
        R.drawable.academia_timetable,
    };

    public String[] slide_titles = {
            "Never Forget Your Assignments",
            "Communicate with your Course mates",
            "Important Announcements"
    };

    public String[] slide_description = {
            "'I forgot to do my assignment' No Be For Here.",
            "Ask questions and chat privately with your course mates.",
            "Get important announcements at your fingertip and stay up to date."
    };

    @Override
    public int getCount() {
        return slide_description.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_layout, container, false);

        ImageView imageView = view.findViewById(R.id.image);
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);

        imageView.setImageResource(slide_images[position]);
        title.setText(slide_titles[position]);
        description.setText(slide_description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
