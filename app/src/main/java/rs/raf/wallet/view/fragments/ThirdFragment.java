package rs.raf.wallet.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import rs.raf.wallet.R;
import rs.raf.wallet.view.viewpager.TabsAdapter;

public class ThirdFragment extends Fragment {


    private ViewPager viewPager;
    private TabLayout tabLayout;

    public ThirdFragment() {
        super(R.layout.fragment_third);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view){
        initView(view);
        initTabs(view);
    }

    private void initView(View view){
        viewPager = view.findViewById(R.id.viewPagerTabs);
        tabLayout = view.findViewById(R.id.tabLayout);
    }

    private void initTabs(View view){
        viewPager.setAdapter(new TabsAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }


}
