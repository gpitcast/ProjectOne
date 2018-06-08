package com.feature.projectone.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.fragment.HomeFragment;
import com.feature.projectone.fragment.MineFragment;
import com.feature.projectone.fragment.OrderFragment;
import com.feature.projectone.fragment.ShoppingCarFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.CheckLoginUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements RongIM.UserInfoProvider, RongIM.GroupInfoProvider {

    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.tv_home)
    TextView tv_home;
    @BindView(R.id.iv_order)
    ImageView iv_order;
    @BindView(R.id.tv_order)
    TextView tv_order;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.iv_mine)
    ImageView iv_mine;
    @BindView(R.id.tv_mine)
    TextView tv_mine;
    @BindView(R.id.iv_shopping_car)
    ImageView iv_shopping_car;
    @BindView(R.id.tv_shopping_car)
    TextView tv_shopping_car;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ll_jump_to_contact)
    LinearLayout ll_jump_to_contact;

    private static final String RongImUrl = HttpUtils.Host + "/rongyun/get_token";//获取融云token的接口
    private static final String userInfoUrl = HttpUtils.Host + "/user/user_info";//用户信息接口
    private static final String AllGroupListUrl = HttpUtils.Host + "/qunzhu/get_all_list";//获取用户加入的所有的群组信息
    private List<HashMap<String, Object>> groupList = new ArrayList<>();//存储群组的头像名称id等信息的集合
    private Fragment homeFragment, orderFragment, mineFragment, shoppingCarFragment;
    private ConversationListFragment conversationListFragment;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    PostTokenList();//获取融云的token接口
                    break;
                case 1:
                    PostUserInfoList();//请求用户信息接口
                    break;
                case 2:
                    PostGroupInfoList();//请求
                    break;
            }
        }
    };


    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case RongImUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) resultMap.get("data");
                        String token = dataMap.get("token") + "";
                        if (token != null && token.length() > 0) {
                            Constanst.RongToken = token;
                            connectRongIM();//连接服务器
                        }

                    } else {
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case userInfoUrl:
                //发送请求群组信息的接口的消息
                Message msg1 = Message.obtain();
                msg1.what = 2;
                mHandler.sendMessage(msg1);

                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //请求成功
                        HashMap<String, Object> dataList = (HashMap<String, Object>) resultMap.get("data");
                        //昵称
                        if (dataList.get("nickname") != null) {
                            Constanst.userNickName = dataList.get("nickname") + "";
                        } else {
                            Constanst.userNickName = "";
                        }
                        //真实姓名
                        if (dataList.get("realname") != null) {
                            Constanst.userRealName = dataList.get("realname") + "";
                        } else {
                            Constanst.userRealName = "";
                        }
                        //手机号码
                        if (dataList.get("username") != null) {
                            Constanst.userPhoneNum = dataList.get("username") + "";
                        } else {
                            Constanst.userPhoneNum = "";
                        }
                        //头像url
                        if (dataList.get("img") != null) {
                            Constanst.userHeadImg = dataList.get("img") + "";
                        } else {
                            Constanst.userHeadImg = "";
                        }
                        //性别
                        if (dataList.get("sex") != null) {
                            Constanst.userSex = dataList.get("sex") + "";
                        } else {
                            Constanst.userSex = "";
                        }
                        //生日
                        if (dataList.get("birthday") != null) {
                            Constanst.userBirthday = dataList.get("birthday") + "";
                        } else {
                            Constanst.userBirthday = "";
                        }
                        //邮寄地址
                        if (dataList.get("address") != null) {
                            Constanst.userAddress = dataList.get("address") + "";
                        } else {
                            Constanst.userAddress = "";
                        }
                        //身份
                        if (dataList.get("role_id") != null) {
                            Constanst.user_role_id = dataList.get("role_id") + "";
                        } else {
                            Constanst.user_role_id = "";
                        }

                        initUserInfo();
                    } else {
                        //请求失败
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case AllGroupListUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                    if (arrayList != null) {
                        groupList.addAll(arrayList);
                    }
                    initGroupInfo();
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        tv_title.setText("消息");
        ll_back.setVisibility(View.GONE);
        ll_jump_to_contact.setVisibility(View.VISIBLE);

        showHomeFragment();

        //请求融云接口需要登录,所以先判断是否登录
        if (CheckLoginUtil.isLogin(this)) {
            //初始化先请求融云token接口
            Message msg = Message.obtain();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (orderFragment != null) {
            transaction.hide(orderFragment);
        }
        if (shoppingCarFragment != null) {
            transaction.hide(shoppingCarFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
        if (conversationListFragment != null) {
            transaction.hide(conversationListFragment);
        }
    }

    //显示首页fragment
    private void showHomeFragment() {
        rl_title.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            transaction.add(R.id.framelayout, homeFragment);
        }
        hideFragment(transaction);
        transaction.show(homeFragment);//显示选中的fragment
        transaction.commit();//提交
    }

    //显示订单fragment
    private void showOrderFragment() {
        rl_title.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (orderFragment == null) {
            orderFragment = new OrderFragment();
            transaction.add(R.id.framelayout, orderFragment);
        }
        hideFragment(transaction);
        transaction.show(orderFragment);//显示选中的fragment
        transaction.commit();//提交
    }

    //显示购物车fragment
    private void showShoppingCarFragment() {
        rl_title.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (shoppingCarFragment == null) {
            shoppingCarFragment = new ShoppingCarFragment();
            transaction.add(R.id.framelayout, shoppingCarFragment);
        }
        hideFragment(transaction);
        transaction.show(shoppingCarFragment);//显示选中的fragment
        transaction.commit();//提交
    }

    //显示消息fragment
    private void showMessageFragment() {
        rl_title.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (conversationListFragment == null) {
            conversationListFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")//设置私聊会话，该会话聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话时候显示
                    .appendQueryParameter(Conversation.ConversationType.CHATROOM.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.CUSTOMER_SERVICE.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.NONE.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.PUSH_SERVICE.getName(), "false")
                    .build();
            conversationListFragment.setUri(uri);
            transaction.add(R.id.framelayout, conversationListFragment);
        }
        hideFragment(transaction);
        transaction.show(conversationListFragment);//显示选中的fragment
        transaction.commit();//提交
    }

    //显示我的fragment
    private void showMineFragment() {
        rl_title.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mineFragment == null) {
            mineFragment = new MineFragment();
            transaction.add(R.id.framelayout, mineFragment);
        }
        hideFragment(transaction);
        transaction.show(mineFragment);//显示选中的fragment
        transaction.commit();//提交
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.rl_home, R.id.rl_order, R.id.ll_shopping_car, R.id.rl_message, R.id.rl_mine, R.id.ll_jump_to_contact})
    public void Onclick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//操作fragment的事物
        switch (view.getId()) {
            case R.id.rl_home:
                //首页底部导航栏点击事件
                homeSelected();//改变导航栏图标
                showHomeFragment();//显示首页的fragment
                break;
            case R.id.rl_order:
                //订单底部导航栏点击事件
                orderSelected();//改变导航栏图标
                showOrderFragment();//显示选中的fragment
                break;
            case R.id.ll_shopping_car:
                //购物车底部导航栏点击事件
                shopCarSelected();//改变导航栏图标
                showShoppingCarFragment();
                break;
            case R.id.rl_message:
                //消息底部导航栏点击事件
                messageSelected();//改变导航栏图标
                showMessageFragment();//显示选中的fragment
                break;
            case R.id.rl_mine:
                //我的底部导航栏点击事件
                mineSelected();//改变导航栏图标
                showMineFragment();//显示选中的fragment
                break;
            case R.id.ll_jump_to_contact:
                //点击跳转到联系人界面
                startActivity(new Intent(this, ContactsActivity.class));
                break;
        }
    }

    //首页被选中
    private void homeSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_red);
        tv_home.setTextColor(getResources().getColor(R.color.orangeone1));
        iv_order.setImageResource(R.mipmap.icon_order_list_gray);
        tv_order.setTextColor(getResources().getColor(R.color.grayone));
        iv_message.setImageResource(R.mipmap.icon_message_gray);
        tv_message.setTextColor(getResources().getColor(R.color.grayone));
        iv_mine.setImageResource(R.mipmap.icon_mine_gray);
        tv_mine.setTextColor(getResources().getColor(R.color.grayone));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_gray);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.grayone));
    }

    //订单被选中
    private void orderSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_gray);
        tv_home.setTextColor(getResources().getColor(R.color.grayone));
        iv_order.setImageResource(R.mipmap.icon_order_list_red);
        tv_order.setTextColor(getResources().getColor(R.color.orangeone1));
        iv_message.setImageResource(R.mipmap.icon_message_gray);
        tv_message.setTextColor(getResources().getColor(R.color.grayone));
        iv_mine.setImageResource(R.mipmap.icon_mine_gray);
        tv_mine.setTextColor(getResources().getColor(R.color.grayone));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_gray);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.grayone));
    }


    //购物车被选中
    private void shopCarSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_gray);
        tv_home.setTextColor(getResources().getColor(R.color.grayone));
        iv_order.setImageResource(R.mipmap.icon_order_list_gray);
        tv_order.setTextColor(getResources().getColor(R.color.grayone));
        iv_message.setImageResource(R.mipmap.icon_message_gray);
        tv_message.setTextColor(getResources().getColor(R.color.grayone));
        iv_mine.setImageResource(R.mipmap.icon_mine_gray);
        tv_mine.setTextColor(getResources().getColor(R.color.grayone));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_red);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.orangeone1));
    }

    //消息被选中
    private void messageSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_gray);
        tv_home.setTextColor(getResources().getColor(R.color.grayone));
        iv_order.setImageResource(R.mipmap.icon_order_list_gray);
        tv_order.setTextColor(getResources().getColor(R.color.grayone));
        iv_message.setImageResource(R.mipmap.icon_message_red);
        tv_message.setTextColor(getResources().getColor(R.color.orangeone1));
        iv_mine.setImageResource(R.mipmap.icon_mine_gray);
        tv_mine.setTextColor(getResources().getColor(R.color.grayone));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_gray);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.grayone));
    }

    //我的被选中
    private void mineSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_gray);
        tv_home.setTextColor(getResources().getColor(R.color.grayone));
        iv_order.setImageResource(R.mipmap.icon_order_list_gray);
        tv_order.setTextColor(getResources().getColor(R.color.grayone));
        iv_message.setImageResource(R.mipmap.icon_message_gray);
        tv_message.setTextColor(getResources().getColor(R.color.grayone));
        iv_mine.setImageResource(R.mipmap.icon_mine_red);
        tv_mine.setTextColor(getResources().getColor(R.color.orangeone1));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_gray);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.grayone));
    }

    @Subscribe
    public void onEventMainThread(String str) {
        if (str == null) {
            return;
        }
        switch (str) {
            case "关闭MainActivity":
                finish();
                break;
            case "修改登录密码成功":
                finish();
                break;
        }
    }

    //获取融云的token接口
    private void PostTokenList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "rongyun");
        map.put("action", "get_token");
        getJsonUtil().PostJson(this, map);
    }

    //请求用户信息接口
    public void PostUserInfoList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "user_info");
        getJsonUtil().PostJson(this, map);
    }

    //获取群组信息的接口
    private void PostGroupInfoList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "get_all_list");
        getJsonUtil().PostJson(this, map);
    }

    private UserInfo user;

    //初始化登录的用户UserInfo对象,实现反馈个融云用户信息的接口
    private void initUserInfo() {
        Log.i("MainActivity", "    Constanst.userPhoneNum:      " + Constanst.userPhoneNum);
        Log.i("MainActivity", "    Constanst.userNickName:      " + Constanst.userNickName);
        Log.i("MainActivity", "    Constanst.userHeadImg:      " + Constanst.userHeadImg);
        user = new UserInfo(Constanst.userPhoneNum, Constanst.userNickName, Uri.parse(Constanst.userHeadImg));
        //设置用户信息
        RongIM.setUserInfoProvider(this, true);
        RongIM.getInstance().refreshUserInfoCache(user);
    }

    //反馈给融云登录的用户信息
    @Override
    public UserInfo getUserInfo(String userId) {
        return user;
    }

    //实现反馈个融云群组信息的接口
    private void initGroupInfo() {
        RongIM.setGroupInfoProvider(this, true);
    }

    //反馈给融云群组信息
    @Override
    public Group getGroupInfo(String s) {
        for (int i = 0; i < groupList.size(); i++) {
            HashMap<String, Object> map = groupList.get(i);
            String groupId = map.get("groupId") + "";
            if (s.equals(groupId)) {
                String img = map.get("img") + "";
                Uri imgUri = Uri.parse(img);
                Group group = new Group(groupId, map.get("name") + "", imgUri);
                RongIM.getInstance().refreshGroupInfoCache(group);
                return group;
            }
        }
        return null;
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link # i n  i t(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     * <p>
     * token    从服务端获取的用户身份令牌（Token）。
     * callback 连接回调。
     *
     * @return RongIM  客户端核心类的实例。
     */

    private void connectRongIM() {
        RongIM.connect(Constanst.RongToken, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.i("RongIM", "        token错误       ");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.i("RongIM", "        连接融云成功      userid  " + userid);
                //融云连接成功后,发送请求用户信息的接口的消息
                Message message = Message.obtain();
                message.what = 1;
                mHandler.sendMessage(message);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("RongIM", "        连接融云失败       errorCode.getMessage()     " + errorCode.getMessage());
                Log.i("RongIM", "        连接融云失败       errorCode.getValue()     " + errorCode.getValue());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "         MainActivity被销毁了        ");
        RongIM.getInstance().disconnect();
        EventBus.getDefault().unregister(this);
    }


    private long firstTime = 0;//记录首次点击的时间,首次初始化

    //实现点击两次退出应用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                System.exit(0);
            } else {
                ToastUtil.show(this, "再按一次退出程序", 0);
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
