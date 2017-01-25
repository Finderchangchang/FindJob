package liuliu.findjob.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.droidlover.xdroidmvp.db.FinalDb;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import liuliu.findjob.R;
import liuliu.findjob.model.Config;
import liuliu.findjob.model.UserInfo;
import liuliu.findjob.util.Utils;

/**
 * Created by Administrator on 2017/1/24.
 */

public class LoginActivity extends XActivity {
    public static LoginActivity instance;
    @BindView(R.id.tel_et)
    EditText telEt;
    @BindView(R.id.pwd_et)
    EditText pwdEt;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.reg_btn)
    Button regBtn;
    FinalDb db;

    @Override
    public void initData(Bundle savedInstanceState) {
        db = FinalDb.create(this);
        instance = this;
        regBtn.setOnClickListener(v -> Utils.IntentPost(RegActivity.class));
        loginBtn.setOnClickListener(v -> {
            String tel = telEt.getText().toString().trim();
            String pwd = pwdEt.getText().toString().trim();
            if (TextUtils.isEmpty(tel)) {
                ToastShort("请输入手机号码");
            } else if (!Utils.isMobileNo(tel)) {
                ToastShort("请输入正确的手机号码");
            } else if (TextUtils.isEmpty(pwd)) {
                ToastShort("请输入密码");
            } else {
                BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
                query.addWhereEqualTo("username", tel);
                query.addWhereEqualTo("password", pwd);
                query.include("type");
                query.findObjects(new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        if (e == null) {
                            db.deleteAll(UserInfo.class);
                            UserInfo info = list.get(0);
                            info.setTypeName(info.getType().getName());
                            db.save(info);
                            Utils.IntentPost(MainActivity.class);
                            Utils.putCache(Config.KEY_User_ID, tel);
                            finish();
                        } else {
                            ToastShort("用户名或密码错误请重新输入");
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_login;
    }

    @Override
    public Object newP() {
        return null;
    }
}
