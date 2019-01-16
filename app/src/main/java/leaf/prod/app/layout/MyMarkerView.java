
package leaf.prod.app.layout;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import leaf.prod.app.R;
import leaf.prod.walletsdk.model.Trend;

@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {

    private final TextView tvContent;

    private List<Trend> trends;

    public MyMarkerView(Context context, int layoutResource, List<Trend> tends) {
        super(context, layoutResource);
        this.trends = tends;
        this.tvContent = findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Trend trend = trends.get((int) e.getX());
        tvContent.setText(trend.getRange());
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF((-getWidth() / 2), -800-getHeight());
    }
}
