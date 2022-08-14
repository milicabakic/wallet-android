package rs.raf.wallet.view.viewpager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import rs.raf.wallet.view.fragments.FirstFragment;
import rs.raf.wallet.view.fragments.FourthFragment;
import rs.raf.wallet.view.fragments.SecondFragment;
import rs.raf.wallet.view.fragments.ThirdFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private final int ITEM_COUNT = 4;
    public static final int FRAGMENT_1 = 0;
    public static final int FRAGMENT_2 = 1;
    public static final int FRAGMENT_3 = 2;
    public static final int FRAGMENT_4 = 3;

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case FRAGMENT_1: fragment = new FirstFragment(); break;
            case FRAGMENT_2: fragment = new SecondFragment(); break;
            case FRAGMENT_3: fragment = new ThirdFragment(); break;
            default: fragment = new FourthFragment(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case FRAGMENT_1: return "1";
            case FRAGMENT_2: return "2";
            case FRAGMENT_3: return "3";
            default: return "4";
        }
    }
}
