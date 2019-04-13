package net.qiujuer.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.next.easynavigation.view.EasyNavigationBar;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;
import net.qiujuer.italker.common.app.Activity;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.factory.persistence.Account;
import net.qiujuer.italker.push.LaunchActivity;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.main.ActiveFragment;
import net.qiujuer.italker.push.frags.main.ContactFragment;
import net.qiujuer.italker.push.frags.main.GroupFragment;
import net.qiujuer.italker.push.frags.yctfragment.yctfragment1;
import net.qiujuer.italker.push.helper.NavHelper;
import net.qiujuer.italker.push.helper.NavHelper.Tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {

    private EasyNavigationBar navigationBar;

    private String[] tabText = {"消息","联系人","我","我"};
    //未选中icon
    private int[] normalIcon = { R.mipmap.message,R.mipmap.index, R.mipmap.me, R.mipmap.me};
    //选中时icon
    private int[] selectIcon = { R.mipmap.message1,R.mipmap.index1, R.mipmap.me1, R.mipmap.me1};
    private List<Fragment> fragments = new ArrayList<>();

    /*@BindView(R.id.navigationBar)
    EasyNavigationBar navigationBar;*/

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelper;

    /**
     * 自己添加的一个重写父类方法
     * @param savedInstanceState
     *
     */
   /* @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"老子是自己的oncreat方法:::"+"数字"+(++yct),Toast.LENGTH_LONG).show();
        this.startActivity(new Intent(this, LaunchActivity.class));
    }*/
    /**
     * MainActivity 显示的入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        //Toast.makeText(context,"show方法"+context.toString()+"数字"+(++yct),Toast.LENGTH_LONG).show();
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (Account.isComplete()) {
            // 判断用户信息是否完全，完全则走正常流程
            return super.initArgs(bundle);
        } else {
            UserActivity.show(this);
            return false;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        /*// 初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container,
                getSupportFragmentManager(), this);
        //这里先来三个tab对象，其中包含有fragment信息，有什么用呢  拭目以待吧
        Tab<Integer> tab1 = new Tab<>(ActiveFragment.class, R.string.title_home);
        Tab<Integer> tab2 = new Tab<>(GroupFragment.class, R.string.title_group);
        Tab<Integer> tab3 = new Tab<>(ContactFragment.class, R.string.title_contact);

        mNavHelper.add(R.id.action_home, tab1)//底部按钮R.id.action_home对应tab1
                .add(R.id.action_group, tab2)
                .add(R.id.action_contact, tab3);


        // 添加对底部按钮点击的监听
        mNavigation.setOnNavigationItemSelectedListener(this);*/

        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });

        navigationBar = findViewById(R.id.navigationBar);

        fragments.add(new ActiveFragment());
        fragments.add(new ContactFragment());
        fragments.add(new GroupFragment());
        fragments.add(new yctfragment1());

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .smoothScroll(true)
                .canScroll(true)
                .hasPadding(true)
                // .anim(Anim.ZoomIn)
               .build();
        navigationBar.setMsgPointCount(0, 5);
    }

    @Override
    protected void initData() {
        super.initData();



        /*// 从底部导中接管我们的Menu，然后进行手动的触发第一次点击
        Menu menu = mNavigation.getMenu();
        // 触发首次选中Home
        menu.performIdentifierAction(R.id.action_home, 0);*/

        // 初始化头像加载
        mPortrait.setup(Glide.with(this), Account.getUser());
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        PersonalActivity.show(this, Account.getUserId());
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {
        // 在群的界面的时候，点击顶部的搜索就进入群搜索界面
        // 其他都为人搜索的界面
        Toast.makeText(this,"测试这是个什么玩意："+navigationBar.getFragmentManager()
                .getFragments(),Toast.LENGTH_LONG).show();
        /*int type = Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group) ?
                SearchActivity.TYPE_GROUP : SearchActivity.TYPE_USER;
        SearchActivity.show(this, type);*/
    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
        // 浮动按钮点击时，判断当前界面是群还是联系人界面
        // 如果是群，则打开群创建的界面
        if (Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group)) {
            // 打开群创建界面
            GroupCreateActivity.show(this);
        } else {
            // 如果是其他，都打开添加用户的界面
            SearchActivity.show(this, SearchActivity.TYPE_USER);
        }
    }

    /**
     * 当我们的底部导航被点击的时候触发
     *
     * @param item MenuItem
     * @return True 代表我们能够处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 转接事件流到工具类中
        //这个item就是那个标题
        Toast.makeText(this,"傻逼"+item.getItemId(),Toast.LENGTH_LONG).show();
        return mNavHelper.performClickMenu(item.getItemId());


    }

    /**
     * NavHelper 处理后回调的方法
     *
     * @param newTab 新的Tab
     * @param oldTab 就的Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        // 从额外字段中取出我们的Title资源Id
        mTitle.setText(newTab.extra);


        // 对浮动按钮进行隐藏与显示的动画
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            // 主界面时隐藏
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            // transY 默认为0 则显示
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                // 群
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                // 联系人
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }

        // 开始动画
        // 旋转，Y轴位移，弹性差值器，时间
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();


    }
}
