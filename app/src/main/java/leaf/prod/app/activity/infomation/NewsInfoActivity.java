package leaf.prod.app.activity.infomation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.loopj.android.image.WebImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.layout.RoundSmartImageView;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.util.DpUtil;

public class NewsInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    public TitleView title;

    @BindView(R.id.tv_title)
    public TextView tvTitle;
    //    @BindView(R.id.tv_content)
    //    public TextView tvContent;

    @BindView(R.id.tv_time)
    public TextView tvTime;

    @BindView(R.id.tv_source)
    public TextView tvSource;

    @BindView(R.id.ll_content)
    public LinearLayout llContent;

    @BindView(R.id.sv_content)
    public ScrollView svContent;

    private static int margin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_news_info);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        margin = DpUtil.dp2Int(this, 12);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.news));
        title.clickLeftGoBack(getWContext());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        margin = DpUtil.dp2Int(this, 12);
        News news = (News) getIntent().getSerializableExtra("data");
        tvTitle.setText(news.getTitle());
        //        tvContent.setText(news.getContent());
        tvTime.setText(news.getPublishTime());
        tvSource.setText(getString(R.string.news_source) + ": " + news.getSource());
        Pattern p = Pattern.compile("<img src=\"([\\s\\S]*?)\">");
        Matcher m = p.matcher(news.getContent());
        int begin = 0;
        int end = 0;
        while (m.find()) {
            String content = news.getContent().substring(begin, m.start());
            String image = news.getContent()
                    .substring(m.start(), m.end())
                    .replace("<img src=\"", "")
                    .replace("\">", "");
            addTextView(content.trim());
            addImageView(image);
            begin = end = m.end();
        }
        if (begin == 0) {
            addTextView(news.getContent());
        }
        if (end < news.getContent().length() && !news.getContent().substring(end).trim().isEmpty()) {
            addTextView(news.getContent().substring(end).trim());
        }
        svContent.post(() -> svContent.fullScroll(ScrollView.FOCUS_UP));
    }

    @Override
    public void initData() {
    }

    private void addTextView(String content) {
        if (content.trim().isEmpty())
            return;
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, margin, 0, 0);
        textView.setLayoutParams(lp);
        textView.setTextIsSelectable(true);
        textView.setText(content);
        textView.setTextColor(getResources().getColor(R.color.colorNineText));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setLineSpacing(0, 1.5f);
        llContent.addView(textView);
    }

    private void addImageView(String url) {
        LyqbLogger.log(url);
        WebImage webImage = new WebImage(url);
        //        Bitmap bitmap = webImage.getBitmap(this);
        //        if (bitmap == null || bitmap.getWidth() < 80)
        //            return;
        RoundSmartImageView imageView = new RoundSmartImageView(this);
        imageView.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, margin, 0, 0);
        imageView.setLayoutParams(lp);
        imageView.setImage(webImage);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        llContent.addView(imageView);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
