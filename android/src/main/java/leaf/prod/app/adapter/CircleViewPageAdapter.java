package leaf.prod.app.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 *
 */
public class CircleViewPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> data;

    public CircleViewPageAdapter(FragmentManager fm, List<Fragment> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position % data.size());
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
